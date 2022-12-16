package ru.practicum.shareit.booking.exception;

import org.apache.commons.lang3.NotImplementedException;
import ru.practicum.shareit.booking.service.BookingService.State;

public class StateNotImplementedException extends NotImplementedException {
    private static final String STATE_NOT_IMPLEMENTED;

    static {
        STATE_NOT_IMPLEMENTED = "STATE['%s'] not implemented";
    }

    public StateNotImplementedException(String message) {
        super(message);
    }

    public static StateNotImplementedException getFromState(State state) {
        String message = String.format(STATE_NOT_IMPLEMENTED, state);
        return new StateNotImplementedException(message);
    }
}
