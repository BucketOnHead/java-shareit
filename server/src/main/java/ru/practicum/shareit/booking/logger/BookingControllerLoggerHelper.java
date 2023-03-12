package ru.practicum.shareit.booking.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;

@UtilityClass
public final class BookingControllerLoggerHelper {

    public static void addBooking(
            Logger log,
            BookItemRequestDto bookingDto,
            Long userId
    ) {
        log.info("add BOOKING["
                        + "item_id={}, "
                        + "booker_id={}"
                        + "].",
                bookingDto.getItemId(),
                userId);
    }

    public static void updateBookingStatus(
            Logger log,
            Long bookingId,
            Boolean approved,
            Long userId
    ) {
        log.info("update BOOKING["
                        + "booking_id={}, "
                        + "approved='{}'"
                        + "] from USER["
                        + "id={}"
                        + "].",
                bookingId,
                approved,
                userId);
    }

    public static void getBookingDtoById(
            Logger log,
            Long bookingId,
            Long userId
    ) {
        log.info("get BOOKING["
                        + "id={}"
                        + "] for USER["
                        + "id={}"
                        + "].",
                bookingId,
                userId);
    }

    public static void getBookingByBookerIdDtoPage(
            Logger log,
            Long userId,
            String state,
            Integer from,
            Integer size
    ) {
        log.info("get BOOKING_PAGE<DTO>["
                        + "from={}, "
                        + "size={}, "
                        + "state='{}'"
                        + "] for USER["
                        + "id={}"
                        + "].",
                from,
                size,
                state,
                userId);
    }

    public static void getBookingDtoPageForUserItems(
            Logger log,
            Long userId,
            String state,
            Integer from,
            Integer size
    ) {
        log.info("get BOOKING_PAGE<DTO>["
                        + "from={}, "
                        + "size={}, "
                        + "state='{}'"
                        + "] for USER_ITEMS["
                        + "id={}"
                        + "].",
                from,
                size,
                state,
                userId);
    }
}
