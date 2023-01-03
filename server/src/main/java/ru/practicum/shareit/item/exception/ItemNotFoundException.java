package ru.practicum.shareit.item.exception;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class ItemNotFoundException extends EntityNotFoundException {
    private static final String ITEM_NOT_FOUND;
    private static final String ITEM_WITH_OWNER_NOT_FOUND;

    static {
        ITEM_NOT_FOUND = "ITEM[ID_%d] not found";
        ITEM_WITH_OWNER_NOT_FOUND = "ITEM[ID_%d] with owner-USER[ID_%d] not found";
    }

    public ItemNotFoundException(String message) {
        super(message);
    }

    public static ItemNotFoundException getFromItemId(Long itemId) {
        String message = String.format(ITEM_NOT_FOUND, itemId);
        return new ItemNotFoundException(message);
    }

    public static ItemNotFoundException getFromItemIdAndUserId(Long itemId, Long ownerId) {
        String message = String.format(ITEM_WITH_OWNER_NOT_FOUND, itemId, ownerId);
        return new ItemNotFoundException(message);
    }
}
