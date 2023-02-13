package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.user.UserMapper;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getAllUsers(ArrayList<Integer> idUsers, Integer from, Integer size) {
        Collection<UserDto> userDtos = new ArrayList<>();
        Collection<User> users;
        Pageable pageable = PageRequest.of(from / size, size);
        if (idUsers != null) {
            users = userRepository.findUsersByIds(idUsers, pageable).getContent();
        } else {
            users = userRepository.findAllUsers(pageable).getContent();
        }
        for (User user : users) {
            userDtos.add(UserMapper.toUserDto(user));
        }
        return userDtos;
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        User newSaveUser = userRepository.save(UserMapper.mapToNewUser(userDto));
        log.info("Создан новый пользователь: {}", newSaveUser);
        return UserMapper.toUserDto(newSaveUser);
    }

    @Transactional
    @Override
    public void deleteUser(int userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        if (userFromDb.isEmpty()) {
            throw new ObjectNotFoundException("Пользователя с ID = " + userId + " не существует.");
        }
        userRepository.deleteById(userId);
        log.info("Пользователь ID = {} успешно удален.", userId);
    }

}
