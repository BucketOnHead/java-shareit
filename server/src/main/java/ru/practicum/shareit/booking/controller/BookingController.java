package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.logger.BookingControllerLoggerHelper;
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
        BookingControllerLoggerHelper.addBooking(log, bookingDto, userId);
        return bookingService.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam Boolean approved,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        BookingControllerLoggerHelper.updateBookingStatus(log, bookingId, approved, userId);
        return bookingService.updateBookingStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(
            @PathVariable Long bookingId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        BookingControllerLoggerHelper.getBookingDtoById(log, bookingId, userId);
        return bookingService.getBookingByIdOnlyForOwnerOrBooker(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllByBookerId(
            @RequestParam String state,
            @RequestParam Integer from,
            @RequestParam Integer size,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        BookingControllerLoggerHelper.getBookingByBookerIdDtoPage(log, userId, state, from, size);
        return bookingService.getBookingPageByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsForUserItems(
            @RequestParam String state,
            @RequestParam Integer from,
            @RequestParam Integer size,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        BookingControllerLoggerHelper.getBookingDtoPageForUserItems(log, userId, state, from, size);
        return bookingService.getBookingsForUserItems(userId, state, from, size);
    }
}
