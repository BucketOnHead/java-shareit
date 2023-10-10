package ru.practicum.shareit.user.exception;

import lombok.NonNull;
import ru.practicum.shareit.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    private static final long serialVersionUID = -2546797154273459251L;

    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException byId(@NonNull Long userId) {
        String message = String.format("User not found with id: %s", userId);
        return new UserNotFoundException(message);
    }
}
