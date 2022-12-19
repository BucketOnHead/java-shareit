package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.in.RequestBookingDto;
import ru.practicum.shareit.booking.dto.out.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService.State;
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookingServiceImplTest {
    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private UserRepository userRepository;
    private final BookingServiceImpl bookingService;

    /**
     * Method under test: {@link BookingServiceImpl#checkBookingExistsById(BookingRepository, Long)}
     */
    @Test
    void testCheckBookingExistsById() {
        // test parameters
        final Long bookingId = 1L;
        // test context
        when(bookingRepository.existsById(anyLong())).thenReturn(true);

        BookingServiceImpl.checkBookingExistsById(bookingRepository, bookingId);

        verify(bookingRepository).existsById(bookingId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#checkBookingExistsById(BookingRepository, Long)}
     */
    @Test
    void testCheckBookingExistsById_NotExists() {
        // test parameters
        final Long bookingId = 1L;
        // test context
        when(bookingRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(BookingNotFoundException.class, () ->
                BookingServiceImpl.checkBookingExistsById(bookingRepository, bookingId));

        verify(bookingRepository).existsById(bookingId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#addBooking(RequestBookingDto, Long)}
     */
    @Test
    void testAddBooking() {
        // test parameters
        final Long ownerId  = 1L;
        final Long bookerId = 2L;
        final Long itemId   = 1L;
        // test context
        final RequestBookingDto requestBookingDto = getRequestBookingDto(itemId);
        final User              booker            = getBooker(bookerId);
        final User              owner             = getOwner(ownerId);
        final Item              item              = getItem(itemId, TRUE, owner, null);
        final Booking           booking           = getBooking(requestBookingDto, booker, item);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.getReferenceById(anyLong())).thenReturn(booker);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto bookingDto = bookingService.addBooking(requestBookingDto, bookerId);

        assertBookingEquals(booking, bookingDto);
        verify(bookingRepository).save(any(Booking.class));
    }

    /**
     * Method under test: {@link BookingServiceImpl#addBooking(RequestBookingDto, Long)}
     */
    @Test
    void testAddBooking_UserNotExists() {
        // test parameters
        final Long userId = 1L;
        final Long itemId = 1L;
        // test context
        final RequestBookingDto requestBookingDto = getRequestBookingDto(itemId);
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                bookingService.addBooking(requestBookingDto, userId));

        verify(userRepository).existsById(userId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#addBooking(RequestBookingDto, Long)}
     */
    @Test
    void testAddBooking_ItemNotExists() {
        // test parameters
        final Long userId = 1L;
        final Long itemId = 1L;
        // test context
        final RequestBookingDto requestBookingDto = getRequestBookingDto(itemId);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ItemNotFoundException.class, () ->
                bookingService.addBooking(requestBookingDto, userId));

        verify(userRepository).existsById(userId);
        verify(itemRepository).existsById(itemId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#addBooking(RequestBookingDto, Long)}
     */
    @Test
    void testAddBooking_UserBookingHisItem() {
        // test parameters
        final Long userId = 1L;
        final Long itemId = 1L;
        // test context
        final RequestBookingDto requestBookingDto = getRequestBookingDto(itemId);
        final User              owner             = getOwner(userId);
        final Item              item              = getItem(itemId, TRUE, owner, null);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);

        assertThrows(BookingLogicException.class, () ->
                bookingService.addBooking(requestBookingDto, userId));

        verify(userRepository).existsById(userId);
        verify(itemRepository).existsById(itemId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#addBooking(RequestBookingDto, Long)}
     */
    @Test
    void testAddBooking_ItemNotAvailableForBooking() {
        // test parameters
        final Long ownerId  = 1L;
        final Long bookerId = 2L;
        final Long itemId   = 1L;
        // test context
        final RequestBookingDto requestBookingDto = getRequestBookingDto(itemId);
        final User              booker            = getBooker(bookerId);
        final User              owner             = getOwner(ownerId);
        final Item              item              = getItem(itemId, Boolean.FALSE, owner, null);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.getReferenceById(anyLong())).thenReturn(booker);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);

        assertThrows(ItemNotAvailableForBookingException.class, () ->
                bookingService.addBooking(requestBookingDto, bookerId));

        verify(userRepository).existsById(bookerId);
        verify(itemRepository).existsById(itemId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBookingStatus(Long, Boolean, Long)}
     */
    @Test
    void testUpdateBookingStatus_Approved() {
        // test parameters
        final Long    bookerId  = 1L;
        final Long    ownerId   = 2L;
        final Long    itemId    = 1L;
        final Long    bookingId = 1L;
        final Status  status    = Status.WAITING;
        // test context
        final User    booker         = getBooker(bookerId);
        final User    owner          = getOwner(ownerId);
        final Item    item           = getItem(itemId, TRUE, owner, null);
        final Booking booking        = getBooking(bookingId, status, booker, item);
        final Booking updatedBooking = getBooking(booking.getId(), Status.APPROVED,
                                                  booking.getBooker(), booking.getItem());
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);

        BookingDto bookingDto = bookingService.updateBookingStatus(bookingId, TRUE, ownerId);

        assertBookingEquals(updatedBooking, bookingDto);
        verify(bookingRepository).existsById(bookingId);
        verify(userRepository).existsById(ownerId);
        verify(bookingRepository).getReferenceById(bookingId);
        verify(itemRepository).getReferenceById(itemId);
        verify(bookingRepository).save(any(Booking.class));
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBookingStatus(Long, Boolean, Long)}
     */
    @Test
    void testUpdateBookingStatus_Rejected() {
        // test parameters
        final Long    bookerId  = 1L;
        final Long    ownerId   = 2L;
        final Long    itemId    = 1L;
        final Long    bookingId = 1L;
        final Status  status    = Status.WAITING;
        // test context
        final User    booker         = getBooker(bookerId);
        final User    owner          = getOwner(ownerId);
        final Item    item           = getItem(itemId, TRUE, owner, null);
        final Booking booking        = getBooking(bookingId, status, booker, item);
        final Booking updatedBooking = getBooking(booking.getId(), Status.REJECTED,
                                                  booking.getBooker(), booking.getItem());
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);

        BookingDto bookingDto = bookingService.updateBookingStatus(bookingId, FALSE, ownerId);

        assertBookingEquals(updatedBooking, bookingDto);
        verify(bookingRepository).existsById(bookingId);
        verify(userRepository).existsById(ownerId);
        verify(bookingRepository).getReferenceById(bookingId);
        verify(itemRepository).getReferenceById(itemId);
        verify(bookingRepository).save(any(Booking.class));
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBookingStatus(Long, Boolean, Long)}
     */
    @Test
    void testUpdateBookingStatus_BookingNotExists() {
        // test parameters
        final Long    userId    = 1L;
        final Long    bookingId = 1L;
        final Boolean approved  = TRUE;
        // test context
        when(bookingRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(BookingNotFoundException.class, () ->
                bookingService.updateBookingStatus(bookingId, approved, userId));

        verify(bookingRepository).existsById(bookingId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBookingStatus(Long, Boolean, Long)}
     */
    @Test
    void testUpdateBookingStatus_UserNotExists() {
        // test parameters
        final Long    userId    = 1L;
        final Long    bookingId = 1L;
        final Boolean approved  = TRUE;
        // test context
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                bookingService.updateBookingStatus(bookingId, approved, userId));

        verify(bookingRepository).existsById(bookingId);
        verify(userRepository).existsById(userId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBookingStatus(Long, Boolean, Long)}
     */
    @Test
    void testUpdateBookingStatus_NotOwner() {
        // test parameters
        final Long    userId    = 1L;
        final Long    bookerId  = 2L;
        final Long    ownerId   = 3L;
        final Long    itemId    = 1L;
        final Long    bookingId = 1L;
        final Boolean approved  = TRUE;
        final Status  status    = Status.WAITING;
        // test context
        final User    booker  = getBooker(bookerId);
        final User    owner   = getOwner(ownerId);
        final Item    item    = getItem(itemId, TRUE, owner, null);
        final Booking booking = getBooking(bookingId, status, booker, item);
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);

        assertThrows(ItemNotFoundException.class, () ->
                bookingService.updateBookingStatus(bookingId, approved, userId));

        verify(bookingRepository).existsById(bookingId);
        verify(userRepository).existsById(userId);
        verify(bookingRepository).getReferenceById(bookingId);
        verify(itemRepository).getReferenceById(itemId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBookingStatus(Long, Boolean, Long)}
     */
    @Test
    void testUpdateBookingStatus_BookingAlreadyApproved() {
        // test parameters
        final Long    bookerId  = 1L;
        final Long    ownerId   = 2L;
        final Long    itemId    = 1L;
        final Long    bookingId = 1L;
        final Boolean approved  = TRUE;
        // test context
        final User    booker  = getBooker(bookerId);
        final User    owner   = getOwner(ownerId);
        final Item    item    = getItem(itemId, TRUE, owner, null);
        final Booking booking = getBooking(bookingId, Status.APPROVED, booker, item);
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);

        assertThrows(BookingAlreadyApprovedException.class, () ->
                bookingService.updateBookingStatus(bookingId, approved, ownerId));

        verify(bookingRepository).existsById(bookingId);
        verify(userRepository).existsById(ownerId);
        verify(bookingRepository).getReferenceById(bookingId);
        verify(itemRepository).getReferenceById(itemId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingByIdOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingByIdOnlyForOwnerOrBooker_BookingNotExists() {
        // test parameters
        final Long userId    = 1L;
        final Long bookingId = 1L;
        // test context
        when(bookingRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(BookingNotFoundException.class,
                () -> bookingService.getBookingByIdOnlyForOwnerOrBooker(bookingId, userId));

        verify(bookingRepository).existsById(bookingId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingByIdOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingByIdOnlyForOwnerOrBooker_UserNotExists() {
        // test parameters
        final Long userId    = 1L;
        final Long bookingId = 1L;
        // test context
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingByIdOnlyForOwnerOrBooker(bookingId, userId));

        verify(bookingRepository).existsById(bookingId);
        verify(userRepository).existsById(userId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingByIdOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingByIdOnlyForOwnerOrBooker_Owner() {
        // test parameters
        final Long   bookerId  = 1L;
        final Long   ownerId   = 2L;
        final Long   bookingId = 1L;
        final Long   itemId    = 1L;
        final Status status    = Status.WAITING;
        // test context
        final User    booker  = getBooker(bookerId);
        final User    owner   = getOwner(ownerId);
        final Item    item    = getItem(itemId, TRUE, owner, null);
        final Booking booking = getBooking(bookingId, status, booker, item);
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking);

        BookingDto bookingDto =
                bookingService.getBookingByIdOnlyForOwnerOrBooker(bookingId, ownerId);

        assertBookingEquals(booking, bookingDto);
        verify(bookingRepository).existsById(bookingId);
        verify(userRepository).existsById(ownerId);
        verify(bookingRepository).getReferenceById(bookingId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingByIdOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingByIdOnlyForOwnerOrBooker_Booker() {
        // test parameters
        final Long   bookerId  = 1L;
        final Long   ownerId   = 2L;
        final Long   bookingId = 1L;
        final Long   itemId    = 1L;
        final Status status    = Status.WAITING;
        // test context
        final User    booker  = getBooker(bookerId);
        final User    owner   = getOwner(ownerId);
        final Item    item    = getItem(itemId, TRUE, owner, null);
        final Booking booking = getBooking(bookingId, status, booker, item);
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking);

        BookingDto bookingDto =
                bookingService.getBookingByIdOnlyForOwnerOrBooker(bookingId, bookerId);

        assertBookingEquals(booking, bookingDto);
        verify(bookingRepository).existsById(bookingId);
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).getReferenceById(bookingId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingByIdOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingByIdOnlyForOwnerOrBooker_NotOwnerAndNotBooker() {
        // test parameters
        final Long   userId    = 1L;
        final Long   bookerId  = 2L;
        final Long   ownerId   = 3L;
        final Long   bookingId = 1L;
        final Long   itemId    = 1L;
        final Status status    = Status.WAITING;
        // test context
        final User    booker  = getBooker(bookerId);
        final User    owner   = getOwner(ownerId);
        final Item    item    = getItem(itemId, TRUE, owner, null);
        final Booking booking = getBooking(bookingId, status, booker, item);
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking);

        assertThrows(BookingNotFoundException.class, () ->
                bookingService.getBookingByIdOnlyForOwnerOrBooker(bookingId, userId));

        verify(bookingRepository).existsById(bookingId);
        verify(userRepository).existsById(userId);
        verify(bookingRepository).getReferenceById(bookingId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithPaginationAll() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.ALL.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdOrderByStartTimeDesc(anyLong(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdOrderByStartTimeDesc(anyLong(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithPaginationCurrent() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.CURRENT.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                anyLong(), any(), any(), any())).thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                anyLong(), any(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithPaginationPast() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.PAST.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndEndTimeIsBefore(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdAndEndTimeIsBefore(anyLong(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithPaginationFuture() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.FUTURE.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                anyLong(), any(), any())).thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                anyLong(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithPaginationWaiting() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.WAITING.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStatusEquals(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdAndStatusEquals(anyLong(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithPaginationRejected() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.REJECTED.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStatusEquals(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdAndStatusEquals(anyLong(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithoutPaginationAll() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.ALL.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdOrderByStartTimeDesc(anyLong()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdOrderByStartTimeDesc(anyLong());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithoutPaginationCurrent() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.CURRENT.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                anyLong(), any(), any())).thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                anyLong(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithoutPaginationPast() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.PAST.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndEndTimeIsBefore(anyLong(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdAndEndTimeIsBefore(anyLong(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithoutPaginationFuture() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.FUTURE.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                anyLong(), any())).thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                anyLong(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithoutPaginationWaiting() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.WAITING.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStatusEquals(anyLong(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdAndStatusEquals(anyLong(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_WithoutPaginationRejected() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.REJECTED.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdAndStatusEquals(anyLong(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerId(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByBookerIdAndStatusEquals(anyLong(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_NullFrom() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.CURRENT.name();
        final Integer size     = 1;

        assertThrows(IncorrectDataException.class, () ->
                bookingService.getAllByBookerId(bookerId, state, null, size));
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_NullSize() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.CURRENT.name();
        final Integer from     = 1;

        assertThrows(IncorrectDataException.class, () ->
                bookingService.getAllByBookerId(bookerId, state, from, null));
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_UserNotExists() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.CURRENT.name();
        final Integer from     = 1;
        final Integer size     = 1;
        // test context
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                bookingService.getAllByBookerId(bookerId, state, from, size));

        verify(userRepository).existsById(bookerId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId_UnknownState() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        // test context
        final String state = "UnknownState123";
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(IncorrectStateException.class, () ->
                bookingService.getAllByBookerId(bookerId, state, from, size));

        verify(userRepository).existsById(bookerId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithPaginationAll() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.ALL.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdOrderByStartTimeDesc(anyLong(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdOrderByStartTimeDesc(anyLong(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithPaginationCurrent() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.CURRENT.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                anyLong(), any(), any(), any())).thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                anyLong(), any(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithPaginationPast() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.PAST.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdAndEndTimeIsBefore(
                anyLong(), any(), any())).thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdAndEndTimeIsBefore(anyLong(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithPaginationFuture() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.FUTURE.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                anyLong(), any(), any())).thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                anyLong(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithPaginationWaiting() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.WAITING.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdAndStatusEquals(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdAndStatusEquals(anyLong(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithPaginationRejected() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        final String  state    = State.REJECTED.name();
        // test context
        final Page<Booking> bookings = new PageImpl<>(getBookings());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdAndStatusEquals(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, from, size);

        assertEquals(bookings.toList().size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdAndStatusEquals(anyLong(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithoutPaginationAll() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.ALL.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdOrderByStartTimeDesc(anyLong()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdOrderByStartTimeDesc(anyLong());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithoutPaginationCurrent() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.CURRENT.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                anyLong(), any(), any())).thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
                anyLong(), any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithoutPaginationPast() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.PAST.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdAndEndTimeIsBefore(anyLong(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdAndEndTimeIsBefore(anyLong(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithoutPaginationFuture() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.FUTURE.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                anyLong(), any())).thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
                anyLong(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithoutPaginationWaiting() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.WAITING.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdAndStatusEquals(anyLong(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdAndStatusEquals(anyLong(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_WithoutPaginationRejected() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.REJECTED.name();
        // test context
        final List<Booking> bookings = getBookings();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdAndStatusEquals(anyLong(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = bookingService.getAllByBookerItems(bookerId, state, null, null);

        assertEquals(bookings.size(), bookingsDto.size());
        verify(userRepository).existsById(bookerId);
        verify(bookingRepository).findAllByItemOwnerIdAndStatusEquals(anyLong(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_NullFrom() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.CURRENT.name();
        final Integer size     = 1;

        assertThrows(IncorrectDataException.class, () ->
                bookingService.getAllByBookerItems(bookerId, state, null, size));
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_NullSize() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.CURRENT.name();
        final Integer from     = 1;

        assertThrows(IncorrectDataException.class, () ->
                bookingService.getAllByBookerItems(bookerId, state, from, null));
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_UserNotExists() {
        // test parameters
        final Long    bookerId = 1L;
        final String  state    = State.CURRENT.name();
        final Integer from     = 1;
        final Integer size     = 1;
        // test context
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                bookingService.getAllByBookerItems(bookerId, state, from, size));

        verify(userRepository).existsById(bookerId);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems_UnknownState() {
        // test parameters
        final Long    bookerId = 1L;
        final Integer from     = 1;
        final Integer size     = 1;
        // test context
        final String state = "UnknownState123";
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(IncorrectStateException.class, () ->
                bookingService.getAllByBookerItems(bookerId, state, from, size));

        verify(userRepository).existsById(bookerId);
    }

    private static void assertBookingEquals(Booking booking, BookingDto bookingDto) {
        assertEquals(booking.getId(),        bookingDto.getId());
        assertEquals(booking.getStartTime(), bookingDto.getStart());
        assertEquals(booking.getEndTime(),   bookingDto.getEnd());
        assertEquals(booking.getStatus(),    bookingDto.getStatus());

        assertEquals(booking.getItem().getId(),   bookingDto.getItem().getId());
        assertEquals(booking.getItem().getName(), bookingDto.getItem().getName());

        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
    }

    private static User getBooker(Long bookerId) {
        final String name  = String.format("Booker%dName", bookerId);
        final String email = String.format("user.booker%d@example.org", bookerId);

        User booker = new User();
        booker.setId(bookerId);
        booker.setName(name);
        booker.setEmail(email);

        return booker;
    }

    private static User getOwner(Long ownerId) {
        final String name  = String.format("Owner%dName", ownerId);
        final String email = String.format("user.owner%d@example.org", ownerId);

        User owner = new User();
        owner.setId(ownerId);
        owner.setName(name);
        owner.setEmail(email);

        return owner;
    }

    private static User getRequester(Long requesterId) {
        final String name  = String.format("Requester%dName", requesterId);
        final String email = String.format("user.requester%d@example.org", requesterId);

        User requester = new User();
        requester.setId(requesterId);
        requester.setName(name);
        requester.setEmail(email);

        return requester;
    }

    private static Item getItem(Long itemId, Boolean isAvailable, User owner, ItemRequest itemRequest) {
        final String name        = String.format("Item%dName", itemId);
        final String description = String.format("Item %d description", itemId);

        Item item = new Item();
        item.setId(itemId);
        item.setName(name);
        item.setDescription(description);
        item.setIsAvailable(isAvailable);
        item.setOwner(owner);
        item.setItemRequest(itemRequest);

        return item;
    }

    private static Booking getBooking(Long bookingId, Status status, User booker, Item item) {
        final LocalDateTime start = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        final LocalDateTime end   = LocalDateTime.of(7, 6, 5, 4, 3, 2, 1);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus(status);
        booking.setStartTime(start);
        booking.setEndTime(end);
        booking.setBooker(booker);
        booking.setItem(item);

        return booking;
    }

    private static Booking getBooking(RequestBookingDto requestBookingDto, User booker, Item item) {
        return BookingDtoMapper.toBooking(requestBookingDto, booker, item);
    }

    private static RequestBookingDto getRequestBookingDto(Long itemId) {
        final LocalDateTime start = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        final LocalDateTime end   = LocalDateTime.of(7, 6, 5, 4, 3, 2, 1);

        RequestBookingDto bookingDto = new RequestBookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);

        return bookingDto;
    }

    private List<Booking> getBookings(Booking... bookings) {
        return Arrays.asList(bookings);
    }
}
