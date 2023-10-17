package ru.practicum.shareit.server.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.booking.utils.BookingUtils;
import ru.practicum.shareit.server.dto.booking.request.BookingCreationDto;
import ru.practicum.shareit.server.dto.booking.response.BookingDto;
import ru.practicum.shareit.server.constants.booking.BookingState;
import ru.practicum.shareit.server.constants.booking.BookingStatus;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.item.utils.ItemUtils;
import ru.practicum.shareit.server.booking.exception.*;
import ru.practicum.shareit.server.user.repository.UserRepository;

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
            log.trace("User with id: {} is not owner of item with id: {}", userId, item.getId());
            throw new SelfBookingAttemptException(userId, item.getId());
        }
        if (ItemUtils.isUnavailable(item)) {
            log.trace("Item with id: {} is not available for booking", item.getId());
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
    public BookingDto getBookingById(Long bookingId, Long userId) {
        userRepository.existsByIdOrThrow(userId);
        var booking = bookingRepository.findByIdOrThrow(bookingId);

        var item = booking.getItem();
        if (ItemUtils.isNotOwner(item, userId) && BookingUtils.isNotBooker(booking, userId)) {
            log.trace("User with id: {} is not owner of item with id: {} or the booker", userId, item.getId());
            throw new BookingAccessException(booking.getId(), userId);
        }

        var bookingDto = bookingMapper.mapToBookingDto(booking);

        log.info("Booking with id: {} returned for user with id: {}", booking.getId(), userId);
        log.debug("Booking returned for user with id: {}, {}", userId, bookingDto);

        return bookingDto;
    }

    @Override
    public List<BookingDto> getBookingsByBookerId(Long bookerId, String stateStr, Integer from, Integer size) {
        userRepository.existsByIdOrThrow(bookerId);
        var state = BookingState.valueOf(stateStr);

        var bookings = getBookingsByBookerIdAndState(bookerId, state, from, size);
        var bookingsDto = bookingMapper.mapToBookingDto(bookings);

        log.info("Bookings page: from: {} and size: {}, with state: {} for booker with id: {} returned, count: {}",
                from, size, state, bookerId, bookingsDto.size());
        log.debug("Booking page returned for booker with id: {}, {}", bookerId, bookingsDto);

        return bookingsDto;
    }

    @Override
    public List<BookingDto> getBookingsByOwnerId(Long ownerId, String stateStr,
                                                 Integer from, Integer size) {
        userRepository.existsByIdOrThrow(ownerId);

        var state = BookingState.valueOf(stateStr);
        var bookings = getBookingsForUserItemsByState(ownerId, state, from, size);
        var bookingsDto = bookingMapper.mapToBookingDto(bookings);

        log.info("Bookings page with from: {} and size: {}, with state: {} for owner with id: {} returned, count: {}",
                from, size, state, ownerId, bookingsDto.size());
        log.debug("Booking page returned for booker with id: {}, {}", ownerId, bookingsDto);

        return bookingsDto;
    }

    @Override
    @Transactional
    public BookingDto approveOrRejectBooking(Long bookingId, Long userId, Boolean approved) {
        userRepository.existsByIdOrThrow(userId);

        var booking = bookingRepository.findByIdOrThrow(bookingId);
        if (BookingUtils.isNotWaiting(booking)) {
            log.trace("Booking with id: {} is not waiting approval", booking.getId());
            throw new BookingNotAwaitingApprovalException(bookingId);
        }

        var item = booking.getItem();
        if (ItemUtils.isNotOwner(item, userId)) {
            log.trace("User with id: {} is not owner of item with id: {}", userId, item.getId());
            throw new BookingAccessException(booking.getId(), item.getId());
        }

        var updatedBooking = updateBooking(booking, approved);
        var savedBooking = bookingRepository.save(updatedBooking);

        log.info("Booking with id: {} updated", booking.getId());
        log.debug("Booking updated: {}", booking);

        return bookingMapper.mapToBookingDto(savedBooking);
    }

    private Booking updateBooking(Booking booking, Boolean approved) {
        var status = (approved == Boolean.TRUE) ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(status);

        log.trace("Booking status updated: {}", booking);
        return booking;
    }

    private Page<Booking> getBookingsByBookerIdAndState(Long bookerId, BookingState state, Integer from, Integer size) {
        var now = LocalDateTime.now();
        var sort = Sort.by(Direction.DESC, "end");
        var page = PageRequest.of(from / size, size, sort);

        Page<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(bookerId, page);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(bookerId, now, now, page);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(bookerId, now, page);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(bookerId, now, page);
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
        var sort = Sort.by(Direction.DESC, "end");
        var page = PageRequest.of(from / size, size, sort);

        Page<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemOwnerId(ownerId, page);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(ownerId, now, now, page);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBefore(ownerId, now, page);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartIsAfter(ownerId, now, page);
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
