package ru.practicum.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findAllByEventId(Integer eventId, Pageable pageable);

}
