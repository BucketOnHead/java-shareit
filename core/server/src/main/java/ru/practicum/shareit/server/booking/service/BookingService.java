package ru.practicum.shareit.server.booking.service;

import ru.practicum.shareit.server.dto.booking.request.BookingCreationDto;
import ru.practicum.shareit.server.dto.booking.response.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingCreationDto bookingDto, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getBookingsByBookerId(Long bookerId, String state, Integer from, Integer size);

    List<BookingDto> getBookingsByOwnerId(Long ownerId, String state, Integer from, Integer size);

    BookingDto approveOrRejectBooking(Long bookingId, Long userId, Boolean approved);
}
