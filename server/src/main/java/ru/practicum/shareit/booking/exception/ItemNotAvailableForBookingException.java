package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.exception.IncorrectDataException;

public class ItemNotAvailableForBookingException extends IncorrectDataException {
    private static final String ITEM_NOT_AVAILABLE = "ITEM[ID_%d] is not available for booking";

    public ItemNotAvailableForBookingException(String message) {
        super(message);
    }

    public static ItemNotAvailableForBookingException getFromItemId(Long itemId) {
        String message = String.format(ITEM_NOT_AVAILABLE, itemId);
        return new ItemNotAvailableForBookingException(message);
    }
}
