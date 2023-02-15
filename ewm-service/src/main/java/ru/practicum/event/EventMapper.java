package ru.practicum.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.constant.MainConstant;
import ru.practicum.event.dto.*;
import ru.practicum.event.enam.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event mapToNewEvent(User user, NewEventDto newEventDto, Category category) {
        Event newEvent = new Event();
        newEvent.setAnnotation(newEventDto.getAnnotation());
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setCategory(category);
        newEvent.setDescription(newEventDto.getDescription());
        newEvent.setEventDate(
                LocalDateTime.parse(newEventDto.getEventDate(), MainConstant.FORMATTER));
        newEvent.setInitiator(user);
        newEvent.setLat(newEventDto.getLocation().getLat());
        newEvent.setLon(newEventDto.getLocation().getLon());
        newEvent.setPaid(newEventDto.getPaid());
        newEvent.setParticipantLimit(newEventDto.getParticipantLimit());
        newEvent.setRequestModeration(newEventDto.getRequestModeration());
        newEvent.setState(EventState.PENDING);
        newEvent.setTitle(newEventDto.getTitle());
        return newEvent;
    }

    public static EventFullDto mapToFullDtoFromEvent(Event event, int request) {
        EventFullDto fullDto = new EventFullDto();
        fullDto.setAnnotation(event.getAnnotation());
        fullDto.setCategory(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()));
        fullDto.setConfirmedRequests(request);
        fullDto.setCreatedOn(event.getCreatedOn().format(MainConstant.FORMATTER));
        fullDto.setDescription(event.getDescription());
        fullDto.setEventDate(event.getEventDate().format(MainConstant.FORMATTER));
        fullDto.setId(event.getId());
        fullDto.setInitiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()));
        fullDto.setLocation(new Location(event.getLat(), event.getLon()));
        fullDto.setPaid(event.isPaid());
        fullDto.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            fullDto.setPublishedOn(event.getPublishedOn().format(MainConstant.FORMATTER));
        }
        fullDto.setRequestModeration(event.isRequestModeration());
        fullDto.setState(event.getState().toString());
        fullDto.setTitle(event.getTitle());
        return fullDto;
    }

    public static EventShortDto mapToShortDto(Event event, int request) {
        EventShortDto shortDto = new EventShortDto();
        shortDto.setAnnotation(event.getAnnotation());
        shortDto.setCategory(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()));
        shortDto.setConfirmedRequests(request);
        shortDto.setEventDate(event.getEventDate().format(MainConstant.FORMATTER));
        shortDto.setId(event.getId());
        shortDto.setInitiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()));
        shortDto.setPaid(event.isPaid());
        shortDto.setTitle(event.getTitle());
        return shortDto;
    }

    public static Event mapUpdateEvent(Event event, UpdateEventUserRequest updateEvent) {
        return getEvent(
                event,
                updateEvent.getAnnotation(),
                updateEvent.getDescription(),
                updateEvent.getEventDate(),
                updateEvent.getLocation(),
                updateEvent.getPaid(),
                updateEvent.getParticipantLimit(),
                updateEvent.getRequestModeration(),
                updateEvent.getTitle()
        );
    }

    public static Event mapUpdateEventByAdmin(Event event, UpdateEventAdminRequest updateEvent) {
        return getEvent(event,
                updateEvent.getAnnotation(),
                updateEvent.getDescription(),
                updateEvent.getEventDate(),
                updateEvent.getLocation(),
                updateEvent.getPaid(),
                updateEvent.getParticipantLimit(),
                updateEvent.getRequestModeration(),
                updateEvent.getTitle()
        );
    }

    public static List<EventShortDto> getListOfEventShortDto(List<Event> eventsList, Collection<Request> confRequests) {
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        List<Request> confirmListForOne = new ArrayList<>();
        for (Event event : eventsList) {
            for (Request request : confRequests) {
                if (request.getEvent().getId() == event.getId()) {
                    confirmListForOne.add(request);
                }
            }
            eventShortDtos.add(EventMapper.mapToShortDto(event, confirmListForOne.size()));
            confirmListForOne.clear();
        }
        return eventShortDtos;
    }

    private static Event getEvent(
            Event event,
            String annotation,
            String description,
            String eventDate,
            Location location,
            Boolean paid,
            Integer participantLimit,
            Boolean requestModeration,
            String title) {
        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        if (description != null) {
            event.setDescription(description);
        }
        if (eventDate != null) {
            event.setEventDate(LocalDateTime.parse(
                    eventDate, MainConstant.FORMATTER));
        }
        if (location != null) {
            event.setLon(location.getLon());
            event.setLat(location.getLat());
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        if (title != null) {
            event.setTitle(title);
        }
        return event;
    }

}

