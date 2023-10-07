package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.logger.BookingServiceLoggerHelper;
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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingDtoMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponseDto addBooking(BookItemRequestDto bookingDto, Long userId) {
        userRepository.validateUserExistsById(userId);
        itemRepository.validateItemExistsById(bookingDto.getItemId());
        validateUserNotOwnerByItemIdAndUserId(bookingDto.getItemId(), userId);

        Booking booking = getBooking(bookingDto, userId);
        validateItemAvailableForBooking(booking.getItem());

        Booking savedBooking = bookingRepository.save(booking);
        BookingServiceLoggerHelper.bookingSaved(log, savedBooking);
        return bookingMapper.mapToBookingResponseDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingResponseDto updateBookingStatus(Long bookingId, Boolean approved, Long userId) {
        bookingRepository.validateBookingExistsById(bookingId);
        userRepository.validateUserExistsById(userId);

        Booking booking = bookingRepository.getReferenceById(bookingId);
        itemRepository.validateUserIdIsItemOwner(booking.getItem().getId(), userId);
        validateBookingStatusNotApprove(booking);

        booking.setStatus((approved == Boolean.TRUE) ? (Status.APPROVED) : (Status.REJECTED));

        Booking updatedBooking = bookingRepository.save(booking);
        BookingServiceLoggerHelper.bookingUpdated(log, updatedBooking);
        return bookingMapper.mapToBookingResponseDto(updatedBooking);
    }

    @Override
    public BookingResponseDto getBookingByIdOnlyForOwnerOrBooker(Long bookingId, Long userId) {
        bookingRepository.validateBookingExistsById(bookingId);
        userRepository.validateUserExistsById(userId);

        Booking booking = bookingRepository.getReferenceById(bookingId);
        validateUserIsOwnerOrBooker(booking, userId);

        BookingResponseDto bookingDto = bookingMapper.mapToBookingResponseDto(booking);
        BookingServiceLoggerHelper.bookingReturned(log, bookingDto, userId);
        return bookingDto;
    }

    @Override
    public List<BookingResponseDto> getBookingPageByBookerId(Long bookerId, String stateStr,
                                                             Integer from, Integer size) {
        userRepository.validateUserExistsById(bookerId);
        State state = State.valueOf(stateStr);

        Page<Booking> bookingsByBookerId = getBookingsByBookerIdAndState(bookerId, state, from, size);
        List<BookingResponseDto> bookingDtos = bookingMapper.mapToBookingResponseDto(bookingsByBookerId);

        BookingServiceLoggerHelper.bookingByBookerIdPageReturned(log, from, size, bookingDtos, stateStr, bookerId);
        return bookingDtos;
    }

    @Override
    public List<BookingResponseDto> getBookingsForUserItems(Long ownerId, String stateStr,
                                                            Integer from, Integer size) {
        userRepository.validateUserExistsById(ownerId);
        State state = State.valueOf(stateStr);

        Page<Booking> bookingsByBookerItems = getBookingsForUserItemsByState(ownerId, state, from, size);
        List<BookingResponseDto> bookingDtos = bookingMapper.mapToBookingResponseDto(bookingsByBookerItems);

        BookingServiceLoggerHelper.bookingPageForUserItemsReturned(log, from, size, stateStr, bookingDtos, ownerId);
        return bookingDtos;
    }

    private static void validateBookingStatusNotApprove(Booking booking) {
        if (booking.getStatus() == Status.APPROVED) {
            throw BookingAlreadyApprovedException.getFromBookingId(booking.getId());
        }
    }

    private static void validateItemAvailableForBooking(Item item) {
        if (item.getIsAvailable() == Boolean.FALSE) {
            throw ItemNotAvailableForBookingException.getFromItemId(item.getId());
        }
    }

    private static void validateUserIsOwnerOrBooker(Booking booking, Long userId) {
        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();

        boolean isOwner = ownerId.equals(userId);
        boolean isBooker = bookerId.equals(userId);

        if (!(isOwner || isBooker)) {
            throw BookingNotFoundException.getFromBookingIdAndUserId(booking.getId(), userId);
        }
    }

    private void validateUserNotOwnerByItemIdAndUserId(Long itemId, Long userId) {
        Long ownerId = itemRepository.getReferenceById(itemId).getOwner().getId();
        if (ownerId.equals(userId)) {
            throw BookingLogicException.getFromOwnerIdAndItemId(ownerId, itemId);
        }
    }

    private Booking getBooking(BookItemRequestDto bookingDto, Long userId) {
        User booker = userRepository.getReferenceById(userId);
        Item item = itemRepository.getReferenceById(bookingDto.getItemId());
        return bookingMapper.mapToBooking(bookingDto, booker, item);
    }

    private Page<Booking> getBookingsByBookerIdAndState(Long bookerId, State state, Integer from, Integer size) {
        LocalDateTime time = LocalDateTime.now();
        Pageable page = PageRequest.of(from / size, size);

        Page<Booking> bookingsByState;
        switch (state) {
            case ALL:
                bookingsByState = getAllBookingsByBookerId(bookerId, page);
                break;
            case CURRENT:
                bookingsByState = getCurrentBookingsByBookerId(bookerId, time, page);
                break;
            case PAST:
                bookingsByState = getPastBookingsByBookerId(bookerId, time, page);
                break;
            case FUTURE:
                bookingsByState = getFutureBookingsByBookerId(bookerId, time, page);
                break;
            case WAITING:
                bookingsByState = getBookingsByBookerIdAndStatus(bookerId, Status.WAITING, page);
                break;
            case REJECTED:
                bookingsByState = getBookingsByBookerIdAndStatus(bookerId, Status.REJECTED, page);
                break;
            default:
                throw StateNotImplementedException.fromState(state);
        }

        return bookingsByState;
    }

    private Page<Booking> getBookingsForUserItemsByState(Long ownerId, State state, Integer from, Integer size) {
        LocalDateTime time = LocalDateTime.now();
        Pageable page = PageRequest.of(from / size, size);

        Page<Booking> bookingsForUserItemsByState;
        switch (state) {
            case ALL:
                bookingsForUserItemsByState = getAllBookingsForUserItems(ownerId, page);
                break;
            case CURRENT:
                bookingsForUserItemsByState = getCurrentBookingsForUserItems(ownerId, time, page);
                break;
            case PAST:
                bookingsForUserItemsByState = getPastBookingsForUserItems(ownerId, time, page);
                break;
            case FUTURE:
                bookingsForUserItemsByState = getFutureBookingsForUserItems(ownerId, time, page);
                break;
            case WAITING:
                bookingsForUserItemsByState = getBookingsForUserItemsAndStatus(ownerId, Status.WAITING, page);
                break;
            case REJECTED:
                bookingsForUserItemsByState = getBookingsForUserItemsAndStatus(ownerId, Status.REJECTED, page);
                break;
            default:
                throw StateNotImplementedException.fromState(state);
        }

        return bookingsForUserItemsByState;
    }

    private Page<Booking> getAllBookingsByBookerId(Long bookerId, Pageable page) {
        return bookingRepository.findAllByBookerIdOrderByStartTimeDesc(
                bookerId,
                page);
    }

    private Page<Booking> getCurrentBookingsByBookerId(Long bookerId, LocalDateTime time, Pageable page) {
        return bookingRepository.findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                bookerId,
                time,
                time,
                page);
    }

    private Page<Booking> getPastBookingsByBookerId(Long bookerId, LocalDateTime time, Pageable page) {
        return bookingRepository.findAllByBookerIdAndEndTimeIsBefore(
                bookerId,
                time,
                page);
    }

    private Page<Booking> getFutureBookingsByBookerId(Long bookerId, LocalDateTime time, Pageable page) {
        return bookingRepository.findAllByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                bookerId,
                time,
                page);
    }

    private Page<Booking> getBookingsByBookerIdAndStatus(Long bookerId, Status status, Pageable page) {
        return bookingRepository.findAllByBookerIdAndStatusEquals(
                bookerId,
                status,
                page);
    }

    private Page<Booking> getAllBookingsForUserItems(Long ownerId, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdOrderByStartTimeDesc(
                ownerId,
                page);
    }

    private Page<Booking> getCurrentBookingsForUserItems(Long ownerId, LocalDateTime time, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                ownerId,
                time,
                time,
                page);
    }

    private Page<Booking> getPastBookingsForUserItems(Long ownerId, LocalDateTime time, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndEndTimeIsBefore(
                ownerId,
                time,
                page);
    }

    private Page<Booking> getFutureBookingsForUserItems(Long ownerId, LocalDateTime time, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                ownerId,
                time,
                page);
    }

    private Page<Booking> getBookingsForUserItemsAndStatus(Long ownerId, Status status, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndStatusEquals(
                ownerId,
                status,
                page);
    }
}
