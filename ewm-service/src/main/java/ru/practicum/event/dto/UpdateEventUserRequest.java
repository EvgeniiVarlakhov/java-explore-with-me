package ru.practicum.event.dto;

import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "Длина кр.описания должна быть от 20 до 2000.")
    private String annotation;

    @Positive
    private Integer category;

    @Size(min = 20, max = 7000, message = "Длина полного описания должда быть от 20 до 7000.")
    private String description;

    private String eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private String stateAction;

    @Size(min = 3, max = 120, message = "Длина заголовка от 3 до 120.")
    private String title;
}
