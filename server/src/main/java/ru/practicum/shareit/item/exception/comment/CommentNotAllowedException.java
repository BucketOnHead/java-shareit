package ru.practicum.shareit.item.exception.comment;

import lombok.NonNull;

public class CommentNotAllowedException extends RuntimeException {
    private static final long serialVersionUID = 7407587872848067002L;

    public CommentNotAllowedException(@NonNull Long itemId, @NonNull Long userId) {
        super(String.format("Unable to leave comment for item with id: %d: " +
                "no approved booking for user with id: %d or booking is not yet finished", itemId, userId));
    }
}
