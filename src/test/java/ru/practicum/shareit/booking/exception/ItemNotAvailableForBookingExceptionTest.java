package ru.practicum.shareit.booking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemNotAvailableForBookingExceptionTest {
    /**
     * Method under test: {@link ItemNotAvailableForBookingException#ItemNotAvailableForBookingException(String)}
     */
    @Test
    void testConstructor() {
        // test parameters
        final String message = "Exception message";

        ItemNotAvailableForBookingException ex =
                new ItemNotAvailableForBookingException(message);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link ItemNotAvailableForBookingException#getFromItemId(Long)}
     */
    @Test
    void testGetFromItemId() {
        // test parameters
        final Long itemId = 1L;
        // test context
        final String message = String.format("ITEM[ID_%d] is not available for booking", itemId);

        ItemNotAvailableForBookingException ex =
                ItemNotAvailableForBookingException.getFromItemId(itemId);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }
}
