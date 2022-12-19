package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.in.RequestBookingDto;
import ru.practicum.shareit.booking.dto.out.BookingDto;
import ru.practicum.shareit.booking.exception.IncorrectStateException;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(RequestBookingDto bookingDto, Long userId);

    BookingDto updateBookingStatus(Long bookingId, Boolean approved, Long userId);

    BookingDto getBookingByIdOnlyForOwnerOrBooker(Long bookingId, Long userId);

    List<BookingDto> getAllByBookerId(Long userId, String state, Integer from, Integer size);

    List<BookingDto> getAllByBookerItems(Long bookerId, String state, Integer from, Integer size);

    enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

        public static State checkState(String possibleState) {
            try {
                return State.valueOf(possibleState);
            } catch (IllegalArgumentException ex) {
                throw IncorrectStateException.getFromString(possibleState);
            }
        }
    }
}
