package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.request.UserRequestDto;

import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponseDto addUser(
            @RequestBody UserRequestDto userDto) {
        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(
            @RequestBody UserRequestDto userDto,
            @PathVariable Long userId) {
        return userService.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUserById(
            @PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(
            @PathVariable Long userId) {
        userService.deleteUserById(userId);
    }
}
