package ru.practicum.comment.dto;

import lombok.*;
import ru.practicum.user.model.User;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class CommentFullDto {
    private int id;
    private String text;
    private User author;
    private int eventId;
    private String created;
}
