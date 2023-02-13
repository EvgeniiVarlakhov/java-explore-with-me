package ru.practicum.request.service;

import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface RequestService {

    ParticipationRequestDto createRequest(int userId, int eventId);

    ParticipationRequestDto updateUsersRequest(int userId, int requestId);

    Collection<ParticipationRequestDto> getUsersRequests(int userId);

    Collection<ParticipationRequestDto> getRequestForUsersEvent(int userId, int eventId);

    EventRequestStatusUpdateResult getUpdateRequestByEventsOwner(
            int userId, int eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

}
