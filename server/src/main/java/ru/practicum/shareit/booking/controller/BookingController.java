package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(
            @RequestBody BookItemRequestDto bookingDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        return bookingService.updateBookingStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        return bookingService.getBookingByIdOnlyForOwnerOrBooker(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBookerId(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam String state,
            @RequestParam Integer from,
            @RequestParam Integer size) {
        return bookingService.getAllByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByBookerItems(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam String state,
            @RequestParam Integer from,
            @RequestParam Integer size) {
        return bookingService.getAllByBookerItems(userId, state, from, size);
    }
}
