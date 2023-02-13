package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;
import ru.practicum.request.RequestState;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    Collection<Request> findAllByRequester(User user);

    Collection<Request> findAllByEventAndStatus(Event event, RequestState requestState);

    Collection<Request> findAllByEvent(Event event);

}
