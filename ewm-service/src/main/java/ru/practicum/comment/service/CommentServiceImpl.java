package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.CommentMapper;
import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.InvalidMadeRequestException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository comRepository;

    @Override
    public Collection<CommentShortDto> getAllCommentsEventByPublic(int eventId, int from, int size) {
        validateEvent(eventId);
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<Comment> comments = comRepository.findAllByEventId(eventId, pageable).getContent();
        Collection<CommentShortDto> listDto = CommentMapper.mapToListShortDtos(comments);
        log.info("Получен список комментариев для события ID = {}. {}", eventId, listDto);
        return listDto;
    }

    @Override
    public Collection<CommentShortDto> getAllCommentsEventByUser(int userId, int eventId, int from, int size) {
        User author = validateUser(userId);
        validateEvent(eventId);
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<Comment> comments = comRepository.findAllByEventIdAndAuthor(eventId, author, pageable).getContent();
        Collection<CommentShortDto> listDto = CommentMapper.mapToListShortDtos(comments);
        log.info("Получен список комментариев для события ID = {}. {}", eventId, listDto);
        return listDto;
    }

    @Override
    public Collection<CommentFullDto> getAllCommentsEventByAdmin(int eventId, int from, int size) {
        validateEvent(eventId);
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<Comment> comments = comRepository.findAllByEventId(eventId, pageable).getContent();
        Collection<CommentFullDto> listDto = CommentMapper.mapToListFullDtos(comments);
        log.info("Получен список комментариев для события ID = {}. {}", eventId, listDto);
        return listDto;
    }

    @Override
    public CommentShortDto getCommentByIdEventForUser(int userId, int comId) {
        validateUser(userId);
        Optional<Comment> comment = comRepository.findById(comId);
        if (comment.isEmpty()) {
            throw new ObjectNotFoundException("Комментария с ID = " + comId + " нет.");
        }
        if (comment.get().getAuthor().getId() != userId) {
            throw new ObjectNotFoundException(
                    "У пользователя с ID = " + userId + " нет комментария с ID = " + comId + ".");
        }
        log.info("Получен комментарий: {}.", comment);
        return CommentMapper.mapToShortDto(comment.get());
    }

    @Override
    public CommentFullDto getCommentByIdEventForAdmin(int comId) {
        Optional<Comment> comment = comRepository.findById(comId);
        if (comment.isEmpty()) {
            throw new ObjectNotFoundException("Комментария с ID = " + comId + " нет.");
        }
        log.info("Получен комментарий: {}.", comment.get());
        return CommentMapper.mapToFullDto(comment.get());
    }

    @Transactional
    @Override
    public CommentShortDto createCommentByUser(int userId, int eventId, CommentDtoIn comDtoIn) {
        User user = validateUser(userId);
        validateEvent(eventId);
        Comment newComment = comRepository.save(CommentMapper.mapToEvent(comDtoIn, user, eventId));
        log.info("Создан новый комментарий пользователем ID = {} для события ID = {}. {}", userId, eventId, newComment);
        return CommentMapper.mapToShortDto(newComment);
    }

    @Transactional
    @Override
    public CommentShortDto updateCommentByUser(int userId, int comId, CommentDtoIn comDtoIn) {
        validateUser(userId);
        Optional<Comment> comment = comRepository.findById(comId);
        if (comment.isEmpty()) {
            throw new ObjectNotFoundException("Комментария с ID = " + comId + " нет.");
        }
        if (comment.get().getAuthor().getId() != userId) {
            throw new InvalidMadeRequestException(
                    "Пользователь с ID = " + userId + " не является автором комментария с ID = " + comId + ".");
        }
        comment.get().setText(comDtoIn.getText());
        Comment updateCom = comRepository.save(comment.get());
        log.info("Комментарий с ID = {} изменен автором. {}", comId, updateCom);
        return CommentMapper.mapToShortDto(updateCom);
    }

    @Transactional
    @Override
    public CommentFullDto updateCommentByAdmin(int comId, int comId1, CommentDtoIn comDtoIn) {
        Optional<Comment> comment = comRepository.findById(comId);
        if (comment.isEmpty()) {
            throw new ObjectNotFoundException("Комментария с ID = " + comId + " нет.");
        }
        comment.get().setText(comDtoIn.getText());
        Comment updateCom = comRepository.save(comment.get());
        log.info("Комментарий с ID = {} изменен администратором. {}", comId, updateCom);
        return CommentMapper.mapToFullDto(updateCom);
    }

    @Transactional
    @Override
    public void deleteCommentByUser(int userId, int comId) {
        validateUser(userId);
        Optional<Comment> comment = comRepository.findById(comId);
        if (comment.isEmpty()) {
            throw new ObjectNotFoundException("Комментария с ID = " + comId + " нет.");
        }
        if (comment.get().getAuthor().getId() != userId) {
            throw new InvalidMadeRequestException(
                    "Пользователь с ID = " + userId + " не является автором комментария с ID = " + comId + ".");
        }
        comRepository.deleteById(comId);
        log.info("Комментарий с ID = {} удален Администратором.", comId);
    }

    @Transactional
    @Override
    public void deleteCommentByAdmin(int comId) {
        Optional<Comment> comment = comRepository.findById(comId);
        if (comment.isEmpty()) {
            throw new ObjectNotFoundException("Комментария с ID = " + comId + " нет.");
        }
        comRepository.deleteById(comId);
        log.info("Комментарий с ID = {} удален Администратором.", comId);
    }

    private User validateUser(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Пользователя с ID = " + userId + " не существует.");
        }
        return user.get();
    }

    private void validateEvent(int eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new ObjectNotFoundException("События с ID = " + eventId + " не существует.");
        }
    }

}
