package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class BookingNotFoundException extends EntityNotFoundException {
    private static final String BOOKING_NOT_FOUND = "BOOKING[ID_%d] not found";
    private static final String BOOKING_WITH_USER_NOT_FOUND = "BOOKING[ID_%d] with USER[ID_%d] not found";

    public BookingNotFoundException(String message) {
        super(message);
    }

    public static BookingNotFoundException getFromBookingId(Long bookingId) {
        String message = String.format(BOOKING_NOT_FOUND, bookingId);
        return new BookingNotFoundException(message);
    }

    public static BookingNotFoundException getFromBookingIdAndUserId(Long bookingId, Long userId) {
        String message = String.format(BOOKING_WITH_USER_NOT_FOUND, bookingId, userId);
        return new BookingNotFoundException(message);
    }
}
