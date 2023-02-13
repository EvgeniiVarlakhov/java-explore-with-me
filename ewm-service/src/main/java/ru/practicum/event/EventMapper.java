package ru.practicum.event;

import ru.practicum.Constant;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.*;
import ru.practicum.event.enam.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {

    public static Event mapToNewEvent(User user, NewEventDto newEventDto, Category category) {
        Event newEvent = new Event();
        newEvent.setAnnotation(newEventDto.getAnnotation());
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setCategory(category);
        newEvent.setDescription(newEventDto.getDescription());
        newEvent.setEventDate(
                LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
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
        fullDto.setCreatedOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
        fullDto.setDescription(event.getDescription());
        fullDto.setEventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
        fullDto.setId(event.getId());
        fullDto.setInitiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()));
        fullDto.setLocation(new Location(event.getLat(), event.getLon()));
        fullDto.setPaid(event.isPaid());
        fullDto.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            fullDto.setPublishedOn(event.getPublishedOn().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
        }
        fullDto.setRequestModeration(event.isRequestModeration());
        fullDto.setState(event.getState().toString());
        fullDto.setTitle(event.getTitle());
        fullDto.setViews(0);
        return fullDto;
    }

    public static EventShortDto mapToShortDto(Event event, int request) {
        EventShortDto shortDto = new EventShortDto();
        shortDto.setAnnotation(event.getAnnotation());
        shortDto.setCategory(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()));
        shortDto.setConfirmedRequests(request);
        shortDto.setEventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
        shortDto.setId(event.getId());
        shortDto.setInitiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()));
        shortDto.setPaid(event.isPaid());
        shortDto.setTitle(event.getTitle());
        shortDto.setViews(0);
        return shortDto;
    }

    public static Event mapUpdateEvent(Event event, UpdateEventUserRequest updateEvent) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(
                    updateEvent.getEventDate(), DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
        }
        if (updateEvent.getLocation() != null) {
            event.setLon(updateEvent.getLocation().getLon());
            event.setLat(updateEvent.getLocation().getLat());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        return event;
    }

    public static Event mapUpdateEventByAdmin(Event event, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(
                    updateEvent.getEventDate(), DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
        }
        if (updateEvent.getLocation() != null) {
            event.setLon(updateEvent.getLocation().getLon());
            event.setLat(updateEvent.getLocation().getLat());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        return event;
    }

}

