package ru.practicum.shareit.booking.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.booking.dto.response.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@UtilityClass
public final class BookingServiceLoggerHelper {

    public static void bookingSaved(
            Logger log,
            Booking booking
    ) {
        log.debug("BOOKING["
                        + "id={}"
                        + "] saved.",
                booking.getId());
    }

    public static void bookingUpdated(
            Logger log,
            Booking booking
    ) {
        log.debug("BOOKING["
                        + "id={}"
                        + "] updated.",
                booking.getId());
    }

    public static void bookingReturned(
            Logger log,
            BookingDto bookingDto,
            Long userId
    ) {
        log.debug("BOOKING<DTO>["
                        + "id={}"
                        + "] returned for USER["
                        + "id={}"
                        + "].",
                bookingDto.getId(),
                userId);
    }

    public static void bookingByBookerIdPageReturned(
            Logger log,
            Integer from,
            Integer size,
            List<BookingDto> bookingDtos,
            String state,
            Long bookerId
    ) {
        log.debug("BOOKING_PAGE<DTO>["
                        + "from={}, "
                        + "size={}, "
                        + "state='{}', "
                        + "bookings_count={}"
                        + "] by USER["
                        + "id={}"
                        + "] returned.",
                from,
                size,
                state,
                bookingDtos.size(),
                bookerId);
    }

    public static void bookingPageForUserItemsReturned(
            Logger log,
            Integer from,
            Integer size,
            String state,
            List<BookingDto> bookingsByBookerItems,
            Long userId
    ) {
        log.debug("BOOKING_PAGE<DTO>["
                        + "from={}, "
                        + "size={}, "
                        + "state='{}', "
                        + "bookings_count={}"
                        + "] by USER_ITEMS["
                        + "id={}"
                        + "] returned.",
                from,
                size,
                state,
                bookingsByBookerItems.size(),
                userId);
    }
}
