package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.commondto.user.request.UserCreationDto;
import ru.practicum.shareit.commondto.user.response.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(
            @RequestBody UserCreationDto userDto
    ) {
        log.info("Adding user");
        log.debug("Adding user: {}", userDto);

        return userService.addUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(
            @PathVariable Long userId
    ) {
        log.info("Getting user by id: {}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Getting users page with from: {} and size: {}", from, size);
        return userService.getUsers(from, size);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(
            @RequestBody UserCreationDto userDto,
            @PathVariable Long userId
    ) {
        log.info("Updating user with id: {}", userId);
        log.debug("Updating user with id: {}, {}", userId, userDto);

        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(
            @PathVariable Long userId
    ) {
        log.info("Deleting user by id: {}", userId);
        userService.deleteUserById(userId);
    }
}
