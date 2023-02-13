package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.enam.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.InvalidMadeRequestException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.request.RequestMapper;
import ru.practicum.request.RequestState;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        User user = validateUser(userId);
        Event event = validateEvent(eventId);
        if (event.getInitiator().getId() == userId) {
            throw new InvalidMadeRequestException("Пользователь не может подать заявку для своего события.");
        }
        if (!(event.getState().equals(EventState.PUBLISHED))) {
            throw new InvalidMadeRequestException("Нет опубликованного события с ID = " + eventId + ".");
        }
        if ((event.getParticipantLimit() != 0)) {
            validateLimitRequest(event);
        }
        Request newRequest = RequestMapper.mapToNewRequest(user, event);
        if (event.isRequestModeration()) {
            newRequest.setStatus(RequestState.PENDING);
        } else {
            newRequest.setStatus(RequestState.CONFIRMED);
        }
        Request saveRequest = requestRepository.save(newRequest);
        log.info("Создана новая заявка на участие: {}", newRequest);
        return RequestMapper.mapToDto(saveRequest);
    }

    @Transactional
    @Override
    public ParticipationRequestDto updateUsersRequest(int userId, int requestId) {
        validateUser(userId);
        Optional<Request> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new ObjectNotFoundException("Запроса с ID = " + requestId + " не существует.");
        }
        if (request.get().getRequester().getId() != userId) {
            throw new ObjectNotFoundException("У пользователя с ID = " + userId + " нет запроса с ID = " + requestId + ".");
        }
        request.get().setStatus(RequestState.CANCELED);
        log.info("Запрос пользователя ID = {} отменен.", userId);
        return RequestMapper.mapToDto(request.get());
    }

    @Override
    public Collection<ParticipationRequestDto> getUsersRequests(int userId) {
        User user = validateUser(userId);
        Collection<ParticipationRequestDto> requestDtos = new ArrayList<>();
        Collection<Request> requests = requestRepository.findAllByRequester(user);
        for (Request request : requests) {
            requestDtos.add(RequestMapper.mapToDto(request));
        }
        log.info("Получены запросы пользователя ID = {}. {}", userId, requestDtos);
        return requestDtos;
    }

    @Override
    public Collection<ParticipationRequestDto> getRequestForUsersEvent(int userId, int eventId) {
        validateUser(userId);
        Event event = validateEvent(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new ObjectNotFoundException("У пользователя с ID = "
                    + userId + " нет события с ID = " + eventId + ".");
        }
        Collection<Request> requestsList = requestRepository.findAllByEvent(event);
        Collection<ParticipationRequestDto> listDto = new ArrayList<>();
        for (Request request : requestsList) {
            listDto.add(RequestMapper.mapToDto(request));
        }
        log.info("Получены запросы на участие в событии ID = {}. {}", eventId, requestsList);
        return listDto;
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult getUpdateRequestByEventsOwner(
            int userId, int eventId, EventRequestStatusUpdateRequest updateRequest) {

        validateUser(userId);
        Event event = validateEvent(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new ObjectNotFoundException("У пользователя с ID = "
                    + userId + " нет события с ID = " + eventId + ".");
        }
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        Collection<Request> requests = validateRequestBeforeUpdate(event);
        int availLimit = event.getParticipantLimit();
        if (event.getParticipantLimit() != 0) {
            int close = 0;
            for (Request request : requests) {
                if (request.getStatus().equals(RequestState.CONFIRMED)) {
                    close++;
                }
            }
            availLimit = availLimit - close;
            if (availLimit <= 0) {
                throw new InvalidMadeRequestException("Нет свободных мест для участия.");
            }
        }

        RequestState requestState = validateStatus(updateRequest.getStatus());
        for (int reqId : updateRequest.getRequestIds()) {
            int limit = 0;
            for (Request request : requests) {
                if (request.getId() == reqId && request.getStatus().equals(RequestState.PENDING)) {
                    switch (requestState) {
                        case REJECTED:
                            request.setStatus(RequestState.REJECTED);
                            requestRepository.save(request);
                            result.setRejectedRequests(List.of(RequestMapper.mapToDto(request)));
                            break;
                        case CONFIRMED:
                            if (limit <= availLimit) {
                                request.setStatus(RequestState.CONFIRMED);
                                requestRepository.save(request);
                                result.setConfirmedRequests(List.of(RequestMapper.mapToDto(request)));
                                limit++;
                            } else {
                                request.setStatus(RequestState.REJECTED);
                                requestRepository.save(request);
                                result.setRejectedRequests(List.of(RequestMapper.mapToDto(request)));
                            }
                            break;
                    }
                }
            }
        }
        log.info("Обработаны запросы {}", result);
        return result;
    }

    private User validateUser(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Пользователя с ID = " + userId + " не существует.");
        }
        return user.get();
    }

    private Event validateEvent(int eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new ObjectNotFoundException("Нет события с ID = " + eventId + ".");
        }
        return event.get();
    }

    private void validateLimitRequest(Event event) {
        Collection<Request> requests = requestRepository.findAllByEventAndStatus(event, RequestState.CONFIRMED);
        if (requests.size() >= event.getParticipantLimit()) {
            throw new InvalidMadeRequestException("Нет свободных мест для участия.");
        }
    }

    private Collection<Request> validateRequestBeforeUpdate(Event event) {
        Collection<Request> requests = requestRepository.findAllByEvent(event);
        if (requests.isEmpty()) {
            throw new ObjectNotFoundException("Запросов к этому событию не найдено.");
        }
        return requests;
    }

    private RequestState validateStatus(String state) {
        RequestState requestState;
        try {
            requestState = RequestState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new InvalidMadeRequestException("Unknown state: " + state);
        }
        return requestState;
    }

}
