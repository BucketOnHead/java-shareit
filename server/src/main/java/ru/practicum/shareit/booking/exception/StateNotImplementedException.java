package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.booking.model.BookingState;

public class StateNotImplementedException extends RuntimeException {
    private static final long serialVersionUID = -1102722510929195659L;

    public StateNotImplementedException(BookingState state) {
        super(String.format("State '%s' not implemented", state));
    }
}
