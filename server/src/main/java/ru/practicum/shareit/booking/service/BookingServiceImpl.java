package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw BookingAlreadyApprovedException.getFromBookingId(booking.getId());
        }
    }

    private static void validateItemAvailableForBooking(Item item) {
        if (item.getIsAvailable() == Boolean.FALSE) {
            throw ItemNotAvailableForBookingException.getFromItemId(item.getId());
        }
    }

    private static void validateUserIsOwnerOrBooker(Booking booking, Long userId) {
        Long bookerId = booking.getBooker().getId();

        boolean isOwner = booking.getItem().isOwner(userId);
        boolean isBooker = bookerId.equals(userId);

        if (!isOwner && !isBooker) {
            throw BookingNotFoundException.getFromBookingIdAndUserId(booking.getId(), userId);
        }
    }

    private void validateUserNotOwnerByItemIdAndUserId(Long itemId, Long userId) {
        var item = itemRepository.getReferenceById(itemId);
        if (item.isOwner(userId)) {
            throw BookingLogicException.getFromOwnerIdAndItemId(userId, itemId);
        }
    }

    private Booking getBooking(BookItemRequestDto bookingDto, Long userId) {
        User booker = userRepository.getReferenceById(userId);
        Item item = itemRepository.getReferenceById(bookingDto.getItemId());
        return bookingMapper.mapToBooking(bookingDto, booker, item);
    }

    private Page<Booking> getBookingsByBookerIdAndState(Long bookerId, State state, Integer from, Integer size) {
        LocalDateTime time = LocalDateTime.now();
        Pageable page = PageRequest.of(from / size, size, Sort.by(Direction.DESC, "endTime"));

        Page<Booking> bookingsByState;
        switch (state) {
            case ALL:
                bookingsByState = bookingRepository.findAllByBookerId(bookerId, page);
                break;
            case CURRENT:
                bookingsByState = bookingRepository.findAllByBookerIdAndStartTimeBeforeAndEndTimeAfter(
                        bookerId, time, time, page);
                break;
            case PAST:
                bookingsByState = bookingRepository.findAllByBookerIdAndEndTimeBefore(bookerId, time, page);
                break;
            case FUTURE:
                bookingsByState = bookingRepository.findAllByBookerIdAndStartTimeAfter(bookerId, time, page);
                break;
            case WAITING:
                bookingsByState = bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.WAITING, page);
                break;
            case REJECTED:
                bookingsByState = bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.REJECTED, page);
                break;
            default:
                throw StateNotImplementedException.fromState(state);
        }

        return bookingsByState;
    }

    private Page<Booking> getBookingsForUserItemsByState(Long ownerId, State state, Integer from, Integer size) {
        LocalDateTime time = LocalDateTime.now();
        Pageable page = PageRequest.of(from / size, size, Sort.by(Direction.DESC, "endTime"));

        Page<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemOwnerId(ownerId, page);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartTimeBeforeAndEndTimeAfter(
                        ownerId, time, time, page);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemOwnerIdAndEndTimeBefore(ownerId, time, page);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                        ownerId, time, page);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, Status.WAITING, page);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, Status.REJECTED, page);
                break;
            default:
                throw StateNotImplementedException.fromState(state);
        }

        return bookings;
    }
}
