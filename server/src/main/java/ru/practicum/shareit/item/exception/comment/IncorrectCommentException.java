package ru.practicum.shareit.item.exception.comment;

import ru.practicum.shareit.exception.IncorrectDataException;

import java.time.LocalDateTime;

public class IncorrectCommentException extends IncorrectDataException {
    private static final String COMPLETED_BOOKING_NOT_FOUND;

    static {
        COMPLETED_BOOKING_NOT_FOUND = "BOOKING of ITEM[ID_%d] for USER[ID_%d] not found at moment %s";
    }

    public IncorrectCommentException(String message) {
        super(message);
    }

    public static IncorrectCommentException fromItemIdAndUserIdAndTime(
            Long itemId, Long userId, LocalDateTime time
    ) {
        String message = String.format(COMPLETED_BOOKING_NOT_FOUND, itemId, userId, time);
        return new IncorrectCommentException(message);
    }
}
