package ru.practicum.event.repository.qdsl;

import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.event.enam.EventState;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class EventFilterAdmin {
    private ArrayList<User> users;
    private ArrayList<EventState> states;
    private ArrayList<Category> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
