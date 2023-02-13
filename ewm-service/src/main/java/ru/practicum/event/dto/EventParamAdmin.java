package ru.practicum.event.dto;

import lombok.*;

import java.util.ArrayList;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class EventParamAdmin {
    private ArrayList<Integer> usersId;
    private ArrayList<String> states;
    private ArrayList<Integer> categoriesId;
    private String rangeStart;
    private String rangeEnd;
}
