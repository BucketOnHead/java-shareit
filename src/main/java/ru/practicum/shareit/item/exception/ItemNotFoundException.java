package ru.practicum.shareit.item.exception;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class ItemNotFoundException extends EntityNotFoundException {
    private static final String ITEM_NOT_FOUND_MESSAGE = "Item with ID_%d not found";

    public ItemNotFoundException(Long itemId) {
        super(String.format(ITEM_NOT_FOUND_MESSAGE, itemId));
    }
}
