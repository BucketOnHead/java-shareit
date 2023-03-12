package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.exception.LogicException;

public class BookingLogicException extends LogicException {
    private static final String USER_CANT_BOOK_OWN_ITEM = "USER[ID_%d] cannot book own ITEM[ID_%d]";

    public BookingLogicException(String message) {
        super(message);
    }

    public static BookingLogicException getFromOwnerIdAndItemId(Long ownerId, Long itemId) {
        String message = String.format(USER_CANT_BOOK_OWN_ITEM, ownerId, itemId);
        return new BookingLogicException(message);
    }
}
