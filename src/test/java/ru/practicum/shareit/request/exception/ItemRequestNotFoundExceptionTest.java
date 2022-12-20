package ru.practicum.shareit.request.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemRequestNotFoundExceptionTest {
    /**
     * Method under test: {@link ItemRequestNotFoundException#ItemRequestNotFoundException(String)}
     */
    @Test
    void testConstructor() {
        // test parameters
        final String message = "exception message";

        ItemRequestNotFoundException ex = new ItemRequestNotFoundException(message);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message, ex.getMessage());
        assertEquals(message, ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link ItemRequestNotFoundException#getFromId(Long)}
     */
    @Test
    void testGetFromId() {
        // test parameters
        final Long itemRequestId = 1L;
        // test context
        final String message = String.format("ITEM_REQUEST[ID_%d] not found", itemRequestId);

        ItemRequestNotFoundException ex = ItemRequestNotFoundException.getFromId(itemRequestId);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message, ex.getMessage());
        assertEquals(message, ex.getLocalizedMessage());
    }
}
