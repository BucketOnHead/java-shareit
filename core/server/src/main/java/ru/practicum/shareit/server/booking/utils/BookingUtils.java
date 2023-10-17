package ru.practicum.shareit.server.booking.utils;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.constants.booking.BookingStatus;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@UtilityClass
public class BookingUtils {

    public Map<Long, Booking> toBookingByItemId(Iterable<Booking> lastBookings) {
        if (lastBookings == null) {
            return Collections.emptyMap();
        }

        return StreamSupport.stream(lastBookings.spliterator(), true)
                .filter(Objects::nonNull)
                .filter(b -> Objects.nonNull(b.getItem()))
                .filter(b -> Objects.nonNull(b.getItem().getId()))
                .collect(Collectors.toMap(b -> b.getItem().getId(), Function.identity()));
    }

    public boolean isNotWaiting(Booking booking) {
        if (booking == null) {
            return false;
        }

        return booking.getStatus() != BookingStatus.WAITING;
    }

    public boolean isNotBooker(Booking booking, Long userId) {
        if (booking == null || userId == null) {
            return false;
        }

        var booker = booking.getBooker();
        if (booker == null) {
            return false;
        }

        var bookerId = booker.getId();
        if (bookerId == null) {
            return false;
        }

        return !bookerId.equals(userId);
    }
}
