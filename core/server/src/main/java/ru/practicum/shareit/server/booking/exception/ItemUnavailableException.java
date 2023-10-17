package ru.practicum.shareit.server.booking.exception;

public class ItemUnavailableException extends RuntimeException {
    private static final long serialVersionUID = 8732166332276656666L;

    public ItemUnavailableException(Long itemId) {
        super(String.format("Item with id: %d not available for booking", itemId));
    }
}
