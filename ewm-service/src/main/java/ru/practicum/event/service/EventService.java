package ru.practicum.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.practicum.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface EventService {

    EventFullDto createEvent(int userId, NewEventDto newEventDto);

    Collection<EventShortDto> getAllUsersEvents(int userId, int from, int size);

    EventFullDto getUsersEventById(int userId, int eventId);

    EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest);

    EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest);

    EventFullDto getEventById(int id, HttpServletRequest request) throws JsonProcessingException;

    Collection<EventFullDto> getAllEventsByAdmin(EventParamAdmin param, int from, int size);

    Collection<EventShortDto> getAllEventsByPub(EventParamPublic param, int from, int size, HttpServletRequest request);

}
