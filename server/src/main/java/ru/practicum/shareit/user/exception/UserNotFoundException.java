package ru.practicum.shareit.user.exception;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -2546797154273459251L;

    public UserNotFoundException(Long userId) {
        super(String.format("User with id: %d not found", userId));
    }
}
