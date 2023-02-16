package ru.practicum.user.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserShortDto {
    private Integer id;
    private String name;
}
