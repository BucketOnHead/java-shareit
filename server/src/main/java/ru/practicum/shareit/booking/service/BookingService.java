package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingDto;
import ru.practicum.shareit.booking.exception.IncorrectStateException;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookItemRequestDto bookingDto, Long userId);

    BookingDto updateBookingStatus(Long bookingId, Boolean approved, Long userId);

    BookingDto getBookingByIdOnlyForOwnerOrBooker(Long bookingId, Long userId);

    List<BookingDto> getAllByBookerId(Long userId, String state, Integer from, Integer size);

    List<BookingDto> getAllByBookerItems(Long bookerId, String state, Integer from, Integer size);

    enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;
    }
}
