package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.commondto.booking.request.BookingCreationDto;
import ru.practicum.shareit.commondto.booking.response.BookingDto;
import ru.practicum.shareit.commons.constants.HttpHeadersConstants;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(
            @RequestBody BookingCreationDto bookingDto,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Adding booking to item with id: {} from user with id: {}", bookingDto.getItemId(), userId);
        log.info("Adding booking from user with id: {}, {}", userId, bookingDto);

        return bookingService.addBooking(bookingDto, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(
            @PathVariable Long bookingId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Getting booking with id: {} for user with id: {}", bookingId, userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByBookerId(
            @RequestParam String state,
            @RequestParam Integer from,
            @RequestParam Integer size,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Getting bookings page with from: {} and size: {}, with state: {} for user with id: {}", from, size,
                state, userId);
        return bookingService.getBookingsByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerId(
            @RequestParam String state,
            @RequestParam Integer from,
            @RequestParam Integer size,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Getting bookings page with from: {} and size: {}, with state: {} for user items with id: {}", from,
                size, state, userId);
        return bookingService.getBookingsByOwnerId(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveOrRejectBooking(
            @PathVariable Long bookingId,
            @RequestParam Boolean approved,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Updating booking status with id: {} from user with id: {}, approved: {}", userId, bookingId,
                approved);
        return bookingService.approveOrRejectBooking(bookingId, userId, approved);
    }
}
