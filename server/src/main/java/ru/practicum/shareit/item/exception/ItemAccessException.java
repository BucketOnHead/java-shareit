package ru.practicum.shareit.item.exception;

public class ItemAccessException extends RuntimeException {
    private static final long serialVersionUID = -3867552143715665754L;

    public ItemAccessException(Long itemId, Long userId) {
        super(String.format("User with id: %d is not owner of item with id: %d", userId, itemId));
    }
}

