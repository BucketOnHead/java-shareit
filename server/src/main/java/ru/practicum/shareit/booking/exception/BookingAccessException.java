package ru.practicum.shareit.booking.exception;

public class BookingAccessException extends RuntimeException {
    private static final long serialVersionUID = -5966341693850334272L;

    public BookingAccessException(Long bookingId, Long userId) {
        super(String.format("User with id: %d cannot request booking with id: %d", userId, bookingId));
    }
}
