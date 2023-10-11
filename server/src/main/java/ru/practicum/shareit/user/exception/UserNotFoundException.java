package ru.practicum.shareit.user.exception;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    private static final long serialVersionUID = -2546797154273459251L;

    public UserNotFoundException(Long userId) {
        super(String.format("User not found with id: %d", userId));
    }
}
