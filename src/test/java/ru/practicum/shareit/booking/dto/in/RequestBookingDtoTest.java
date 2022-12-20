package ru.practicum.shareit.booking.dto.in;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class RequestBookingDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link RequestBookingDto}
     *   <li>{@link RequestBookingDto#setEnd(LocalDateTime)}
     *   <li>{@link RequestBookingDto#setItemId(Long)}
     *   <li>{@link RequestBookingDto#setStart(LocalDateTime)}
     *   <li>{@link RequestBookingDto#getEnd()}
     *   <li>{@link RequestBookingDto#getItemId()}
     *   <li>{@link RequestBookingDto#getStart()}
     * </ul>
     */
    @Test
    void testConstructor() {
        RequestBookingDto actualRequestBookingDto = new RequestBookingDto();
        LocalDateTime ofResult = LocalDateTime.of(1, 1, 1, 1, 1);
        actualRequestBookingDto.setEnd(ofResult);
        actualRequestBookingDto.setItemId(123L);
        LocalDateTime ofResult1 = LocalDateTime.of(1, 1, 1, 1, 1);
        actualRequestBookingDto.setStart(ofResult1);
        assertSame(ofResult, actualRequestBookingDto.getEnd());
        assertEquals(123L, actualRequestBookingDto.getItemId().longValue());
        assertSame(ofResult1, actualRequestBookingDto.getStart());
    }
}
