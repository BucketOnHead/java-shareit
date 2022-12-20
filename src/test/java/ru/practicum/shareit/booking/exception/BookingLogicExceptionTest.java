package ru.practicum.shareit.booking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookingLogicExceptionTest {
    /**
     * Method under test: {@link BookingLogicException#BookingLogicException(String)}
     */
    @Test
    void testConstructor() {
        // test parameters
        final String message = "Exception message";

        BookingLogicException ex = new BookingLogicException(message);
        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link BookingLogicException#getFromOwnerIdAndItemId(Long, Long)}
     */
    @Test
    void testGetFromOwnerIdAndItemId() {
        // test parameters
        final Long ownerId = 1L;
        final Long itemId  = 10L;
        // test context
        final String message = String.format(
                "USER[ID_%d] cannot book own ITEM[ID_%d]",
                ownerId, itemId);

        BookingLogicException ex =
                BookingLogicException.getFromOwnerIdAndItemId(ownerId, itemId);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }
}
