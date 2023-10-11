package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.constants.HttpHeadersConstants;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto addBooking(
            @RequestBody BookItemRequestDto bookingDto,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Adding booking from user with id: {}", userId);
        log.info("Adding booking from user with id: {}, {}", userId, bookingDto);

        return bookingService.addBooking(bookingDto, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(
            @PathVariable Long bookingId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Getting booking with id: {} for user with id: {}", bookingId, userId);

        return bookingService.getBookingByIdOnlyForOwnerOrBooker(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookingsByBookerId(
            @RequestParam String state,
            @RequestParam Integer from,
            @RequestParam Integer size,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Getting bookings page(from: {}, size: {}) with state: {} for user with id: {}", from, size, state,
                userId);

        return bookingService.getBookingPageByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsForUserItems(
            @RequestParam String state,
            @RequestParam Integer from,
            @RequestParam Integer size,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Getting bookings page(from: {}, size: {}) with state: {} for user items with id: {}", from, size,
                state, userId);

        return bookingService.getBookingsForUserItems(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveOrRejectBooking(
            @PathVariable Long bookingId,
            @RequestParam Boolean approved,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Updating booking status with id: {}, from user with id: {}, approved: {}", userId, bookingId,
                approved);

        return bookingService.updateBookingStatus(bookingId, approved, userId);
    }
}
