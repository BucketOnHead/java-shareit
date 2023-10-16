package ru.practicum.shareit.gateway.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.client.booking.BookingClient;
import ru.practicum.shareit.gateway.booking.validation.annotation.BookingStateEnum;
import ru.practicum.shareit.server.dto.booking.request.BookingCreationDto;
import ru.practicum.shareit.server.dto.booking.response.BookingDto;
import ru.practicum.shareit.server.dto.validation.Groups;
import ru.practicum.shareit.server.constants.HttpHeaderConstants;
import ru.practicum.shareit.gateway.consts.DefaultParams;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public BookingDto addBooking(
            @RequestBody @Validated(Groups.OnCreate.class) BookingCreationDto bookingDto,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam Boolean approved,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.updateBookingStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @PathVariable Long bookingId,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBookerId(
            @RequestParam(defaultValue = DefaultParams.STATE) @BookingStateEnum String state,
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.getAllByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByBookerItems(
            @RequestParam(defaultValue = DefaultParams.STATE) @BookingStateEnum String state,
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.getAllByBookerItems(userId, state, from, size);
    }
}
