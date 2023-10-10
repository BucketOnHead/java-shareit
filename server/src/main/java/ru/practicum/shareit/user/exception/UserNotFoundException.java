package ru.practicum.shareit.user.exception;

import lombok.NonNull;
import ru.practicum.shareit.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException byId(@NonNull String userId) {
        String message = String.format("User not fount with id: %s", userId);
        return new UserNotFoundException(message);
    }
}
