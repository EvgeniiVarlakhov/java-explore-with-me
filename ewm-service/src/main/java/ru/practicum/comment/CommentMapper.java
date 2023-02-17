package ru.practicum.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.constant.MainConstant;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static Comment mapToEvent(CommentDtoIn comDto, User user, int eventId) {
        Comment comment = new Comment();
        comment.setText(comDto.getText());
        comment.setEventId(eventId);
        comment.setAuthor(user);
        comment.setCreatedTime(LocalDateTime.now());
        return comment;
    }

    public static CommentShortDto mapToShortDto(Comment comment) {
        return new CommentShortDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreatedTime().format(MainConstant.FORMATTER)
        );
    }

    public static CommentFullDto mapToFullDto(Comment comment) {
        return new CommentFullDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor(),
                comment.getEventId(),
                comment.getCreatedTime().format(MainConstant.FORMATTER)
        );
    }

    public static Collection<CommentShortDto> mapToListShortDtos(Collection<Comment> comments) {
        Collection<CommentShortDto> listDto = new ArrayList<>();
        for (Comment comment : comments) {
            listDto.add(mapToShortDto(comment));
        }
        return listDto;
    }

    public static Collection<CommentFullDto> mapToListFullDtos(Collection<Comment> comments) {
        Collection<CommentFullDto> listDto = new ArrayList<>();
        for (Comment comment : comments) {
            listDto.add(mapToFullDto(comment));
        }
        return listDto;
    }
}
