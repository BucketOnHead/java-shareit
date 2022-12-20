package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LogicExceptionTest {
    /**
     * Method under test: {@link LogicException#LogicException(String)}
     */
    @Test
    void testConstructor() {
        LogicException actualLogicException = new LogicException("An error occurred");
        assertNull(actualLogicException.getCause());
        assertEquals(0, actualLogicException.getSuppressed().length);
        assertEquals("An error occurred", actualLogicException.getMessage());
        assertEquals("An error occurred", actualLogicException.getLocalizedMessage());
    }
}
