package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.request.BookingCreationDto;
import ru.practicum.shareit.booking.dto.response.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.utils.BookingUtils;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.utils.ItemUtils;
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
    public BookingDto addBooking(BookingCreationDto bookingDto, Long userId) {
        var item = itemRepository.findByIdOrThrow(bookingDto.getItemId());
        if (ItemUtils.isOwner(item, userId)) {
            throw new SelfBookingAttemptException(userId, item.getId());
        }
        if (ItemUtils.isUnavailable(item)) {
            throw new ItemUnavailableException(item.getId());
        }

        var booker = userRepository.findByIdOrThrow(userId);
        var booking = bookingMapper.mapToBooking(bookingDto, booker, item);
        var savedBooking = bookingRepository.save(booking);

        log.info("Booking with id: {} for item with id: {} from user with id: {} added", savedBooking.getId(),
                item.getId(), booker.getId());
        log.debug("Booking added: {}", savedBooking);

        return bookingMapper.mapToBookingDto(savedBooking);
    }

    @Override
    public BookingDto getBookingByIdOnlyForOwnerOrBooker(Long bookingId, Long userId) {
        userRepository.existsByIdOrThrow(userId);

        var booking = bookingRepository.findByIdOrThrow(bookingId);
        var item = booking.getItem();
        if (ItemUtils.isNotOwner(item, userId) && BookingUtils.isNotBooker(booking, userId)) {
            throw new BookingAccessException(booking.getId(), userId);
        }

        var bookingDto = bookingMapper.mapToBookingDto(booking);

        log.info("Booking with id: {} returned", booking.getId());
        log.debug("Booking returned: {}", bookingDto);

        return bookingDto;
    }

    @Override
    public List<BookingDto> getBookingPageByBookerId(Long bookerId, String stateStr, Integer from, Integer size) {
        userRepository.existsByIdOrThrow(bookerId);
        var state = BookingState.valueOf(stateStr);

        var bookings = getBookingsByBookerIdAndState(bookerId, state, from, size);
        var bookingsDto = bookingMapper.mapToBookingDto(bookings);

        log.info("Bookings page(from: {}, size: {}) with state: {} for booker with id: {} returned, count: {}", from,
                size, state, bookerId, bookingsDto.size());
        log.debug("Booking page returned for booker with id: {}, {}", bookerId, bookingsDto);

        return bookingsDto;
    }

    @Override
    public List<BookingDto> getBookingsForUserItems(Long ownerId, String stateStr,
                                                    Integer from, Integer size) {
        userRepository.existsByIdOrThrow(ownerId);
        var state = BookingState.valueOf(stateStr);

        var bookings = getBookingsForUserItemsByState(ownerId, state, from, size);
        var bookingsDto = bookingMapper.mapToBookingDto(bookings);

        log.info("Bookings page(from: {}, size: {}) with state: {} for owner with id: {} returned, count: {}", from,
                size, state, ownerId, bookingsDto.size());
        log.debug("Booking page returned for booker with id: {}, {}", ownerId, bookingsDto);

        return bookingsDto;
    }

    @Override
    @Transactional
    public BookingDto updateBookingStatus(Long bookingId, Boolean approved, Long userId) {
        userRepository.existsByIdOrThrow(userId);

        var booking = bookingRepository.findByIdOrThrow(bookingId);
        if (BookingUtils.isNotWaiting(booking)) {
            throw new BookingNotAwaitingApprovalException(bookingId);
        }

        var item = booking.getItem();
        if (ItemUtils.isNotOwner(item, userId)) {
            throw new BookingAccessException(booking.getId(), item.getId());
        }

        var updatedBooking = updateBooking(booking, approved);
        var savedBooking = bookingRepository.save(updatedBooking);

        log.info("Booking with id: {} updated", booking.getId());
        log.debug("Booking updated: {}", booking);

        return bookingMapper.mapToBookingDto(savedBooking);
    }

    private Booking updateBooking(Booking booking, Boolean approved) {
        booking.setStatus((approved == Boolean.TRUE) ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        log.trace("Booking status updated: {}", booking);

        return booking;
    }

    private Page<Booking> getBookingsByBookerIdAndState(Long bookerId, BookingState state, Integer from, Integer size) {
        var now = LocalDateTime.now();
        var sort = Sort.by(Direction.DESC, "endTime");
        var page = PageRequest.of(from / size, size, sort);

        Page<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(bookerId, page);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartTimeBeforeAndEndTimeAfter(bookerId, now, now,
                        page);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndTimeBefore(bookerId, now, page);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartTimeAfter(bookerId, now, page);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.WAITING, page);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.REJECTED, page);
                break;
            default:
                throw new StateNotImplementedException(state);
        }

        log.trace("Bookings page by state: {} for booker with id: {} returned, {}", state, bookerId, bookings);

        return bookings;
    }

    private Page<Booking> getBookingsForUserItemsByState(Long ownerId, BookingState state, Integer from, Integer size) {
        var now = LocalDateTime.now();
        var sort = Sort.by(Direction.DESC, "endTime");
        var page = PageRequest.of(from / size, size, sort);

        Page<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemOwnerId(ownerId, page);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartTimeBeforeAndEndTimeAfter(ownerId, now, now,
                        page);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemOwnerIdAndEndTimeBefore(ownerId, now, page);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(ownerId, now,
                        page);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.WAITING, page);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.REJECTED, page);
                break;
            default:
                throw new StateNotImplementedException(state);
        }

        log.trace("Bookings page by state: {} for owner_of_items with id: {} returned, {}", state, ownerId, bookings);

        return bookings;
    }
}
