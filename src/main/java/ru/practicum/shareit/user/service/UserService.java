package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.in.RequestUserDto;
import ru.practicum.shareit.user.dto.out.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(RequestUserDto userDto);

    UserDto updateUser(RequestUserDto userDto, Long userId);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUserById(Long userId);
}
