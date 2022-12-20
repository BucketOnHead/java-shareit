package ru.practicum.shareit.booking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IncorrectStateExceptionTest {
    /**
     * Method under test: {@link IncorrectStateException#IncorrectStateException(String)}
     */
    @Test
    void testConstructor() {
        // test parameters
        final String message = "Exception message";

        IncorrectStateException ex = new IncorrectStateException(message);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link IncorrectStateException#getFromString(String)}
     */
    @Test
    void testGetFromStringState() {
        // test parameters
        final String incorrectState = "Incorrect state";
        // test context
        final String message = String.format("Unknown state: %s", incorrectState);

        IncorrectStateException ex = IncorrectStateException.getFromString(incorrectState);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }
}
