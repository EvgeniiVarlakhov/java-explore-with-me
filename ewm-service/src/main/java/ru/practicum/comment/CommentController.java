package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@Validated
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/events/{eventId}/comments")
    public Collection<CommentShortDto> getAllCommentsEventForPublic(
            @PathVariable int eventId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return commentService.getAllCommentsEventByPublic(eventId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}/comments")
    public Collection<CommentShortDto> getAllCommentsEventForUser(
            @PathVariable int userId,
            @PathVariable int eventId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return commentService.getAllCommentsEventByUser(userId, eventId, from, size);
    }

    @GetMapping("/admin/events/{eventId}/comments")
    public Collection<CommentFullDto> getAllCommentsEventForAdmin(
            @PathVariable int eventId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return commentService.getAllCommentsEventByAdmin(eventId, from, size);
    }

    @GetMapping("/users/{userId}/comments/{comId}")
    public CommentShortDto getCommentByIdEventForUser(
            @PathVariable int userId,
            @PathVariable int comId) {
        return commentService.getCommentByIdEventForUser(userId, comId);
    }

    @GetMapping("/admin/comments/{comId}")
    public CommentFullDto getCommentByIdEventForAdmin(
            @PathVariable int comId) {
        return commentService.getCommentByIdEventForAdmin(comId);
    }

    @PostMapping("/users/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentShortDto createComment(
            @PathVariable int userId,
            @RequestParam(name = "eventId") @Positive int eventId,
            @Validated @RequestBody CommentDtoIn comDtoIn) {
        return commentService.createCommentByUser(userId, eventId, comDtoIn);
    }

    @PatchMapping("/users/{userId}/comments/{comId}")
    public CommentShortDto updateCommentByUser(
            @PathVariable int userId,
            @PathVariable int comId,
            @Validated @RequestBody CommentDtoIn comDtoIn) {
        return commentService.updateCommentByUser(userId, comId, comDtoIn);
    }

    @PatchMapping("/admin/comments/{comId}")
    public CommentFullDto updateCommentByUser(
            @PathVariable int comId,
            @Validated @RequestBody CommentDtoIn comDtoIn) {
        return commentService.updateCommentByAdmin(comId, comId, comDtoIn);
    }

    @DeleteMapping("/users/{userId}/comments/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByUser(
            @PathVariable int userId,
            @PathVariable int comId) {
        commentService.deleteCommentByUser(userId, comId);
    }

    @DeleteMapping("/admin/comments/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(
            @PathVariable int comId) {
        commentService.deleteCommentByAdmin(comId);
    }

}
