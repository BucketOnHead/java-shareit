package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.commondto.user.request.UserCreationDto;
import ru.practicum.shareit.commondto.user.response.UserDto;
import ru.practicum.shareit.commondto.validation.Groups;
import ru.practicum.shareit.user.client.UserClient;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public UserDto addUser(
            @RequestBody @Validated(Groups.OnCreate.class) UserCreationDto userDto
    ) {
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(
            @RequestBody @Validated(Groups.OnUpdate.class) UserCreationDto userDto,
            @PathVariable Long userId
    ) {
        return userClient.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(
            @PathVariable Long userId
    ) {
        return userClient.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userClient.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(
            @PathVariable Long userId
    ) {
        userClient.deleteUserById(userId);
    }
}
