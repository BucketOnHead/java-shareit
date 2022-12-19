package ru.practicum.shareit.user.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserNotFoundExceptionTest {
    /**
     * Method under test: {@link UserNotFoundException#UserNotFoundException(String)}
     */
    @Test
    void testConstructor() {
        // test parameters
        final String message = "exception message";

        UserNotFoundException ex = new UserNotFoundException(message);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message, ex.getMessage());
        assertEquals(message, ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link UserNotFoundException#getFromUserId(Long)}
     */
    @Test
    void testGetFromUserId() {
        // test parameters
        final Long userId = 1L;
        // test context
        final String message = String.format("USER[ID_%d] not found", userId);

        UserNotFoundException ex = UserNotFoundException.getFromUserId(userId);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message, ex.getMessage());
        assertEquals(message, ex.getLocalizedMessage());
    }
}

