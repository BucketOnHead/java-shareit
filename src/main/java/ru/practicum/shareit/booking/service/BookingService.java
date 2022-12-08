package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.in.BookingCreationRequestDto;
import ru.practicum.shareit.booking.dto.out.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }

    static void checkBookingTimePeriod(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw IncorrectBookingDatesException.getFromDates(start, end);
        }
    }

    static void checkBookingExistsById(BookingRepository bookingRepository, Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw BookingNotFoundException.getFromBookingId(bookingId);
        }
    }

    static void checkBookingStatusNotApprove(Booking booking) {
        if (booking.getStatus() == Booking.Status.APPROVED) {
            throw BookingAlreadyApprovedException.getFromBookingId(booking.getId());
        }
    }

    static State checkState(String possibleState) {
        try {
            return State.valueOf(possibleState);
        } catch (IllegalArgumentException ex) {
            throw IncorrectStateException.getFromIncorrectState(possibleState);
        }
    }

    static void checkItemAvailableForBooking(Item item) {
        if (item.getIsAvailable() == Boolean.FALSE) {
            throw ItemNotAvailableForBookingException.getFromItemId(item.getId());
        }
    }

    static void checkUserNotOwnerByItemIdAndUserId(ItemRepository itemRepository, Long itemId, Long userId) {
        Long ownerId = itemRepository.getReferenceById(itemId).getOwner().getId();
        if (ownerId.equals(userId)) {
            throw BookingLogicException.getFromOwnerIdAndItemId(ownerId, itemId);
        }
    }

    static void checkOwnerOrBooker(Booking booking, Long userId) {
        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();

        boolean isOwner = ownerId.equals(userId);
        boolean isBooker = bookerId.equals(userId);

        if (!isOwner && !isBooker) {
            throw BookingNotFoundException.getFromBookingIdAndUserId(booking.getId(), userId);
        }
    }

    BookingDto addBooking(BookingCreationRequestDto bookingDto, Long userId);

    BookingDto updateBookingStatus(Long bookingId, Boolean approved, Long userId);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getAllByBookerId(Long userId, String state);

    List<BookingDto> getAllByBookerItems(Long bookerId, String state);
}
