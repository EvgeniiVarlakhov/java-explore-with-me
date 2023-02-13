package ru.practicum.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@Validated
public class RequestController {
    private final RequestService service;

    @Autowired
    public RequestController(RequestService service) {
        this.service = service;
    }

    @GetMapping("/users/{userId}/requests")
    public Collection<ParticipationRequestDto> getUsersRequests(
            @PathVariable int userId) {
        return service.getUsersRequests(userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> getRequestsForUsersEvent(
            @PathVariable int userId,
            @PathVariable int eventId) {
        return service.getRequestForUsersEvent(userId, eventId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createdRequest(
            @PathVariable int userId,
            @RequestParam(name = "eventId") @Positive int eventId) {
        return service.createRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto updateUsersRequest(
            @PathVariable int userId,
            @PathVariable int requestId) {
        return service.updateUsersRequest(userId, requestId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult getUpdateRequestByEventsOwner(
            @PathVariable int userId,
            @PathVariable int eventId,
            @Validated @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return service.getUpdateRequestByEventsOwner(userId, eventId, eventRequestStatusUpdateRequest);
    }

}
