package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IncorrectDataExceptionTest {
    /**
     * Method under test: {@link IncorrectDataException#IncorrectDataException(String)}
     */
    @Test
    void testConstructor() {
        IncorrectDataException actualIncorrectDataException = new IncorrectDataException("An error occurred");
        assertNull(actualIncorrectDataException.getCause());
        assertEquals(0, actualIncorrectDataException.getSuppressed().length);
        assertEquals("An error occurred", actualIncorrectDataException.getMessage());
        assertEquals("An error occurred", actualIncorrectDataException.getLocalizedMessage());
    }
}

