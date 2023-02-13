package ru.practicum.event.dto;

import lombok.*;

import java.util.ArrayList;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class EventParamPublic {
    private String text;
    private ArrayList<Integer> categoriesId;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;
    private Boolean onlyAvailable;
    private String sort;
}
