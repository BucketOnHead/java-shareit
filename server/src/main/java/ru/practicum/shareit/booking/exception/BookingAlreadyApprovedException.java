package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.exception.IncorrectDataException;

public class BookingAlreadyApprovedException extends IncorrectDataException {
    private static final String BOOKING_ALREADY_APPROVED = "BOOKING[ID_%d] already approved";

    public BookingAlreadyApprovedException(String message) {
        super(message);
    }

    public static BookingAlreadyApprovedException getFromBookingId(Long bookingId) {
        String message = String.format(BOOKING_ALREADY_APPROVED, bookingId);
        return new BookingAlreadyApprovedException(message);
    }
}
