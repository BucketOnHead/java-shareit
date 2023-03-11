package ru.practicum.shareit.item.exception;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class ItemNotFoundException extends EntityNotFoundException {
    private static final String ITEM_NOT_FOUND_MESSAGE = "ITEM[ID_%d] not found";
    private static final String ITEM_WITH_OWNER_NOT_FOUND_MESSAGE = "ITEM[ID_%d] with owner-USER[ID_%d] not found";

    public ItemNotFoundException(String message) {
        super(message);
    }

    public static ItemNotFoundException fromItemId(Long itemId) {
        return new ItemNotFoundException(
                String.format(ITEM_NOT_FOUND_MESSAGE, itemId));
    }

    public static ItemNotFoundException fromItemIdAndUserId(Long itemId, Long ownerId) {
        return new ItemNotFoundException(
                String.format(ITEM_WITH_OWNER_NOT_FOUND_MESSAGE, itemId, ownerId));
    }
}
