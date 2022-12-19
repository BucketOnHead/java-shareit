package ru.practicum.shareit.exception.handler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ErrorResponseTest {
    /**
     * Method under test: {@link ErrorResponse#getFromException(Throwable)}
     */
    @Test
    void testGetFromException() {
        ErrorResponse actualFromException = ErrorResponse.getFromException(new Throwable());
        assertNull(actualFromException.getError());
        assertEquals("Throwable", actualFromException.getException());
    }

    /**
     * Method under test: {@link ErrorResponse#getFromExceptionAndMessage(Throwable, String)}
     */
    @Test
    void testGetFromExceptionAndMessage() {
        ErrorResponse actualFromExceptionAndMessage = ErrorResponse.getFromExceptionAndMessage(new Throwable(),
                "An error occurred");
        assertEquals("An error occurred", actualFromExceptionAndMessage.getError());
        assertEquals("Throwable", actualFromExceptionAndMessage.getException());
    }
}

