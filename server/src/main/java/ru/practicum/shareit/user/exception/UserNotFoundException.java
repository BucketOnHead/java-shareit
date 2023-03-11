package ru.practicum.shareit.user.exception;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    private static final String USER_NOT_FOUND = "USER[ID_%d] not found";

    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException getFromUserId(Long userId) {
        String message = String.format(USER_NOT_FOUND, userId);
        return new UserNotFoundException(message);
    }
}
