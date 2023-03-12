package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.booking.service.BookingService.State;

public class StateNotImplementedException extends RuntimeException {
    private static final String STATE_NOT_IMPLEMENTED = "State '%s' not implemented";

    public StateNotImplementedException(String message) {
        super(message);
    }

    public static StateNotImplementedException getFromState(State state) {
        String message = String.format(STATE_NOT_IMPLEMENTED, state);
        return new StateNotImplementedException(message);
    }
}
