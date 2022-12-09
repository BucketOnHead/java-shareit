package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.in.UserRequestDto;
import ru.practicum.shareit.user.dto.out.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserRequestDto userDto);

    UserDto updateUser(UserRequestDto userDto, Long userId);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUserById(Long userId);
}
