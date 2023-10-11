package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class BookingNotFoundException extends EntityNotFoundException {
    private static final long serialVersionUID = 4729513982714859242L;

    public BookingNotFoundException(Long bookingId) {
        super(String.format("Booking with id: %d not found", bookingId));
    }
}
