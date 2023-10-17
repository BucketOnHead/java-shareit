package ru.practicum.shareit.gateway.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.client.user.UserClient;
import ru.practicum.shareit.server.dto.user.request.UserCreationDto;
import ru.practicum.shareit.server.dto.user.response.UserDto;
import ru.practicum.shareit.server.dto.validation.Groups;
import ru.practicum.shareit.gateway.consts.DefaultParams;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
    public List<UserDto> getAllUsers(
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size
    ) {
        return userClient.getAllUsers(from, size);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(
            @PathVariable Long userId
    ) {
        userClient.deleteUserById(userId);
    }
}
