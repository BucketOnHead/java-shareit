package ru.practicum.shareit.booking.exception;

public class BookingNotAwaitingApprovalException extends RuntimeException {
    private static final long serialVersionUID = -6311965066081938485L;

    public BookingNotAwaitingApprovalException(Long bookingId) {
        super(String.format("Booking with id: %d not awaiting approval", bookingId));
    }
}
