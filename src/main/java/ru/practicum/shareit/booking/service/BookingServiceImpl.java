package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.in.RequestBookingDto;
import ru.practicum.shareit.booking.dto.out.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static ru.practicum.shareit.booking.mapper.BookingDtoMapper.toBooking;
import static ru.practicum.shareit.item.service.ItemServiceImpl.checkItemExistsById;
import static ru.practicum.shareit.item.service.ItemServiceImpl.checkOwnerOfItemByItemIdAndUserId;
import static ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public static void checkBookingExistsById(BookingRepository bookingRepository, Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw BookingNotFoundException.getFromBookingId(bookingId);
        }
    }

    @Override
    @Transactional
    public BookingDto addBooking(RequestBookingDto bookingDto, Long userId) {
        checkUserExistsById(userRepository, userId);
        checkItemExistsById(itemRepository, bookingDto.getItemId());
        checkUserNotOwnerByItemIdAndUserId(bookingDto.getItemId(), userId);
        checkBookingTimePeriod(bookingDto.getStart(), bookingDto.getEnd());

        Booking booking = getBooking(bookingDto, userId);
        checkItemAvailableForBooking(booking.getItem());

        Booking savedBooking = bookingRepository.save(booking);
        log.debug("Booking ID_{} added.", savedBooking.getId());

        return BookingDtoMapper.toBookingDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingDto updateBookingStatus(Long bookingId, Boolean approved, Long userId) {
        checkBookingExistsById(bookingRepository, bookingId);
        checkUserExistsById(userRepository, userId);

        Booking booking = bookingRepository.getReferenceById(bookingId);
        checkOwnerOfItemByItemIdAndUserId(itemRepository, booking.getItem().getId(), userId);
        checkBookingStatusNotApprove(booking);

        booking.setStatus((approved == Boolean.TRUE) ? (Status.APPROVED) : (Status.REJECTED));

        Booking updatedBooking = bookingRepository.save(booking);
        log.debug("Booking ID_{} updated.", updatedBooking.getId());

        return BookingDtoMapper.toBookingDto(updatedBooking);
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        checkBookingExistsById(bookingRepository, bookingId);
        checkUserExistsById(userRepository, userId);

        Booking booking = bookingRepository.getReferenceById(bookingId);
        checkOwnerOrBooker(booking, userId);

        log.debug("Booking ID_{} returned.", booking.getId());
        return BookingDtoMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllByBookerId(Long bookerId, String possibleState) {
        checkUserExistsById(userRepository, bookerId);
        State state = checkState(possibleState);
        switch (state) {
            case ALL:
                return BookingDtoMapper.toBookingDto(getAllBookingsByBookerId(bookerId));
            case CURRENT:
                return BookingDtoMapper.toBookingDto(getCurrentBookingsByBookerId(bookerId));
            case PAST:
                return BookingDtoMapper.toBookingDto(getPastBookingsByBookerId(bookerId));
            case FUTURE:
                return BookingDtoMapper.toBookingDto(getFutureBookingsByBookerId(bookerId));
            case WAITING:
                return BookingDtoMapper.toBookingDto(getWaitingBookingsByBookerId(bookerId));
            case REJECTED:
                return BookingDtoMapper.toBookingDto(getRejectedBookingsByBookerId(bookerId));
            default:
                throw StateNotImplementedException.getFromState(state);
        }
    }

    @Override
    public List<BookingDto> getAllByBookerItems(Long ownerId, String possibleState) {
        checkUserExistsById(userRepository, ownerId);
        State state = checkState(possibleState);
        switch (state) {
            case ALL:
                return BookingDtoMapper.toBookingDto(getAllBookingsByOwnerId(ownerId));
            case CURRENT:
                return BookingDtoMapper.toBookingDto(getCurrentBookingsByOwnerId(ownerId));
            case PAST:
                return BookingDtoMapper.toBookingDto(getPastBookingsByOwnerId(ownerId));
            case FUTURE:
                return BookingDtoMapper.toBookingDto(getFutureBookingsByOwnerId(ownerId));
            case WAITING:
                return BookingDtoMapper.toBookingDto(getWaitingBookingsByOwnerId(ownerId));
            case REJECTED:
                return BookingDtoMapper.toBookingDto(getRejectedBookingsByOwnerId(ownerId));
            default:
                throw StateNotImplementedException.getFromState(state);
        }
    }

    private Booking getBooking(RequestBookingDto bookingDto, Long userId) {
        User booker = userRepository.getReferenceById(userId);
        Item item = itemRepository.getReferenceById(bookingDto.getItemId());
        return toBooking(bookingDto, booker, item);
    }

    private void checkUserNotOwnerByItemIdAndUserId(Long itemId, Long userId) {
        Long ownerId = itemRepository.getReferenceById(itemId).getOwner().getId();
        if (ownerId.equals(userId)) {
            throw BookingLogicException.getFromOwnerIdAndItemId(ownerId, itemId);
        }
    }

    private void checkOwnerOrBooker(Booking booking, Long userId) {
        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();

        boolean isOwner = ownerId.equals(userId);
        boolean isBooker = bookerId.equals(userId);

        if (!isOwner && !isBooker) {
            throw BookingNotFoundException.getFromBookingIdAndUserId(booking.getId(), userId);
        }
    }

    private void checkBookingTimePeriod(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw IncorrectBookingDatesException.getFromDates(start, end);
        }
    }

    private void checkBookingStatusNotApprove(Booking booking) {
        if (booking.getStatus() == Booking.Status.APPROVED) {
            throw BookingAlreadyApprovedException.getFromBookingId(booking.getId());
        }
    }

    private State checkState(String possibleState) {
        try {
            return State.valueOf(possibleState);
        } catch (IllegalArgumentException ex) {
            throw IncorrectStateException.getFromIncorrectState(possibleState);
        }
    }

    private void checkItemAvailableForBooking(Item item) {
        if (item.getIsAvailable() == Boolean.FALSE) {
            throw ItemNotAvailableForBookingException.getFromItemId(item.getId());
        }
    }

    private List<Booking> getAllBookingsByBookerId(Long bookerId) {
        return bookingRepository.findBookingByBookerIdOrderByStartTimeDesc(bookerId);
    }

    private List<Booking> getCurrentBookingsByBookerId(Long bookerId) {
        LocalDateTime time = now();
        return bookingRepository.findBookingByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(bookerId, time, time);
    }

    private List<Booking> getPastBookingsByBookerId(Long bookerId) {
        return bookingRepository.findBookingsByBookerIdAndEndTimeIsBefore(bookerId, now());
    }

    private List<Booking> getFutureBookingsByBookerId(Long bookerId) {
        return bookingRepository.findBookingsByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(bookerId, now());
    }

    private List<Booking> getWaitingBookingsByBookerId(Long bookerId) {
        return bookingRepository.findBookingsByBookerIdAndStatusEquals(bookerId, Status.WAITING);
    }

    private List<Booking> getRejectedBookingsByBookerId(Long bookerId) {
        return bookingRepository.findBookingsByBookerIdAndStatusEquals(bookerId, Status.REJECTED);
    }

    private List<Booking> getAllBookingsByOwnerId(Long ownerId) {
        return bookingRepository.findBookingsByItemOwnerIdOrderByStartTimeDesc(ownerId);
    }

    private List<Booking> getCurrentBookingsByOwnerId(Long ownerId) {
        LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findBookingsByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(ownerId, time, time);
    }

    private List<Booking> getPastBookingsByOwnerId(Long ownerId) {
        return bookingRepository.findBookingsByItemOwnerIdAndEndTimeIsBefore(ownerId, now());
    }

    private List<Booking> getFutureBookingsByOwnerId(Long ownerId) {
        return bookingRepository.findBookingsByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(ownerId, now());
    }

    private List<Booking> getWaitingBookingsByOwnerId(Long ownerId) {
        return bookingRepository.findBookingsByItemOwnerIdAndStatusEquals(ownerId, Status.WAITING);
    }

    private List<Booking> getRejectedBookingsByOwnerId(Long ownerId) {
        return bookingRepository.findBookingsByItemOwnerIdAndStatusEquals(ownerId, Status.REJECTED);
    }
}
