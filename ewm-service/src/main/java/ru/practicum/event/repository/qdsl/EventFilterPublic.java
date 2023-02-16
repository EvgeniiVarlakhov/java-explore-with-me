package ru.practicum.event.repository.qdsl;

import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.event.enam.EventState;

import java.time.LocalDateTime;
import java.util.ArrayList;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class EventFilterPublic {
    private String text;
    private ArrayList<Category> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private EventState eventState = EventState.PUBLISHED;
    private Boolean available;
}
