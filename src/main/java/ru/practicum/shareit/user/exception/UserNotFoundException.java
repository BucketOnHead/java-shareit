package ru.practicum.shareit.user.exception;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    private static final String USER_NOT_FOUND_MESSAGE = "User with ID_%d not found";

    public UserNotFoundException(Long userId) {
        super(String.format(USER_NOT_FOUND_MESSAGE, userId));
    }
}
