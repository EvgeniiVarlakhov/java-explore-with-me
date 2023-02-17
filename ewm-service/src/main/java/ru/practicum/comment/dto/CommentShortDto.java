package ru.practicum.comment.dto;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class CommentShortDto {
    private int id;
    private String text;
    private String authorName;
    private String created;
}
