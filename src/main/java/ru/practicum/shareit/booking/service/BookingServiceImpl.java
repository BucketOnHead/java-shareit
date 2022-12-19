package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.in.RequestBookingDto;
import ru.practicum.shareit.booking.dto.out.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

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

        Booking booking = getBooking(bookingDto, userId);
        checkItemAvailableForBooking(booking.getItem());

        Booking savedBooking = bookingRepository.save(booking);
        log.debug("BOOKING[ID_{}] added.", savedBooking.getId());
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
        log.debug("BOOKING[ID_{}] updated.", updatedBooking.getId());
        return BookingDtoMapper.toBookingDto(updatedBooking);
    }

    @Override
    public BookingDto getBookingByIdOnlyForOwnerOrBooker(Long bookingId, Long userId) {
        checkBookingExistsById(bookingRepository, bookingId);
        checkUserExistsById(userRepository, userId);

        Booking booking = bookingRepository.getReferenceById(bookingId);
        checkOwnerOrBooker(booking, userId);

        BookingDto bookingDto = BookingDtoMapper.toBookingDto(booking);
        log.debug("BOOKING[ID_{}]<DTO> returned.", bookingDto.getId());
        return bookingDto;
    }

    @Override
    public List<BookingDto> getAllByBookerId(Long bookerId, String possibleState,
                                             Integer from, Integer size) {
        boolean withPagination = checkPaginationParameters(from, size);
        checkUserExistsById(userRepository, bookerId);
        State state = checkState(possibleState);

        List<BookingDto> bookingsByBookerId;
        if (withPagination) {
            bookingsByBookerId = getAllByBookerIdWithPagination(bookerId, state, from, size);
        } else {
            bookingsByBookerId = getAllByBookerIdWithoutPagination(bookerId, state);
        }
        log.debug("All BOOKING<DTO> by booker-USER[ID_{}] returned, {} in total.",
                bookerId, bookingsByBookerId.size());
        return bookingsByBookerId;
    }

    @Override
    public List<BookingDto> getAllByBookerItems(Long ownerId, String possibleState,
                                                Integer from, Integer size) {
        boolean withPagination = checkPaginationParameters(from, size);
        checkUserExistsById(userRepository, ownerId);
        State state = checkState(possibleState);

        List<BookingDto> bookingsByBookerItems;
        if (withPagination) {
            bookingsByBookerItems = getAllByBookerItemsWithPagination(ownerId, state, from, size);
        } else {
            bookingsByBookerItems = getAllByBookerItemsWithoutPagination(ownerId, state);
        }
        log.debug("All BOOKING<DTO> by booker-USER[ID_{}] items returned, {} in total.",
                ownerId, bookingsByBookerItems.size());
        return bookingsByBookerItems;
    }

    /**
     * The method checks the parameters according
     * to the all-or-nothing principle, and also returns
     * a boolean value indicating the use of pagination.
     *
     * @param from Index of the starting element,
     * @param size Number of items to display.
     * @return Boolean value indicating the use of pagination.
     */
    private static boolean checkPaginationParameters(Integer from, Integer size) {
        boolean withPagination = (from != null) && (size != null);
        boolean withoutPagination = (from == null) && (size == null);

        if (!withPagination && !withoutPagination) {
            throw new IncorrectDataException(String.format(""
                    + "Pagination parameters must be specified in full or not specified at all, "
                    + "but it was: from = '%d' and size = '%d'", from, size));
        }

        return withPagination;
    }

    private static void checkBookingStatusNotApprove(Booking booking) {
        if (booking.getStatus() == Booking.Status.APPROVED) {
            throw BookingAlreadyApprovedException.getFromBookingId(booking.getId());
        }
    }

    private static State checkState(String possibleState) {
        try {
            return State.valueOf(possibleState);
        } catch (IllegalArgumentException ex) {
            throw IncorrectStateException.getFromString(possibleState);
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

    private Booking getBooking(RequestBookingDto bookingDto, Long userId) {
        User booker = userRepository.getReferenceById(userId);
        Item item = itemRepository.getReferenceById(bookingDto.getItemId());
        return BookingDtoMapper.toBooking(bookingDto, booker, item);
    }

    private List<BookingDto> getAllByBookerIdWithoutPagination(Long bookerId, State state) {
        switch (state) {
            case ALL:
                return BookingDtoMapper.toBookingDto(
                        getAllBookingsByBookerId(bookerId));
            case CURRENT:
                return BookingDtoMapper.toBookingDto(
                        getCurrentBookingsByBookerIdWithCurrentTime(bookerId));
            case PAST:
                return BookingDtoMapper.toBookingDto(
                        getPastBookingsByBookerIdWithCurrentTime(bookerId));
            case FUTURE:
                return BookingDtoMapper.toBookingDto(
                        getFutureBookingsByBookerIdWithCurrentTime(bookerId));
            case WAITING:
                return BookingDtoMapper.toBookingDto(
                        getWaitingBookingsByBookerId(bookerId));
            case REJECTED:
                return BookingDtoMapper.toBookingDto(
                        getRejectedBookingsByBookerId(bookerId));
            default:
                throw StateNotImplementedException.getFromState(state);
        }
    }

    private List<BookingDto> getAllByBookerIdWithPagination(Long bookerId, State state,
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

    private List<BookingDto> getAllByBookerItemsWithoutPagination(Long ownerId, State state) {
        switch (state) {
            case ALL:
                return BookingDtoMapper.toBookingDto(
                        getAllBookingsByOwnerId(ownerId));
            case CURRENT:
                return BookingDtoMapper.toBookingDto(
                        getCurrentBookingsByOwnerIdWithCurrentTime(ownerId));
            case PAST:
                return BookingDtoMapper.toBookingDto(
                        getPastBookingsByOwnerIdWithCurrentTime(ownerId));
            case FUTURE:
                return BookingDtoMapper.toBookingDto(
                        getFutureBookingsByOwnerIdWithCurrentTime(ownerId));
            case WAITING:
                return BookingDtoMapper.toBookingDto(
                        getWaitingBookingsByOwnerId(ownerId));
            case REJECTED:
                return BookingDtoMapper.toBookingDto(
                        getRejectedBookingsByOwnerId(ownerId));
            default:
                throw StateNotImplementedException.getFromState(state);
        }
    }

    private List<BookingDto> getAllByBookerItemsWithPagination(Long ownerId, State state,
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

    private List<Booking> getAllBookingsByBookerId(Long bookerId) {
        return bookingRepository.findAllByBookerIdOrderByStartTimeDesc(
                bookerId);
    }

    private Page<Booking> getAllBookingsByBookerId(Long bookerId, Pageable page) {
        return bookingRepository.findAllByBookerIdOrderByStartTimeDesc(
                bookerId, page);
    }

    private List<Booking> getCurrentBookingsByBookerIdWithCurrentTime(Long bookerId) {
        LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                bookerId, time, time);
    }

    private Page<Booking> getCurrentBookingsByBookerIdWithCurrentTime(Long bookerId, Pageable page) {
        LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                bookerId, time, time, page);
    }

    private List<Booking> getPastBookingsByBookerIdWithCurrentTime(Long bookerId) {
        return bookingRepository.findAllByBookerIdAndEndTimeIsBefore(
                bookerId, LocalDateTime.now());
    }

    private Page<Booking> getPastBookingsByBookerIdWithCurrentTime(Long bookerId, Pageable page) {
        return bookingRepository.findAllByBookerIdAndEndTimeIsBefore(
                bookerId, LocalDateTime.now(), page);
    }

    private List<Booking> getFutureBookingsByBookerIdWithCurrentTime(Long bookerId) {
        return bookingRepository.findAllByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                bookerId, LocalDateTime.now());
    }

    private Page<Booking> getFutureBookingsByBookerIdWithCurrentTime(Long bookerId, Pageable page) {
        return bookingRepository.findAllByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                bookerId, LocalDateTime.now(), page);
    }

    private List<Booking> getWaitingBookingsByBookerId(Long bookerId) {
        return bookingRepository.findAllByBookerIdAndStatusEquals(
                bookerId, Status.WAITING);
    }

    private Page<Booking> getWaitingBookingsByBookerId(Long bookerId, Pageable page) {
        return bookingRepository.findAllByBookerIdAndStatusEquals(
                bookerId, Status.WAITING, page);
    }

    private List<Booking> getRejectedBookingsByBookerId(Long bookerId) {
        return bookingRepository.findAllByBookerIdAndStatusEquals(
                bookerId, Status.REJECTED);
    }

    private Page<Booking> getRejectedBookingsByBookerId(Long bookerId, Pageable page) {
        return bookingRepository.findAllByBookerIdAndStatusEquals(
                bookerId, Status.REJECTED, page);
    }

    private List<Booking> getAllBookingsByOwnerId(Long ownerId) {
        return bookingRepository.findAllByItemOwnerIdOrderByStartTimeDesc(
                ownerId);
    }

    private Page<Booking> getAllBookingsByOwnerId(Long ownerId, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdOrderByStartTimeDesc(
                ownerId, page);
    }

    private List<Booking> getCurrentBookingsByOwnerIdWithCurrentTime(Long ownerId) {
        LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                ownerId, time, time);
    }

    private Page<Booking> getCurrentBookingsByOwnerIdWithCurrentTime(Long ownerId, Pageable page) {
        LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                ownerId, time, time, page);
    }

    private List<Booking> getPastBookingsByOwnerIdWithCurrentTime(Long ownerId) {
        return bookingRepository.findAllByItemOwnerIdAndEndTimeIsBefore(
                ownerId, LocalDateTime.now());
    }

    private Page<Booking> getPastBookingsByOwnerIdWithCurrentTime(Long ownerId, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndEndTimeIsBefore(
                ownerId, LocalDateTime.now(), page);
    }

    private List<Booking> getFutureBookingsByOwnerIdWithCurrentTime(Long ownerId) {
        return bookingRepository.findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                ownerId, LocalDateTime.now());
    }

    private Page<Booking> getFutureBookingsByOwnerIdWithCurrentTime(Long ownerId, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                ownerId, LocalDateTime.now(), page);
    }

    private List<Booking> getWaitingBookingsByOwnerId(Long ownerId) {
        return bookingRepository.findAllByItemOwnerIdAndStatusEquals(
                ownerId, Status.WAITING);
    }

    private Page<Booking> getWaitingBookingsByOwnerId(Long ownerId, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndStatusEquals(
                ownerId, Status.WAITING, page);
    }

    private List<Booking> getRejectedBookingsByOwnerId(Long ownerId) {
        return bookingRepository.findAllByItemOwnerIdAndStatusEquals(
                ownerId, Status.REJECTED);
    }

    private Page<Booking> getRejectedBookingsByOwnerId(Long ownerId, Pageable page) {
        return bookingRepository.findAllByItemOwnerIdAndStatusEquals(
                ownerId, Status.REJECTED, page);
    }
}
