package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EntityNotFoundExceptionTest {
    /**
     * Method under test: {@link EntityNotFoundException#EntityNotFoundException(String)}
     */
    @Test
    void testConstructor() {
        EntityNotFoundException actualEntityNotFoundException = new EntityNotFoundException("An error occurred");
        assertNull(actualEntityNotFoundException.getCause());
        assertEquals(0, actualEntityNotFoundException.getSuppressed().length);
        assertEquals("An error occurred", actualEntityNotFoundException.getMessage());
        assertEquals("An error occurred", actualEntityNotFoundException.getLocalizedMessage());
    }
}
