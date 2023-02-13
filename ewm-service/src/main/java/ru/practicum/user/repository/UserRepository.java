package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * " +
            "from  users " +
            "order by id desc ", nativeQuery = true)
    Page<User> findAllUsers(Pageable pageable);

    @Query(value = "select * " +
            "from  users " +
            "where id in ?1 " +
            "order by id desc ", nativeQuery = true)
    Page<User> findUsersByIds(List<Integer> ips, Pageable pageable);

    @Query(value = "select * " +
            "from  users " +
            "where id in ?1 " +
            "order by id asc ", nativeQuery = true)
    ArrayList<User> findAll(ArrayList<Integer> userIds);

}
