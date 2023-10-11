package ru.practicum.shareit.booking.exception;

public class SelfBookingAttemptException extends RuntimeException {
    private static final long serialVersionUID = 6485884600583012994L;

    public SelfBookingAttemptException(Long ownerId, Long itemId) {
        super(String.format("User with id: %d cannot book their item with id: %d", ownerId, itemId));
    }
}
