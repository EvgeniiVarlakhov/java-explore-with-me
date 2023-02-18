package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;

import java.util.Collection;

public interface CommentService {

    Collection<CommentShortDto> getAllCommentsEventByPublic(int eventId, int from, int size);

    CommentShortDto createCommentByUser(int userId, int eventId, CommentDtoIn comDtoIn);

    CommentShortDto updateCommentByUser(int userId, int comId, CommentDtoIn comDtoIn);

    void deleteCommentByUser(int userId, int comId);

    void deleteCommentByAdmin(int comId);

    CommentFullDto updateCommentByAdmin(int comId, int comId1, CommentDtoIn comDtoIn);

    Collection<CommentShortDto> getAllCommentsEventByUser(int userId, int eventId, int from, int size);

    Collection<CommentFullDto> getAllCommentsEventByAdmin(int eventId, int from, int size);

    CommentShortDto getCommentByIdEventForUser(int userId, int comId);

    CommentFullDto getCommentByIdEventForAdmin(int comId);

}

