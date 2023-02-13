package ru.practicum.event.repository;

import com.querydsl.core.types.Predicate;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {

    @Query(value = "select * " +
            "from  events " +
            "where initiator_id = ?1 " +
            "order by id desc ", nativeQuery = true)
    Page<Event> findAllUsersEvents(Integer userId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Integer eventId, Integer userId);

    @Query(value = "select * " +
            "from  events " +
            "where id = ?1 " +
            "order by id asc ", nativeQuery = true)
    List<Event> findAllByIds(Set<Integer> ids);

    @Override
    @NotNull
    Page<Event> findAll(@NotNull Predicate predicate, @NotNull Pageable pageable);
}
