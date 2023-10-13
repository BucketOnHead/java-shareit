package ru.practicum.shareit.item.exception;

public class ItemNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -2405233709787706440L;

    public ItemNotFoundException(Long itemId) {
        super(String.format("Item not found with id: %d", itemId));
    }
}
