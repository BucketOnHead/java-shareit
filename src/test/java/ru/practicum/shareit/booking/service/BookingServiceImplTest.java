package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.in.RequestBookingDto;
import ru.practicum.shareit.booking.dto.out.BookingDto;
import ru.practicum.shareit.booking.exception.StateNotImplementedException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {BookingServiceImpl.class})
@ExtendWith(SpringExtension.class)
class BookingServiceImplTest {
    @MockBean
    private BookingRepository bookingRepository;

    @Autowired
    private BookingServiceImpl bookingServiceImpl;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link BookingServiceImpl#checkBookingExistsById(BookingRepository, Long)}
     */
    @Test
    void testCheckBookingExistsById() {
        BookingRepository bookingRepository1 = mock(BookingRepository.class);
        when(bookingRepository1.existsById((Long) any())).thenReturn(true);
        BookingServiceImpl.checkBookingExistsById(bookingRepository1, 123L);
        verify(bookingRepository1).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#checkBookingExistsById(BookingRepository, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCheckBookingExistsById2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.booking.exception.BookingNotFoundException: BOOKING[ID_123] not found
        //       at ru.practicum.shareit.booking.exception.BookingNotFoundException.getFromBookingId(BookingNotFoundException.java:20)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.checkBookingExistsById(BookingServiceImpl.java:41)
        //   See https://diff.blue/R013 to resolve this issue.

        BookingRepository bookingRepository1 = mock(BookingRepository.class);
        when(bookingRepository1.existsById((Long) any())).thenReturn(false);
        BookingServiceImpl.checkBookingExistsById(bookingRepository1, 123L);
    }

    /**
     * Method under test: {@link BookingServiceImpl#checkBookingExistsById(BookingRepository, Long)}
     */
    @Test
    void testCheckBookingExistsById3() {
        BookingRepository bookingRepository1 = mock(BookingRepository.class);
        when(bookingRepository1.existsById((Long) any())).thenThrow(new IncorrectDataException("An error occurred"));
        assertThrows(IncorrectDataException.class,
                () -> BookingServiceImpl.checkBookingExistsById(bookingRepository1, 123L));
        verify(bookingRepository1).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#addBooking(RequestBookingDto, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddBooking() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.booking.exception.BookingLogicException: USER[ID_123] cannot book own ITEM[ID_123]
        //       at ru.practicum.shareit.booking.exception.BookingLogicException.getFromOwnerIdAndItemId(BookingLogicException.java:18)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.checkUserNotOwnerByItemIdAndUserId(BookingServiceImpl.java:183)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.addBooking(BookingServiceImpl.java:50)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userRepository.existsById((Long) any())).thenReturn(true);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user1);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemRepository.existsById((Long) any())).thenReturn(true);

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        requestBookingDto.setItemId(123L);
        requestBookingDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        bookingServiceImpl.addBooking(requestBookingDto, 123L);
    }

    /**
     * Method under test: {@link BookingServiceImpl#addBooking(RequestBookingDto, Long)}
     */
    @Test
    void testAddBooking2() {
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(itemRepository.getReferenceById((Long) any())).thenThrow(new IncorrectDataException("An error occurred"));
        when(itemRepository.existsById((Long) any())).thenReturn(true);

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        requestBookingDto.setItemId(123L);
        requestBookingDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        assertThrows(IncorrectDataException.class, () -> bookingServiceImpl.addBooking(requestBookingDto, 123L));
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).existsById((Long) any());
        verify(itemRepository).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#addBooking(RequestBookingDto, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddBooking3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.user.exception.UserNotFoundException: USER[ID_123] not found
        //       at ru.practicum.shareit.user.exception.UserNotFoundException.getFromUserId(UserNotFoundException.java:18)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById(UserServiceImpl.java:26)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.addBooking(BookingServiceImpl.java:48)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userRepository.existsById((Long) any())).thenReturn(false);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user1);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemRepository.existsById((Long) any())).thenReturn(true);

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        requestBookingDto.setItemId(123L);
        requestBookingDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        bookingServiceImpl.addBooking(requestBookingDto, 123L);
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBookingStatus(Long, Boolean, Long)}
     */
    @Test
    void testUpdateBookingStatus() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user4);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("Name");
        item1.setOwner(user5);

        Booking booking1 = new Booking();
        booking1.setBooker(user3);
        booking1.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(123L);
        booking1.setItem(item1);
        booking1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        when(bookingRepository.save((Booking) any())).thenReturn(booking1);
        when(bookingRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingRepository.existsById((Long) any())).thenReturn(true);
        when(userRepository.existsById((Long) any())).thenReturn(true);

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(123L);
        user6.setName("Name");

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest2.setDescription("The characteristics of someone or something");
        itemRequest2.setId(123L);
        itemRequest2.setRequester(user6);

        User user7 = new User();
        user7.setEmail("jane.doe@example.org");
        user7.setId(123L);
        user7.setName("Name");

        Item item2 = new Item();
        item2.setDescription("The characteristics of someone or something");
        item2.setId(123L);
        item2.setIsAvailable(true);
        item2.setItemRequest(itemRequest2);
        item2.setName("Name");
        item2.setOwner(user7);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item2);
        BookingDto actualUpdateBookingStatusResult = bookingServiceImpl.updateBookingStatus(123L, true, 123L);
        assertEquals(Booking.Status.WAITING, actualUpdateBookingStatusResult.getStatus());
        assertEquals("01:01", actualUpdateBookingStatusResult.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualUpdateBookingStatusResult.getStart().toLocalTime().toString());
        assertEquals(123L, actualUpdateBookingStatusResult.getId().longValue());
        BookingDto.ItemDto item3 = actualUpdateBookingStatusResult.getItem();
        assertEquals("Name", item3.getName());
        assertEquals(123L, item3.getId().longValue());
        assertEquals(123L, actualUpdateBookingStatusResult.getBooker().getId().longValue());
        verify(bookingRepository).existsById((Long) any());
        verify(bookingRepository).getReferenceById((Long) any());
        verify(bookingRepository).save((Booking) any());
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBookingStatus(Long, Boolean, Long)}
     */
    @Test
    void testUpdateBookingStatus2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        when(bookingRepository.save((Booking) any())).thenThrow(new IncorrectDataException("An error occurred"));
        when(bookingRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingRepository.existsById((Long) any())).thenReturn(true);
        when(userRepository.existsById((Long) any())).thenReturn(true);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user3);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("Name");
        item1.setOwner(user4);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item1);
        assertThrows(IncorrectDataException.class, () -> bookingServiceImpl.updateBookingStatus(123L, true, 123L));
        verify(bookingRepository).existsById((Long) any());
        verify(bookingRepository).getReferenceById((Long) any());
        verify(bookingRepository).save((Booking) any());
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBookingStatus(Long, Boolean, Long)}
     */
    @Test
    void testUpdateBookingStatus3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user4);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("Name");
        item1.setOwner(user5);

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(123L);
        user6.setName("Name");

        User user7 = new User();
        user7.setEmail("jane.doe@example.org");
        user7.setId(123L);
        user7.setName("Name");

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest2.setDescription("The characteristics of someone or something");
        itemRequest2.setId(123L);
        itemRequest2.setRequester(user7);

        User user8 = new User();
        user8.setEmail("jane.doe@example.org");
        user8.setId(123L);
        user8.setName("Name");

        Item item2 = new Item();
        item2.setDescription("The characteristics of someone or something");
        item2.setId(123L);
        item2.setIsAvailable(true);
        item2.setItemRequest(itemRequest2);
        item2.setName("Name");
        item2.setOwner(user8);
        Booking booking1 = mock(Booking.class);
        when(booking1.getItem()).thenReturn(item2);
        when(booking1.getId()).thenReturn(123L);
        when(booking1.getEndTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking1.getStartTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking1.getStatus()).thenReturn(Booking.Status.WAITING);
        when(booking1.getBooker()).thenReturn(user6);
        doNothing().when(booking1).setBooker((User) any());
        doNothing().when(booking1).setEndTime((LocalDateTime) any());
        doNothing().when(booking1).setId((Long) any());
        doNothing().when(booking1).setItem((Item) any());
        doNothing().when(booking1).setStartTime((LocalDateTime) any());
        doNothing().when(booking1).setStatus((Booking.Status) any());
        booking1.setBooker(user3);
        booking1.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(123L);
        booking1.setItem(item1);
        booking1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        when(bookingRepository.save((Booking) any())).thenReturn(booking1);
        when(bookingRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingRepository.existsById((Long) any())).thenReturn(true);
        when(userRepository.existsById((Long) any())).thenReturn(true);

        User user9 = new User();
        user9.setEmail("jane.doe@example.org");
        user9.setId(123L);
        user9.setName("Name");

        ItemRequest itemRequest3 = new ItemRequest();
        itemRequest3.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest3.setDescription("The characteristics of someone or something");
        itemRequest3.setId(123L);
        itemRequest3.setRequester(user9);

        User user10 = new User();
        user10.setEmail("jane.doe@example.org");
        user10.setId(123L);
        user10.setName("Name");

        Item item3 = new Item();
        item3.setDescription("The characteristics of someone or something");
        item3.setId(123L);
        item3.setIsAvailable(true);
        item3.setItemRequest(itemRequest3);
        item3.setName("Name");
        item3.setOwner(user10);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item3);
        BookingDto actualUpdateBookingStatusResult = bookingServiceImpl.updateBookingStatus(123L, true, 123L);
        assertEquals(Booking.Status.WAITING, actualUpdateBookingStatusResult.getStatus());
        assertEquals("01:01", actualUpdateBookingStatusResult.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualUpdateBookingStatusResult.getStart().toLocalTime().toString());
        assertEquals(123L, actualUpdateBookingStatusResult.getId().longValue());
        BookingDto.ItemDto item4 = actualUpdateBookingStatusResult.getItem();
        assertEquals("Name", item4.getName());
        assertEquals(123L, item4.getId().longValue());
        assertEquals(123L, actualUpdateBookingStatusResult.getBooker().getId().longValue());
        verify(bookingRepository).existsById((Long) any());
        verify(bookingRepository).getReferenceById((Long) any());
        verify(bookingRepository).save((Booking) any());
        verify(booking1, atLeast(1)).getId();
        verify(booking1).getEndTime();
        verify(booking1).getStartTime();
        verify(booking1).getStatus();
        verify(booking1).getItem();
        verify(booking1).getBooker();
        verify(booking1).setBooker((User) any());
        verify(booking1).setEndTime((LocalDateTime) any());
        verify(booking1).setId((Long) any());
        verify(booking1).setItem((Item) any());
        verify(booking1).setStartTime((LocalDateTime) any());
        verify(booking1).setStatus((Booking.Status) any());
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBookingStatus(Long, Boolean, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateBookingStatus4() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.exception.IncorrectDataException: An error occurred
        //       at ru.practicum.shareit.booking.mapper.BookingDtoMapper.toBookingDto(BookingDtoMapper.java:52)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.updateBookingStatus(BookingServiceImpl.java:74)
        //   See https://diff.blue/R013 to resolve this issue.

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user4);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("Name");
        item1.setOwner(user5);

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(123L);
        user6.setName("Name");
        Booking booking1 = mock(Booking.class);
        when(booking1.getItem()).thenThrow(new IncorrectDataException("An error occurred"));
        when(booking1.getId()).thenReturn(123L);
        when(booking1.getEndTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking1.getStartTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking1.getStatus()).thenReturn(Booking.Status.WAITING);
        when(booking1.getBooker()).thenReturn(user6);
        doNothing().when(booking1).setBooker((User) any());
        doNothing().when(booking1).setEndTime((LocalDateTime) any());
        doNothing().when(booking1).setId((Long) any());
        doNothing().when(booking1).setItem((Item) any());
        doNothing().when(booking1).setStartTime((LocalDateTime) any());
        doNothing().when(booking1).setStatus((Booking.Status) any());
        booking1.setBooker(user3);
        booking1.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(123L);
        booking1.setItem(item1);
        booking1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        when(bookingRepository.save((Booking) any())).thenReturn(booking1);
        when(bookingRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingRepository.existsById((Long) any())).thenReturn(true);
        when(userRepository.existsById((Long) any())).thenReturn(true);

        User user7 = new User();
        user7.setEmail("jane.doe@example.org");
        user7.setId(123L);
        user7.setName("Name");

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest2.setDescription("The characteristics of someone or something");
        itemRequest2.setId(123L);
        itemRequest2.setRequester(user7);

        User user8 = new User();
        user8.setEmail("jane.doe@example.org");
        user8.setId(123L);
        user8.setName("Name");

        Item item2 = new Item();
        item2.setDescription("The characteristics of someone or something");
        item2.setId(123L);
        item2.setIsAvailable(true);
        item2.setItemRequest(itemRequest2);
        item2.setName("Name");
        item2.setOwner(user8);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item2);
        bookingServiceImpl.updateBookingStatus(123L, true, 123L);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingByIdOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingByIdOnlyForOwnerOrBooker() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        when(bookingRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingRepository.existsById((Long) any())).thenReturn(true);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        BookingDto actualBookingByIdOnlyForOwnerOrBooker = bookingServiceImpl.getBookingByIdOnlyForOwnerOrBooker(123L,
                123L);
        assertEquals(Booking.Status.WAITING, actualBookingByIdOnlyForOwnerOrBooker.getStatus());
        assertEquals("01:01", actualBookingByIdOnlyForOwnerOrBooker.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualBookingByIdOnlyForOwnerOrBooker.getStart().toLocalTime().toString());
        assertEquals(123L, actualBookingByIdOnlyForOwnerOrBooker.getId().longValue());
        BookingDto.ItemDto item1 = actualBookingByIdOnlyForOwnerOrBooker.getItem();
        assertEquals("Name", item1.getName());
        assertEquals(123L, item1.getId().longValue());
        assertEquals(123L, actualBookingByIdOnlyForOwnerOrBooker.getBooker().getId().longValue());
        verify(bookingRepository).existsById((Long) any());
        verify(bookingRepository).getReferenceById((Long) any());
        verify(userRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingByIdOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingByIdOnlyForOwnerOrBooker2() {
        when(bookingRepository.getReferenceById((Long) any())).thenThrow(new IncorrectDataException("An error occurred"));
        when(bookingRepository.existsById((Long) any())).thenReturn(true);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(IncorrectDataException.class,
                () -> bookingServiceImpl.getBookingByIdOnlyForOwnerOrBooker(123L, 123L));
        verify(bookingRepository).existsById((Long) any());
        verify(bookingRepository).getReferenceById((Long) any());
        verify(userRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingByIdOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingByIdOnlyForOwnerOrBooker3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user3);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("Name");
        item1.setOwner(user4);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");
        Booking booking = mock(Booking.class);
        when(booking.getId()).thenReturn(123L);
        when(booking.getEndTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking.getStartTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking.getStatus()).thenReturn(Booking.Status.WAITING);
        when(booking.getBooker()).thenReturn(user5);
        when(booking.getItem()).thenReturn(item1);
        doNothing().when(booking).setBooker((User) any());
        doNothing().when(booking).setEndTime((LocalDateTime) any());
        doNothing().when(booking).setId((Long) any());
        doNothing().when(booking).setItem((Item) any());
        doNothing().when(booking).setStartTime((LocalDateTime) any());
        doNothing().when(booking).setStatus((Booking.Status) any());
        booking.setBooker(user);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        when(bookingRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingRepository.existsById((Long) any())).thenReturn(true);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        BookingDto actualBookingByIdOnlyForOwnerOrBooker = bookingServiceImpl.getBookingByIdOnlyForOwnerOrBooker(123L,
                123L);
        assertEquals(Booking.Status.WAITING, actualBookingByIdOnlyForOwnerOrBooker.getStatus());
        assertEquals("01:01", actualBookingByIdOnlyForOwnerOrBooker.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualBookingByIdOnlyForOwnerOrBooker.getStart().toLocalTime().toString());
        assertEquals(123L, actualBookingByIdOnlyForOwnerOrBooker.getId().longValue());
        BookingDto.ItemDto item2 = actualBookingByIdOnlyForOwnerOrBooker.getItem();
        assertEquals("Name", item2.getName());
        assertEquals(123L, item2.getId().longValue());
        assertEquals(123L, actualBookingByIdOnlyForOwnerOrBooker.getBooker().getId().longValue());
        verify(bookingRepository).existsById((Long) any());
        verify(bookingRepository).getReferenceById((Long) any());
        verify(booking).getId();
        verify(booking).getEndTime();
        verify(booking).getStartTime();
        verify(booking).getStatus();
        verify(booking, atLeast(1)).getItem();
        verify(booking, atLeast(1)).getBooker();
        verify(booking).setBooker((User) any());
        verify(booking).setEndTime((LocalDateTime) any());
        verify(booking).setId((Long) any());
        verify(booking).setItem((Item) any());
        verify(booking).setStartTime((LocalDateTime) any());
        verify(booking).setStatus((Booking.Status) any());
        verify(userRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingByIdOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetBookingByIdOnlyForOwnerOrBooker4() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.booking.exception.StateNotImplementedException: STATE['ALL'] not implemented
        //       at ru.practicum.shareit.booking.mapper.BookingDtoMapper.toBookingDto(BookingDtoMapper.java:47)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.getBookingByIdOnlyForOwnerOrBooker(BookingServiceImpl.java:85)
        //   See https://diff.blue/R013 to resolve this issue.

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user3);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("Name");
        item1.setOwner(user4);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");
        Booking booking = mock(Booking.class);
        when(booking.getId()).thenThrow(StateNotImplementedException.getFromState(BookingService.State.ALL));
        when(booking.getEndTime()).thenThrow(StateNotImplementedException.getFromState(BookingService.State.ALL));
        when(booking.getStartTime()).thenThrow(StateNotImplementedException.getFromState(BookingService.State.ALL));
        when(booking.getStatus()).thenThrow(StateNotImplementedException.getFromState(BookingService.State.ALL));
        when(booking.getBooker()).thenReturn(user5);
        when(booking.getItem()).thenReturn(item1);
        doNothing().when(booking).setBooker((User) any());
        doNothing().when(booking).setEndTime((LocalDateTime) any());
        doNothing().when(booking).setId((Long) any());
        doNothing().when(booking).setItem((Item) any());
        doNothing().when(booking).setStartTime((LocalDateTime) any());
        doNothing().when(booking).setStatus((Booking.Status) any());
        booking.setBooker(user);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        when(bookingRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingRepository.existsById((Long) any())).thenReturn(true);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        bookingServiceImpl.getBookingByIdOnlyForOwnerOrBooker(123L, 123L);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingByIdOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetBookingByIdOnlyForOwnerOrBooker5() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.booking.exception.StateNotImplementedException: STATE['ALL'] not implemented
        //       at ru.practicum.shareit.booking.mapper.BookingDtoMapper.toItemDtoForBookingDto(BookingDtoMapper.java:77)
        //       at ru.practicum.shareit.booking.mapper.BookingDtoMapper.toBookingDto(BookingDtoMapper.java:52)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.getBookingByIdOnlyForOwnerOrBooker(BookingServiceImpl.java:85)
        //   See https://diff.blue/R013 to resolve this issue.

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user3);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");
        Item item1 = mock(Item.class);
        when(item1.getId()).thenThrow(StateNotImplementedException.getFromState(BookingService.State.ALL));
        when(item1.getName()).thenThrow(StateNotImplementedException.getFromState(BookingService.State.ALL));
        when(item1.getOwner()).thenReturn(user5);
        doNothing().when(item1).setDescription((String) any());
        doNothing().when(item1).setId((Long) any());
        doNothing().when(item1).setIsAvailable((Boolean) any());
        doNothing().when(item1).setItemRequest((ItemRequest) any());
        doNothing().when(item1).setName((String) any());
        doNothing().when(item1).setOwner((User) any());
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("Name");
        item1.setOwner(user4);

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(123L);
        user6.setName("Name");
        Booking booking = mock(Booking.class);
        when(booking.getId()).thenReturn(123L);
        when(booking.getEndTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking.getStartTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking.getStatus()).thenReturn(Booking.Status.WAITING);
        when(booking.getBooker()).thenReturn(user6);
        when(booking.getItem()).thenReturn(item1);
        doNothing().when(booking).setBooker((User) any());
        doNothing().when(booking).setEndTime((LocalDateTime) any());
        doNothing().when(booking).setId((Long) any());
        doNothing().when(booking).setItem((Item) any());
        doNothing().when(booking).setStartTime((LocalDateTime) any());
        doNothing().when(booking).setStatus((Booking.Status) any());
        booking.setBooker(user);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        when(bookingRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingRepository.existsById((Long) any())).thenReturn(true);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        bookingServiceImpl.getBookingByIdOnlyForOwnerOrBooker(123L, 123L);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetAllByBookerId() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.booking.exception.IncorrectStateException: Unknown state: Possible State
        //       at ru.practicum.shareit.booking.exception.IncorrectStateException.getFromStringState(IncorrectStateException.java:18)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.checkState(BookingServiceImpl.java:158)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.getAllByBookerId(BookingServiceImpl.java:95)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userRepository.existsById((Long) any())).thenReturn(true);
        bookingServiceImpl.getAllByBookerId(123L, "Possible State", 1, 3);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetAllByBookerId2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.user.exception.UserNotFoundException: USER[ID_123] not found
        //       at ru.practicum.shareit.user.exception.UserNotFoundException.getFromUserId(UserNotFoundException.java:18)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById(UserServiceImpl.java:26)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.getAllByBookerId(BookingServiceImpl.java:94)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userRepository.existsById((Long) any())).thenReturn(false);
        bookingServiceImpl.getAllByBookerId(123L, "Possible State", 1, 3);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetAllByBookerId3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Name is null
        //       at java.lang.Enum.valueOf(Enum.java:238)
        //       at ru.practicum.shareit.booking.service.BookingService$State.valueOf(BookingService.java:9)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.checkState(BookingServiceImpl.java:156)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.getAllByBookerId(BookingServiceImpl.java:95)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userRepository.existsById((Long) any())).thenReturn(true);
        bookingServiceImpl.getAllByBookerId(123L, null, 1, 3);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId4() {
        when(userRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(IncorrectDataException.class,
                () -> bookingServiceImpl.getAllByBookerId(123L, "Possible State", null, 3));
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId5() {
        when(userRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(IncorrectDataException.class,
                () -> bookingServiceImpl.getAllByBookerId(123L, "Possible State", 1, null));
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetAllByBookerItems() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.booking.exception.IncorrectStateException: Unknown state: Possible State
        //       at ru.practicum.shareit.booking.exception.IncorrectStateException.getFromStringState(IncorrectStateException.java:18)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.checkState(BookingServiceImpl.java:158)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.getAllByBookerItems(BookingServiceImpl.java:113)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userRepository.existsById((Long) any())).thenReturn(true);
        bookingServiceImpl.getAllByBookerItems(123L, "Possible State", 1, 3);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetAllByBookerItems2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.user.exception.UserNotFoundException: USER[ID_123] not found
        //       at ru.practicum.shareit.user.exception.UserNotFoundException.getFromUserId(UserNotFoundException.java:18)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById(UserServiceImpl.java:26)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.getAllByBookerItems(BookingServiceImpl.java:112)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userRepository.existsById((Long) any())).thenReturn(false);
        bookingServiceImpl.getAllByBookerItems(123L, "Possible State", 1, 3);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetAllByBookerItems3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Name is null
        //       at java.lang.Enum.valueOf(Enum.java:238)
        //       at ru.practicum.shareit.booking.service.BookingService$State.valueOf(BookingService.java:9)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.checkState(BookingServiceImpl.java:156)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.getAllByBookerItems(BookingServiceImpl.java:113)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userRepository.existsById((Long) any())).thenReturn(true);
        bookingServiceImpl.getAllByBookerItems(123L, null, 1, 3);
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems4() {
        when(userRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(IncorrectDataException.class,
                () -> bookingServiceImpl.getAllByBookerItems(123L, "Possible State", null, 3));
    }

    /**
     * Method under test: {@link BookingServiceImpl#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems5() {
        when(userRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(IncorrectDataException.class,
                () -> bookingServiceImpl.getAllByBookerItems(123L, "Possible State", 1, null));
    }
}

