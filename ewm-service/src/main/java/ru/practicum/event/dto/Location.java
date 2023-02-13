package ru.practicum.event.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Location {
    private Float lat;
    private Float lon;
}
