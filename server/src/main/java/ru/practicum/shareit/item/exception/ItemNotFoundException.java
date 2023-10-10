package ru.practicum.shareit.item.exception;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class ItemNotFoundException extends EntityNotFoundException {

    public ItemNotFoundException(String message) {
        super(message);
    }

    public static ItemNotFoundException byId(Long itemId) {
        var msg = String.format("Item not found with id: %s", itemId);
        return new ItemNotFoundException(msg);
    }

    public static ItemNotFoundException fromItemIdAndUserId(Long itemId, Long ownerId) {
        var msg = String.format("ITEM[ID_%d] with owner-USER[ID_%d] not found", itemId, ownerId);
        return new ItemNotFoundException(msg);
    }
}
