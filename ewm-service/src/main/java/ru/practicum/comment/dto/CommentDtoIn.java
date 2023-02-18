package ru.practicum.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class CommentDtoIn {

    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;

}
