package ru.practicum.event.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsDtoIn;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.constant.MainConstant;
import ru.practicum.controller.ClientController;
import ru.practicum.event.*;
import ru.practicum.event.comparator.EventDataComparator;
import ru.practicum.event.comparator.EventViewComparator;
import ru.practicum.event.dto.*;
import ru.practicum.event.enam.EventSort;
import ru.practicum.event.enam.EventState;
import ru.practicum.event.enam.StateActionAdmin;
import ru.practicum.event.enam.StateActionUser;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.qdsl.EventFilterAdmin;
import ru.practicum.event.repository.qdsl.EventFilterPublic;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.qdsl.QPredicates;
import ru.practicum.exception.InvalidMadeRequestException;
import ru.practicum.exception.InvalidUpdateStatus;
import ru.practicum.exception.InvalidValidationException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.request.RequestState;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ClientController clientController;
    private final RequestRepository requestRepository;
    private final EventStatsService statsService;
    private final QEvent qEvent = QEvent.event;

    @Transactional
    @Override
    public EventFullDto createEvent(int userId, NewEventDto newEventDto) {
        User user = validateUser(userId);
        validateEventDateForUser(newEventDto.getEventDate());
        Category category = validateCategory(newEventDto.getCategory());
        Event createEvent = EventMapper.mapToNewEvent(user, newEventDto, category);
        eventRepository.save(createEvent);
        log.info("Создано новое событие: {}", createEvent);
        EventFullDto fullDto = EventMapper.mapToFullDtoFromEvent(createEvent, 0);
        fullDto.setViews(0);
        return fullDto;
    }

    @Override
    public Collection<EventShortDto> getAllUsersEvents(int userId, int from, int size) {
        validateUser(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<Event> events = eventRepository.findAllUsersEvents(userId, pageable).getContent();
        Collection<Request> confirmList = requestRepository.findAllByEventInAndStatus(
                List.copyOf(events),
                RequestState.CONFIRMED);
        List<EventShortDto> shortDtos = EventMapper.getListOfEventShortDto(List.copyOf(events), confirmList);
        for (EventShortDto dto : shortDtos) {
            dto.setViews(statsService.getStatViewEvents(dto.getId()));
        }
        log.info("Получен список событий пользователя ID = {}", userId);
        return shortDtos;
    }

    @Override
    public EventFullDto getUsersEventById(int userId, int eventId) {
        validateUser(userId);
        Event event = validateEvent(eventId, userId);
        Collection<Request> confirmList = requestRepository.findAllByEventAndStatus(event, RequestState.CONFIRMED);
        log.info("Получено событие ID = {}.{}", eventId, event);
        EventFullDto fullDto = EventMapper.mapToFullDtoFromEvent(event, confirmList.size());
        fullDto.setViews(statsService.getStatViewEvents(eventId));
        return fullDto;
    }

    @Transactional
    @Override
    public EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest updateEvent) {
        validateUser(userId);
        Event oldEvent = validateEvent(eventId, userId);
        if (updateEvent.getEventDate() != null) {
            validateEventDateForUser(updateEvent.getEventDate());
        }
        if (oldEvent.getState().equals(EventState.PUBLISHED)) {
            throw new InvalidUpdateStatus("Можно обновить только не опубликованные события.");
        }
        if (updateEvent.getStateAction() != null) {
            StateActionUser stateAction = validateActionStateForUser(updateEvent.getStateAction());
            switch (stateAction) {
                case SEND_TO_REVIEW:
                    oldEvent.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    oldEvent.setState(EventState.CANCELED);
                    break;
            }
        }
        if (updateEvent.getCategory() != null) {
            Category category = validateCategory(updateEvent.getCategory());
            oldEvent.setCategory(category);
        }
        Event newEvent = eventRepository.save(EventMapper.mapUpdateEvent(oldEvent, updateEvent));
        Collection<Request> confirmList = requestRepository.findAllByEventAndStatus(newEvent, RequestState.CONFIRMED);
        log.info("Событие ID = {} обновлено. {}", eventId, newEvent);
        EventFullDto fullDto = EventMapper.mapToFullDtoFromEvent(newEvent, confirmList.size());
        fullDto.setViews(statsService.getStatViewEvents(eventId));
        return fullDto;
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Optional<Event> oldEvent = eventRepository.findById(eventId);
        Event updateEvent;
        if (oldEvent.isEmpty()) {
            throw new ObjectNotFoundException("Событие с ID = " + eventId + " не найдено.");
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            validateEventDateForAdmin(updateEventAdminRequest.getEventDate());
        }
        updateEvent = oldEvent.get();
        if (updateEventAdminRequest.getStateAction() != null) {
            updateStateEventByAdmin(updateEvent, updateEventAdminRequest.getStateAction());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = validateCategory(updateEventAdminRequest.getCategory());
            updateEvent.setCategory(category);
        }
        Event newEvent = eventRepository.save(EventMapper.mapUpdateEventByAdmin(updateEvent, updateEventAdminRequest));
        Collection<Request> confirmList = requestRepository.findAllByEventAndStatus(newEvent, RequestState.CONFIRMED);
        log.info("Событие ID = {} обновлено. {}", eventId, newEvent);
        EventFullDto fullDto = EventMapper.mapToFullDtoFromEvent(newEvent, confirmList.size());
        fullDto.setViews(statsService.getStatViewEvents(eventId));
        return fullDto;
    }

    @Override
    public EventFullDto getEventById(int id, HttpServletRequest request) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new ObjectNotFoundException("События с ID = " + id + " не найдено.");
        }
        if (event.get().getState() != EventState.PUBLISHED) {
            throw new ObjectNotFoundException("События с ID = " + id + " недоступно.");
        }
        StatsDtoIn statsDtoIn = new StatsDtoIn(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(MainConstant.FORMATTER)
        );
        saveStat(statsDtoIn);
        Collection<Request> confReqs = requestRepository.findAllByEventAndStatus(event.get(), RequestState.CONFIRMED);
        EventFullDto eventDto = EventMapper.mapToFullDtoFromEvent(event.get(), confReqs.size());
        eventDto.setViews(statsService.getStatViewEvents(event.get().getId()));
        log.info("Получено событие ID = {}. {}", id, eventDto);
        return eventDto;
    }

    @Override
    public Collection<EventFullDto> getAllEventsByAdmin(EventParamAdmin param, int from, int size) {
        LocalDateTime start;
        LocalDateTime end;
        EventFilterAdmin filter = new EventFilterAdmin();
        Pageable pageable = PageRequest.of(from / size, size);
        if (param.getRangeStart() != null) {
            start = validateParamRange(param.getRangeStart());
            filter.setRangeStart(start);
        }
        if (param.getRangeEnd() != null) {
            end = validateParamRange(param.getRangeEnd());
            filter.setRangeEnd(end);
        }
        if (param.getUsersId() != null) {
            ArrayList<User> users = userRepository.findAll(param.getUsersId());
            filter.setUsers(users);
        }
        if (param.getStates() != null) {
            ArrayList<EventState> states = new ArrayList<>();
            for (String state : param.getStates()) {
                states.add(validateEventState(state));
            }
            filter.setStates(states);
        }
        if (param.getCategoriesId() != null) {
            ArrayList<Category> categories = categoryRepository.findAll(param.getCategoriesId());
            filter.setCategories(categories);
        }
        Predicate predicate = QPredicates.builder()
                .add(filter.getUsers(), qEvent.initiator::in)
                .add(filter.getStates(), qEvent.state::in)
                .add(filter.getCategories(), qEvent.category::in)
                .add(filter.getRangeStart(), qEvent.eventDate::after)
                .add(filter.getRangeEnd(), qEvent.eventDate::before)
                .buildAnd();
        Collection<Event> eventsList = eventRepository.findAll(predicate, pageable).getContent();
        Collection<EventFullDto> result = new ArrayList<>();
        Collection<Request> confirmList = requestRepository.findAllByEventInAndStatus(
                List.copyOf(eventsList),
                RequestState.CONFIRMED);
        int confirm;
        for (Event event : eventsList) {
            confirm = 0;
            for (Request request : confirmList) {
                if (request.getEvent().getId() == event.getId()) {
                    confirm++;
                }
            }
            EventFullDto eventFullDto = EventMapper.mapToFullDtoFromEvent(event, confirm);
            eventFullDto.setViews(statsService.getStatViewEvents(event.getId()));
            result.add(eventFullDto);
        }
        log.info("Получен списак событий для Админ размером : {}", result.size());
        return result;
    }

    @Override
    public Collection<EventShortDto> getAllEventsByPub(
            EventParamPublic param, int from, int size, HttpServletRequest request) {
        EventFilterPublic filterPublic = new EventFilterPublic();
        LocalDateTime start;
        LocalDateTime end;
        Pageable pageable = PageRequest.of(from / size, size);
        if (param.getRangeStart() != null) {
            start = validateParamRange(param.getRangeStart());
        } else {
            start = LocalDateTime.now();
        }
        filterPublic.setRangeStart(start);
        if (param.getRangeEnd() != null) {
            end = validateParamRange(param.getRangeEnd());
            filterPublic.setRangeEnd(end);
        }
        if (param.getText() != null) {
            filterPublic.setText(param.getText());
        }
        if (param.getCategoriesId() != null) {
            ArrayList<Category> categories = categoryRepository.findAll(param.getCategoriesId());
            filterPublic.setCategories(categories);
        }
        if (param.getPaid() != null) {
            filterPublic.setPaid(param.getPaid());
        }
        if (param.getOnlyAvailable()) {
            filterPublic.setAvailable(true);
        }

        StatsDtoIn statsDtoIn = new StatsDtoIn(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(MainConstant.FORMATTER)
        );
        saveStat(statsDtoIn);
        Predicate predicate = QPredicates.builder()
                .add(filterPublic.getText(), qEvent.annotation::containsIgnoreCase)
                .add(filterPublic.getCategories(), qEvent.category::in)
                .add(filterPublic.getPaid(), qEvent.paid::in)
                .add(filterPublic.getEventState(), qEvent.state::in)
                .add(filterPublic.getRangeStart(), qEvent.eventDate::after)
                .add(filterPublic.getRangeEnd(), qEvent.eventDate::before)
                .buildAnd();

        Collection<Event> eventsList = eventRepository.findAll(predicate, pageable).getContent();
        List<EventShortDto> result = new ArrayList<>();
        Collection<Request> confirmList = requestRepository.findAllByEventInAndStatus(
                List.copyOf(eventsList),
                RequestState.CONFIRMED);
        int confirm;
        for (Event event : eventsList) {
            confirm = 0;
            for (Request req : confirmList) {
                if (req.getEvent().getId() == event.getId()) {
                    confirm++;
                }
            }
            EventShortDto eventShortDto = EventMapper.mapToShortDto(event, confirm);
            eventShortDto.setViews(statsService.getStatViewEvents(event.getId()));
            result.add(eventShortDto);
        }

        if (param.getSort() != null) {
            EventSort sort = validateEventSort(param.getSort());
            switch (sort) {
                case EVENT_DATE:
                    EventDataComparator eventDataComparator = new EventDataComparator();
                    result.sort(eventDataComparator);
                    break;
                case VIEWS:
                    EventViewComparator eventViewComparator = new EventViewComparator();
                    result.sort(eventViewComparator);
                    break;
            }
        }
        log.info("Получен списак событий для Паблик размером : {}", result.size());
        return result;
    }

    private User validateUser(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Пользователя с ID = " + userId + " не существует.");
        }
        return user.get();
    }

    private Event validateEvent(int eventId, int userId) {
        Optional<Event> event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event.isEmpty()) {
            throw new ObjectNotFoundException("У пользователя с ID = "
                    + userId + " нет события с ID = " + eventId + ".");
        }
        return event.get();
    }

    private Category validateCategory(int catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isEmpty()) {
            throw new ObjectNotFoundException("Категории с ID = " + catId + " не существует.");
        }
        return category.get();
    }

    private void validateEventDateForUser(String time) {
        LocalDateTime checkTime = validateParamRange(time);
        if (checkTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidMadeRequestException(
                    "Время начала события должно быть не раньше чем через 2 часа от текущего момента.");
        }
    }

    private LocalDateTime validateParamRange(String time) {
        LocalDateTime checkTime;
        try {
            checkTime = LocalDateTime.parse(time, MainConstant.FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new InvalidValidationException(
                    "Время должно быть указано в формате: " + MainConstant.TIME_FORMAT + ".");
        }
        return checkTime;
    }

    private void validateEventDateForAdmin(String time) {
        LocalDateTime checkTime = validateParamRange(time);
        if (checkTime.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new InvalidMadeRequestException(
                    "Время начала события должно быть не раньше чем через 1 часа от текущего момента.");
        }
    }

    private EventState validateEventState(String state) {
        EventState eventState;
        try {
            eventState = EventState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new InvalidMadeRequestException("Unknown state: " + state);
        }
        return eventState;
    }

    private EventSort validateEventSort(String state) {
        EventSort eventSort;
        try {
            eventSort = EventSort.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new InvalidMadeRequestException("Unknown state: " + state);
        }
        return eventSort;
    }

    private StateActionUser validateActionStateForUser(String state) {
        StateActionUser stateActionUser;
        try {
            stateActionUser = StateActionUser.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new InvalidMadeRequestException("Unknown state: " + state);
        }
        return stateActionUser;
    }

    private void updateStateEventByAdmin(Event oldEvent, String state) {
        StateActionAdmin stateActionAdmin;
        try {
            stateActionAdmin = StateActionAdmin.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new InvalidMadeRequestException("Unknown state: " + state);
        }
        switch (stateActionAdmin) {
            case PUBLISH_EVENT:
                if (oldEvent.getState().equals(EventState.PENDING)) {
                    oldEvent.setState(EventState.PUBLISHED);
                    oldEvent.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new InvalidMadeRequestException("Можно публиковать только события в состоянии PENDING");
                }
                break;
            case REJECT_EVENT:
                if ((oldEvent.getState().equals(EventState.PUBLISHED))) {
                    throw new InvalidMadeRequestException("Можно оклонить только не опубликованные события.");
                } else {
                    oldEvent.setState(EventState.CANCELED);
                }
                break;
        }
    }

    private void saveStat(StatsDtoIn statsDtoIn) {
        ResponseEntity<Object> response = clientController.createStat(statsDtoIn);
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Статистика записана успешно.");
        } else {
            log.error("Статистика не записана.");
        }
    }

}

