package ru.practicum.shareit.server.constants.booking;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Стадии бронирования", enumAsRef = true)
public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED
}
