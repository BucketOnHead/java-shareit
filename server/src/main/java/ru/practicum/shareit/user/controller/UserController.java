package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.request.UserRequestDto;

import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.logger.UserControllerLoggerHelper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponseDto addUser(
            @RequestBody UserRequestDto userDto
    ) {
        UserControllerLoggerHelper.addUser(log, userDto);
        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(
            @RequestBody UserRequestDto userDto,
            @PathVariable Long userId
    ) {
        UserControllerLoggerHelper.updateUser(log, userDto, userId);
        return userService.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUserById(
            @PathVariable Long userId
    ) {
        UserControllerLoggerHelper.getUserDtoById(log, userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserResponseDto> getUsers(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        UserControllerLoggerHelper.getUserDtoPage(log, from, size);
        return userService.getUsers(from, size);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(
            @PathVariable Long userId
    ) {
        UserControllerLoggerHelper.deleteUserById(log, userId);
        userService.deleteUserById(userId);
    }
}
