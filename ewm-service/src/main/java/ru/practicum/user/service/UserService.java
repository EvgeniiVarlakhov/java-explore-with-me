package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

import java.util.ArrayList;
import java.util.Collection;

public interface UserService {

    Collection<UserDto> getAllUsers(ArrayList<Integer> idUsers, Integer from, Integer size);

    UserDto createUser(UserDto userDto);

    void deleteUser(int userId);

}
