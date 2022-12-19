package ru.practicum.shareit.booking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookingNotFoundExceptionTest {
    /**
     * Method under test: {@link BookingNotFoundException#BookingNotFoundException(String)}
     */
    @Test
    void testConstructor() {
        // test parameters
        final String message = "Exception message";

        BookingNotFoundException ex = new BookingNotFoundException(message);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link BookingNotFoundException#getFromBookingId(Long)}
     */
    @Test
    void testGetFromBookingId() {
        // test parameters
        final Long bookingId = 1L;
        // test context
        final String message = String.format("BOOKING[ID_%d] not found", bookingId);

        BookingNotFoundException ex =
                BookingNotFoundException.getFromBookingId(bookingId);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link BookingNotFoundException#getFromBookingIdAndUserId(Long, Long)}
     */
    @Test
    void testGetFromBookingIdAndUserId() {
        // test parameters
        final Long bookingId = 1L;
        final Long userId    = 10L;
        // test context
        final String message = String.format(
                "BOOKING[ID_%d] with owner/booker-USER[ID_%d] not found",
                bookingId, userId);

        BookingNotFoundException ex =
                BookingNotFoundException.getFromBookingIdAndUserId(bookingId, userId);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }
}
