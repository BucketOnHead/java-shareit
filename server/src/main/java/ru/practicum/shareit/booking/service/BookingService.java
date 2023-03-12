package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;

import java.util.List;

public interface BookingService {

    enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }

    BookingResponseDto addBooking(BookItemRequestDto bookingDto, Long userId);

    BookingResponseDto updateBookingStatus(Long bookingId, Boolean approved, Long userId);

    BookingResponseDto getBookingByIdOnlyForOwnerOrBooker(Long bookingId, Long userId);

    List<BookingResponseDto> getBookingPageByBookerId(Long userId, String state, Integer from, Integer size);

    List<BookingResponseDto> getBookingsForUserItems(Long bookerId, String state, Integer from, Integer size);
}
