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
    public BookingResponseDto addBooking(BookItemRequestDto bookingDto, Long userId) {
        checkUserExistsById(userRepository, userId);
        itemRepository.validateItemExistsById(bookingDto.getItemId());
        checkUserNotOwnerByItemIdAndUserId(bookingDto.getItemId(), userId);

        Booking booking = getBooking(bookingDto, userId);
        checkItemAvailableForBooking(booking.getItem());

        Booking savedBooking = bookingRepository.save(booking);
        log.debug("BOOKING[ID_{}] added.", savedBooking.getId());
        return BookingDtoMapper.toBookingDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingResponseDto updateBookingStatus(Long bookingId, Boolean approved, Long userId) {
        checkBookingExistsById(bookingRepository, bookingId);
        checkUserExistsById(userRepository, userId);

        Booking booking = bookingRepository.getReferenceById(bookingId);
        checkOwnerOfItemByItemIdAndUserId(itemRepository, booking.getItem().getId(), userId);
        checkBookingStatusNotApprove(booking);

        booking.setStatus((approved == Boolean.TRUE) ? (Status.APPROVED) : (Status.REJECTED));

        Booking updatedBooking = bookingRepository.save(booking);
        log.debug("BOOKING[ID_{}] updated.", updatedBooking.getId());
        return BookingDtoMapper.toBookingDto(updatedBooking);
    }

    @Override
    public BookingResponseDto getBookingByIdOnlyForOwnerOrBooker(Long bookingId, Long userId) {
        checkBookingExistsById(bookingRepository, bookingId);
        checkUserExistsById(userRepository, userId);

        Booking booking = bookingRepository.getReferenceById(bookingId);
        checkOwnerOrBooker(booking, userId);

        BookingResponseDto bookingDto = BookingDtoMapper.toBookingDto(booking);
        log.debug("BOOKING[ID_{}]<DTO> returned.", bookingDto.getId());
        return bookingDto;
    }

    @Override
    public List<BookingResponseDto> getAllByBookerId(Long bookerId, String stateStr,
                                                     Integer from, Integer size) {
        checkUserExistsById(userRepository, bookerId);
        State state = State.valueOf(stateStr);

        List<BookingResponseDto> bookingsByBookerId
                = getAllByBookerIdWithPagination(bookerId, state, from, size);

        log.debug("All BOOKING<DTO> by booker-USER[ID_{}] returned, {} in total.",
                bookerId, bookingsByBookerId.size());
        return bookingsByBookerId;
    }

    @Override
    public List<BookingResponseDto> getAllByBookerItems(Long ownerId, String stateStr,
                                                        Integer from, Integer size) {
        checkUserExistsById(userRepository, ownerId);
        State state = State.valueOf(stateStr);

        List<BookingResponseDto> bookingsByBookerItems
                = getAllByBookerItemsWithPagination(ownerId, state, from, size);

        log.debug("All BOOKING<DTO> by booker-USER[ID_{}] items returned, {} in total.",
                ownerId, bookingsByBookerItems.size());
        return bookingsByBookerItems;
    }

    private static void checkBookingStatusNotApprove(Booking booking) {
        if (booking.getStatus() == Status.APPROVED) {
            throw BookingAlreadyApprovedException.getFromBookingId(booking.getId());
        }
    }

    private static void checkItemAvailableForBooking(Item item) {
        if (item.getIsAvailable() == Boolean.FALSE) {
            throw ItemNotAvailableForBookingException.getFromItemId(item.getId());
        }
    }

    private static void checkOwnerOrBooker(Booking booking, Long userId) {
        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();

        boolean isOwner = ownerId.equals(userId);
        boolean isBooker = bookerId.equals(userId);

        if (!isOwner && !isBooker) {
            throw BookingNotFoundException.getFromBookingIdAndUserId(booking.getId(), userId);
        }
    }

    private void checkUserNotOwnerByItemIdAndUserId(Long itemId, Long userId) {
        Long ownerId = itemRepository.getReferenceById(itemId).getOwner().getId();
        if (ownerId.equals(userId)) {
            throw BookingLogicException.getFromOwnerIdAndItemId(ownerId, itemId);
        }
    }

    private Booking getBooking(BookItemRequestDto bookingDto, Long userId) {
        User booker = userRepository.getReferenceById(userId);
        Item item = itemRepository.getReferenceById(bookingDto.getItemId());
        return BookingDtoMapper.toBooking(bookingDto, booker, item);
    }

    private List<BookingResponseDto> getAllByBookerIdWithPagination(Long bookerId, State state,
                                                                    Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        switch (state) {
            case ALL:
                return BookingDtoMapper.toBookingDto(
                        getAllBookingsByBookerId(bookerId, page));
            case CURRENT:
                return BookingDtoMapper.toBookingDto(
                        getCurrentBookingsByBookerIdWithCurrentTime(bookerId, page));
            case PAST:
                return BookingDtoMapper.toBookingDto(
                        getPastBookingsByBookerIdWithCurrentTime(bookerId, page));
            case FUTURE:
                return BookingDtoMapper.toBookingDto(
                        getFutureBookingsByBookerIdWithCurrentTime(bookerId, page));
            case WAITING:
                return BookingDtoMapper.toBookingDto(
                        getWaitingBookingsByBookerId(bookerId, page));
            case REJECTED:
                return BookingDtoMapper.toBookingDto(
                        getRejectedBookingsByBookerId(bookerId, page));
            default:
                throw StateNotImplementedException.getFromState(state);
        }
    }

    private List<BookingResponseDto> getAllByBookerItemsWithPagination(Long ownerId, State state,
                                                                       Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        switch (state) {
            case ALL:
                return BookingDtoMapper.toBookingDto(
                        getAllBookingsByOwnerId(ownerId, page));
            case CURRENT:
                return BookingDtoMapper.toBookingDto(
                        getCurrentBookingsByOwnerIdWithCurrentTime(ownerId, page));
            case PAST:
                return BookingDtoMapper.toBookingDto(
                        getPastBookingsByOwnerIdWithCurrentTime(ownerId, page));
            case FUTURE:
                return BookingDtoMapper.toBookingDto(
                        getFutureBookingsByOwnerIdWithCurrentTime(ownerId, page));
            case WAITING:
                return BookingDtoMapper.toBookingDto(
                        getWaitingBookingsByOwnerId(ownerId, page));
            case REJECTED:
                return BookingDtoMapper.toBookingDto(
                        getRejectedBookingsByOwnerId(ownerId, page));
            default:
                throw StateNotImplementedException.getFromState(state);
        }
    }

    private Page<Booking> getAllBookingsByBookerId(Long bookerId, Pageable page) {
        return bookingRepository.findAllByBookerIdOrderByStartTimeDesc(
                bookerId, page);
    }

    private Page<Booking> getCurrentBookingsByBookerIdWithCurrentTime(Long bookerId, Pageable page) {
        LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                bookerId, time, time, page);
    }

    private Page<Booking> getPastBookingsByBookerIdWithCurrentTime(Long bookerId, Pageable page) {
        return bookingRepository.findAllByBookerIdAndEndTimeIsBefore(
                bookerId, LocalDateTime.now(), page);
    }

    private Page<Booking> getFutureBookingsByBookerIdWithCurrentTime(Long bookerId, Pageable page) {
        return bookingRepository.findAllByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                bookerId, LocalDateTime.now(), page);
    }

    private Page<Booking> getWaitingBookingsByBookerId(Long bookerId, Pageable page) {
        return bookingRepository.findAllByBookerIdAndStatusEquals(
                bookerId, Status.WAITING, page);
    }

    private Page<Booking> getRejectedBookingsByBookerId(Long bookerId, Pageable page) {
        return bookingRepository.findAllByBookerIdAndStatusEquals(
                bookerId, Status.REJECTED, page);
    }

    private Page<Booking> getAllBookingsByOwnerId(Long ownerId, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdOrderByStartTimeDesc(
                ownerId, page);
    }

    private Page<Booking> getCurrentBookingsByOwnerIdWithCurrentTime(Long ownerId, Pageable page) {
        LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                ownerId, time, time, page);
    }

    private Page<Booking> getPastBookingsByOwnerIdWithCurrentTime(Long ownerId, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndEndTimeIsBefore(
                ownerId, LocalDateTime.now(), page);
    }

    private Page<Booking> getFutureBookingsByOwnerIdWithCurrentTime(Long ownerId, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                ownerId, LocalDateTime.now(), page);
    }

    private Page<Booking> getWaitingBookingsByOwnerId(Long ownerId, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndStatusEquals(
                ownerId, Status.WAITING, page);
    }

    private Page<Booking> getRejectedBookingsByOwnerId(Long ownerId, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndStatusEquals(
                ownerId, Status.REJECTED, page);
    }
}
