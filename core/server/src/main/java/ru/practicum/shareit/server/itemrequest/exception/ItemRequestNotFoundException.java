package ru.practicum.shareit.server.itemrequest.exception;

public class ItemRequestNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 5833045847077036042L;

    public ItemRequestNotFoundException(Long requestId) {
        super(String.format("Item request with id: %d not found", requestId));
    }
}
