package ru.practicum.shareit.server.user.service;

import ru.practicum.shareit.server.dto.user.request.UserCreationDto;
import ru.practicum.shareit.server.dto.user.response.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserCreationDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getUsers(Integer from, Integer size);

    UserDto updateUser(UserCreationDto userDto, Long userId);

    void deleteUserById(Long userId);
}
