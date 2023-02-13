package ru.practicum.event.dto;

import lombok.*;

import javax.validation.constraints.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class NewEventDto {

    @NotNull
    @Size(min = 20, max = 2000, message = "Длина кр.описания должна быть от 20 до 2000.")
    private String annotation;

    @NotNull
    @Positive
    private Integer category;

    @NotNull
    @Size(min = 20, max = 7000, message = "Длина полного описания должда быть от 20 до 7000.")
    private String description;

    @NotNull
    private String eventDate;

    @NotNull
    private Location location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    @NotNull
    private Boolean requestModeration;

    @NotNull
    @Size(min = 3, max = 120, message = "Длина заголовка от 3 до 120.")
    private String title;
}
