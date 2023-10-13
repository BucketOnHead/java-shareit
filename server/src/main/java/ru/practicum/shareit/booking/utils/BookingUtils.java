package ru.practicum.shareit.booking.utils;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

@UtilityClass
public class BookingUtils {

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
