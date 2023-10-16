package ru.practicum.shareit.user.service;

import ru.practicum.shareit.commondto.user.request.UserCreationDto;
import ru.practicum.shareit.commondto.user.response.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserCreationDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getUsers(Integer from, Integer size);

    UserDto updateUser(UserCreationDto userDto, Long userId);

    void deleteUserById(Long userId);
}
