package ru.practicum.shareit.server.booking.exception;

public class BookingNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4729513982714859242L;

    public BookingNotFoundException(Long bookingId) {
        super(String.format("Booking with id: %d not found", bookingId));
    }
}
