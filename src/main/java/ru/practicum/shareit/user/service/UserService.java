package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.in.UserCreationRequestDto;
import ru.practicum.shareit.user.dto.in.UserUpdateRequestDto;
import ru.practicum.shareit.user.dto.out.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserCreationRequestDto userDto);

    UserDto updateUser(UserUpdateRequestDto userDto, Long userId);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUserById(Long userId);
}
