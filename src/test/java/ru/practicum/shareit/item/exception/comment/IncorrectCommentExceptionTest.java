package ru.practicum.shareit.item.exception.comment;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IncorrectCommentExceptionTest {
    /**
     * Method under test: {@link IncorrectCommentException#IncorrectCommentException(String)}
     */
    @Test
    void testConstructor() {
        // test parameters
        final String message = "Exception message";

        IncorrectCommentException ex = new IncorrectCommentException(message);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link IncorrectCommentException#getFromItemIdAndUserIdAndTime(Long, Long, LocalDateTime)}
     */
    @Test
    void testGetFromItemIdAndUserIdAndTime() {
        // test parameters
        final Long          itemId = 1L;
        final Long          userId = 10L;
        final LocalDateTime time   = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        // test context
        final String message = String.format(
                "BOOKING of ITEM[ID_%d] for USER[ID_%d] not found at moment %s",
                itemId, userId, time);

        IncorrectCommentException ex =
                IncorrectCommentException.getFromItemIdAndUserIdAndTime(userId, itemId, time);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }
}

