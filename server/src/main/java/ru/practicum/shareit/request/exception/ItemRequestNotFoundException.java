package ru.practicum.shareit.request.exception;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class ItemRequestNotFoundException extends EntityNotFoundException {
    private static final String ITEM_REQUEST_NOT_FOUND;

    static {
        ITEM_REQUEST_NOT_FOUND = "ITEM_REQUEST[ID_%d] not found";
    }

    public ItemRequestNotFoundException(String message) {
        super(message);
    }

    public static ItemRequestNotFoundException getFromId(Long itemRequestId) {
        String message = String.format(ITEM_REQUEST_NOT_FOUND, itemRequestId);
        return new ItemRequestNotFoundException(message);
    }
}
