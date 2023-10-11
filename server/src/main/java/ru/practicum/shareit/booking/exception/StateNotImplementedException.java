package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.booking.service.BookingService.State;

public class StateNotImplementedException extends RuntimeException {
    private static final long serialVersionUID = -1102722510929195659L;

    public StateNotImplementedException(State state) {
        super(String.format("State '%s' not implemented", state));
    }
}
