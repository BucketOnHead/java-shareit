package ru.practicum.shareit.booking.exception;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.service.BookingService.State;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StateNotImplementedExceptionTest {
    /**
     * Method under test: {@link StateNotImplementedException#StateNotImplementedException(String)}
     */
    @Test
    void testConstructor() {
        // test parameters
        final String message = "Exception message";

        StateNotImplementedException ex = new StateNotImplementedException(message);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }

    /**
     * Method under test: {@link StateNotImplementedException#getFromState(State)}
     */
    @Test
    void testGetFromState() {
        // test parameters
        final State state = State.FUTURE;
        // test context
        final String message = String.format("State '%s' not implemented", state);

        StateNotImplementedException ex =
                StateNotImplementedException.getFromState(state);

        assertNull(ex.getCause());
        assertEquals(0, ex.getSuppressed().length);
        assertEquals(message,    ex.getMessage());
        assertEquals(message,    ex.getLocalizedMessage());
    }
}
