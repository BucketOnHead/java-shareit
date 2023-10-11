package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.request.BookingCreationDto;
import ru.practicum.shareit.booking.dto.response.BookingDto;

import java.util.List;

public interface BookingService {

    enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }

    BookingDto addBooking(BookingCreationDto bookingDto, Long userId);

    BookingDto updateBookingStatus(Long bookingId, Boolean approved, Long userId);

    BookingDto getBookingByIdOnlyForOwnerOrBooker(Long bookingId, Long userId);

    List<BookingDto> getBookingPageByBookerId(Long userId, String state, Integer from, Integer size);

    List<BookingDto> getBookingsForUserItems(Long bookerId, String state, Integer from, Integer size);
}
