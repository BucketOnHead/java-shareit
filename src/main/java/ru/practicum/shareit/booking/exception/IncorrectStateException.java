package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.exception.IncorrectDataException;

public class IncorrectStateException extends IncorrectDataException {
    private static final String UNKNOWN_STATE;

    static {
        UNKNOWN_STATE = "Unknown state: %s";
    }

    public IncorrectStateException(String message) {
        super(message);
    }

    public static IncorrectStateException getFromStringState(String state) {
        String message = String.format(UNKNOWN_STATE, state);
        throw new IncorrectStateException(message);
    }
}
