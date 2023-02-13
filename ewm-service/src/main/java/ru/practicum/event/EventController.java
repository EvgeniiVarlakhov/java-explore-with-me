package ru.practicum.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@Validated
@Slf4j
public class EventController {
    private final EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("/users/{userId}/events")
    public Collection<EventShortDto> getAllUsersEvents(
            @PathVariable int userId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return service.getAllUsersEvents(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getUsersEventById(
            @PathVariable int userId,
            @PathVariable int eventId) {
        return service.getUsersEventById(userId, eventId);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(
            @PathVariable int id, HttpServletRequest request) throws JsonProcessingException {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return service.getEventById(id, request);
    }

    @GetMapping("/admin/events")
    public Collection<EventFullDto> getAllEventByAdmin(
            @RequestParam(value = "users", required = false) ArrayList<Integer> users,
            @RequestParam(value = "states", required = false) ArrayList<String> states,
            @RequestParam(value = "categories", required = false) ArrayList<Integer> categories,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        EventParamAdmin param = new EventParamAdmin(users, states, categories, rangeStart, rangeEnd);
        return service.getAllEventsByAdmin(param, from, size);
    }

    @GetMapping("/events")
    public Collection<EventShortDto> getAllEventByPublic(
            HttpServletRequest request,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) ArrayList<Integer> categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        EventParamPublic param = new EventParamPublic(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        return service.getAllEventsByPub(param, from, size, request);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable int userId, @Validated @RequestBody NewEventDto newEventDto) {
        return service.createEvent(userId, newEventDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(
            @PathVariable int userId,
            @PathVariable int eventId,
            @Validated @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return service.updateEventByUser(userId, eventId, updateEventUserRequest);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(
            @PathVariable int eventId,
            @Validated @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return service.updateEventByAdmin(eventId, updateEventAdminRequest);
    }

}
