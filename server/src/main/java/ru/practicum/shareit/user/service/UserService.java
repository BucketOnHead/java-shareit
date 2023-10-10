package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.request.UserCreationDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto addUser(UserCreationDto userDto);

    UserResponseDto updateUser(UserCreationDto userDto, Long userId);

    UserResponseDto getUserById(Long userId);

    List<UserResponseDto> getUsers(Integer from, Integer size);

    void deleteUserById(Long userId);
}
