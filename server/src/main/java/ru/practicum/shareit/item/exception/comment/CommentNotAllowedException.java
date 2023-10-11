package ru.practicum.shareit.item.exception.comment;

import lombok.NonNull;
import ru.practicum.shareit.exception.IncorrectDataException;

public class CommentNotAllowedException extends IncorrectDataException {

    public CommentNotAllowedException(@NonNull Long itemId, @NonNull Long userId) {
        super(String.format("Unable to leave comment for item with id: %d: " +
                "no approved booking for user with id: %d or booking is not yet finished", itemId, userId));
    }
}
