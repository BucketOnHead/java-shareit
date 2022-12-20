package ru.practicum.shareit.booking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookingAlreadyApprovedExceptionTest {
    /**
     * Method under test: {@link BookingAlreadyApprovedException#BookingAlreadyApprovedException(String)}
     */
    @Test
    void testConstructor() {
        // test parameters
        final String message = "Exception message";

        BookingAlreadyApprovedException ex = new BookingAlreadyApprovedException(message);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link BookingAlreadyApprovedException#getFromBookingId(Long)}
     */
    @Test
    void testGetFromBookingId() {
        // test parameters
        final Long bookingId = 1L;
        // test context
        final String message = String.format("BOOKING[ID_%d] already approved", bookingId);

        BookingAlreadyApprovedException ex =
                BookingAlreadyApprovedException.getFromBookingId(bookingId);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }
}
