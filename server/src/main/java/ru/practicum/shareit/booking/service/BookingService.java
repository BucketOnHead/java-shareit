package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.request.BookingCreationDto;
import ru.practicum.shareit.booking.dto.response.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingCreationDto bookingDto, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getBookingsByBookerId(Long bookerId, String state, Integer from, Integer size);

    List<BookingDto> getBookingsByOwnerId(Long ownerId, String state, Integer from, Integer size);

    BookingDto approveOrRejectBooking(Long bookingId, Long userId, Boolean approved);
}
