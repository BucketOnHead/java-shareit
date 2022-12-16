package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.in.RequestBookingDto;
import ru.practicum.shareit.booking.dto.out.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class BookingDtoMapperTest {
    /**
     * Method under test: {@link BookingDtoMapper#toBooking(RequestBookingDto, User, Item)}
     */
    @Test
    void testToBooking() {
        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        requestBookingDto.setItemId(123L);
        requestBookingDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));

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
        Booking actualToBookingResult = BookingDtoMapper.toBooking(requestBookingDto, user, item);
        assertSame(user, actualToBookingResult.getBooker());
        assertEquals(Booking.Status.WAITING, actualToBookingResult.getStatus());
        assertSame(item, actualToBookingResult.getItem());
        assertEquals("01:01", actualToBookingResult.getEndTime().toLocalTime().toString());
        assertEquals("01:01", actualToBookingResult.getStartTime().toLocalTime().toString());
    }

    /**
     * Method under test: {@link BookingDtoMapper#toBookingDto(Collection)}
     */
    @Test
    void testToBookingDto() {
        assertTrue(BookingDtoMapper.toBookingDto(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link BookingDtoMapper#toBookingDto(Collection)}
     */
    @Test
    void testToBookingDto2() {
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

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        assertEquals(1, BookingDtoMapper.toBookingDto(bookingList).size());
    }

    /**
     * Method under test: {@link BookingDtoMapper#toBookingDto(Collection)}
     */
    @Test
    void testToBookingDto3() {
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

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking1);
        bookingList.add(booking);
        assertEquals(2, BookingDtoMapper.toBookingDto(bookingList).size());
    }

    /**
     * Method under test: {@link BookingDtoMapper#toBookingDto(Page)}
     */
    @Test
    void testToBookingDto4() {
        PageImpl<Booking> pageImpl = new PageImpl<>(new ArrayList<>());
        assertTrue(BookingDtoMapper.toBookingDto(pageImpl).isEmpty());
        assertTrue(pageImpl.toList().isEmpty());
    }

    /**
     * Method under test: {@link BookingDtoMapper#toBookingDto(Page)}
     */
    @Test
    void testToBookingDto5() {
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

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        PageImpl<Booking> pageImpl = new PageImpl<>(bookingList);
        assertEquals(1, BookingDtoMapper.toBookingDto(pageImpl).size());
        assertEquals(1, pageImpl.toList().size());
    }

    /**
     * Method under test: {@link BookingDtoMapper#toBookingDto(Page)}
     */
    @Test
    void testToBookingDto6() {
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

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking1);
        bookingList.add(booking);
        PageImpl<Booking> pageImpl = new PageImpl<>(bookingList);
        assertEquals(2, BookingDtoMapper.toBookingDto(pageImpl).size());
        assertEquals(2, pageImpl.toList().size());
    }

    /**
     * Method under test: {@link BookingDtoMapper#toBookingDto(Booking)}
     */
    @Test
    void testToBookingDto7() {
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
        BookingDto actualToBookingDtoResult = BookingDtoMapper.toBookingDto(booking);
        assertEquals(Booking.Status.WAITING, actualToBookingDtoResult.getStatus());
        assertEquals("01:01", actualToBookingDtoResult.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualToBookingDtoResult.getStart().toLocalTime().toString());
        assertEquals(123L, actualToBookingDtoResult.getId().longValue());
        BookingDto.ItemDto item1 = actualToBookingDtoResult.getItem();
        assertEquals("Name", item1.getName());
        assertEquals(123L, item1.getId().longValue());
        assertEquals(123L, actualToBookingDtoResult.getBooker().getId().longValue());
    }
}

