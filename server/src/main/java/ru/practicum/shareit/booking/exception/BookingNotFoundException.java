package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class BookingNotFoundException extends EntityNotFoundException {
    private static final String BOOKING_NOT_FOUND;
    private static final String BOOKING_WITH_OWNER_OR_BOOKER_NOT_FOUND;

    static {
        BOOKING_NOT_FOUND = "BOOKING[ID_%d] not found";
        BOOKING_WITH_OWNER_OR_BOOKER_NOT_FOUND = "BOOKING[ID_%d] with owner/booker-USER[ID_%d] not found";
    }

    public BookingNotFoundException(String message) {
        super(message);
    }

    public static BookingNotFoundException getFromBookingId(Long bookingId) {
        String message = String.format(BOOKING_NOT_FOUND, bookingId);
        return new BookingNotFoundException(message);
    }

    public static BookingNotFoundException getFromBookingIdAndUserId(Long bookingId, Long userId) {
        String message = String.format(BOOKING_WITH_OWNER_OR_BOOKER_NOT_FOUND, bookingId, userId);
        return new BookingNotFoundException(message);
    }
}
