package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.in.BookingCreationRequestDto;
import ru.practicum.shareit.booking.dto.out.BookingDto;

import java.util.List;

public interface BookingService {
    enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }

    BookingDto addBooking(BookingCreationRequestDto bookingDto, Long userId);

    BookingDto updateBookingStatus(Long bookingId, Boolean approved, Long userId);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getAllByBookerId(Long userId, String state);

    List<BookingDto> getAllByBookerItems(Long bookerId, String state);
}
