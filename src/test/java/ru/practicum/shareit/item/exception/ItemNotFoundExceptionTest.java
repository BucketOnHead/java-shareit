package ru.practicum.shareit.item.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemNotFoundExceptionTest {
    /**
     * Method under test: {@link ItemNotFoundException#ItemNotFoundException(String)}
     */
    @Test
    void testConstructor() {
        // test parameters
        final String message = "Exception message";

        ItemNotFoundException ex = new ItemNotFoundException(message);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link ItemNotFoundException#getFromItemId(Long)}
     */
    @Test
    void testGetFromItemId() {
        // test parameters
        final Long itemId = 1L;
        // test context
        final String message = String.format("ITEM[ID_%d] not found", itemId);

        ItemNotFoundException ex = ItemNotFoundException.getFromItemId(itemId);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link ItemNotFoundException#getFromItemIdAndUserId(Long, Long)}
     */
    @Test
    void testGetFromItemIdAndUserId() {
        // test parameters
        final Long itemId  = 1L;
        final Long ownerId = 10L;
        // test context
        final String message = String.format(
                "ITEM[ID_%d] with owner-USER[ID_%d] not found",
                itemId, ownerId);

        ItemNotFoundException ex =
                ItemNotFoundException.getFromItemIdAndUserId(itemId, ownerId);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }
}
