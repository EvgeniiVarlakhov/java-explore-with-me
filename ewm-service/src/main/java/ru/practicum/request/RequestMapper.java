package ru.practicum.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.constant.MainConstant;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static Request mapToNewRequest(User user, Event event) {
        Request request = new Request();
        request.setRequester(user);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        return request;
    }

    public static ParticipationRequestDto mapToDto(Request request) {
        return new ParticipationRequestDto(
                request.getCreated().format(MainConstant.FORMATTER),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatus().toString()
        );
    }

}
