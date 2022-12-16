package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.in.RequestBookingDto;
import ru.practicum.shareit.booking.dto.out.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {BookingController.class})
@ExtendWith(SpringExtension.class)
class BookingControllerTest {
    @Autowired
    private BookingController bookingController;

    @MockBean
    private BookingService bookingService;

    /**
     * Method under test: {@link BookingController#addBooking(RequestBookingDto, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddBooking() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: ru.practicum.shareit.booking.dto.in.RequestBookingDto["start"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1300)
        //       at com.fasterxml.jackson.databind.ser.impl.UnsupportedTypeSerializer.serialize(UnsupportedTypeSerializer.java:35)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:728)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4568)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3821)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.booking.exception.BookingLogicException: USER[ID_123] cannot book own ITEM[ID_123]
        //       at ru.practicum.shareit.booking.exception.BookingLogicException.getFromOwnerIdAndItemId(BookingLogicException.java:18)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.checkUserNotOwnerByItemIdAndUserId(BookingServiceImpl.java:183)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.addBooking(BookingServiceImpl.java:50)
        //       at ru.practicum.shareit.booking.controller.BookingController.addBooking(BookingController.java:26)
        //   See https://diff.blue/R013 to resolve this issue.

        UserRepository userRepository = mock(UserRepository.class);
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
        ItemRepository itemRepository = mock(ItemRepository.class);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemRepository.existsById((Long) any())).thenReturn(true);
        BookingController bookingController = new BookingController(
                new BookingServiceImpl(mock(BookingRepository.class), userRepository, itemRepository));

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        requestBookingDto.setItemId(123L);
        requestBookingDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        bookingController.addBooking(requestBookingDto, 123L);
    }

    /**
     * Method under test: {@link BookingController#addBooking(RequestBookingDto, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddBooking2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: ru.practicum.shareit.booking.dto.in.RequestBookingDto["start"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1300)
        //       at com.fasterxml.jackson.databind.ser.impl.UnsupportedTypeSerializer.serialize(UnsupportedTypeSerializer.java:35)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:728)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4568)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3821)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.user.exception.UserNotFoundException: USER[ID_123] not found
        //       at ru.practicum.shareit.user.exception.UserNotFoundException.getFromUserId(UserNotFoundException.java:18)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById(UserServiceImpl.java:26)
        //       at ru.practicum.shareit.booking.service.BookingServiceImpl.addBooking(BookingServiceImpl.java:48)
        //       at ru.practicum.shareit.booking.controller.BookingController.addBooking(BookingController.java:26)
        //   See https://diff.blue/R013 to resolve this issue.

        UserRepository userRepository = mock(UserRepository.class);
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
        ItemRepository itemRepository = mock(ItemRepository.class);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemRepository.existsById((Long) any())).thenReturn(true);
        BookingController bookingController = new BookingController(
                new BookingServiceImpl(mock(BookingRepository.class), userRepository, itemRepository));

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        requestBookingDto.setItemId(123L);
        requestBookingDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        bookingController.addBooking(requestBookingDto, 123L);
    }

    /**
     * Method under test: {@link BookingController#addBooking(RequestBookingDto, Long)}
     */
    @Test
    void testAddBooking3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: ru.practicum.shareit.booking.dto.in.RequestBookingDto["start"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1300)
        //       at com.fasterxml.jackson.databind.ser.impl.UnsupportedTypeSerializer.serialize(UnsupportedTypeSerializer.java:35)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:728)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4568)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3821)
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
        BookingRepository bookingRepository = mock(BookingRepository.class);
        when(bookingRepository.save((Booking) any())).thenReturn(booking);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getReferenceById((Long) any())).thenReturn(user3);
        when(userRepository.existsById((Long) any())).thenReturn(true);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user4);
        User user5 = mock(User.class);
        when(user5.getId()).thenReturn(1L);
        doNothing().when(user5).setEmail((String) any());
        doNothing().when(user5).setId((Long) any());
        doNothing().when(user5).setName((String) any());
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
        ItemRepository itemRepository = mock(ItemRepository.class);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item1);
        when(itemRepository.existsById((Long) any())).thenReturn(true);
        BookingController bookingController = new BookingController(
                new BookingServiceImpl(bookingRepository, userRepository, itemRepository));

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        requestBookingDto.setItemId(123L);
        requestBookingDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        BookingDto actualAddBookingResult = bookingController.addBooking(requestBookingDto, 123L);
        assertEquals(Booking.Status.WAITING, actualAddBookingResult.getStatus());
        assertEquals("01:01", actualAddBookingResult.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualAddBookingResult.getStart().toLocalTime().toString());
        assertEquals(123L, actualAddBookingResult.getId().longValue());
        BookingDto.ItemDto item2 = actualAddBookingResult.getItem();
        assertEquals("Name", item2.getName());
        assertEquals(123L, item2.getId().longValue());
        assertEquals(123L, actualAddBookingResult.getBooker().getId().longValue());
        verify(bookingRepository).save((Booking) any());
        verify(userRepository).existsById((Long) any());
        verify(userRepository).getReferenceById((Long) any());
        verify(itemRepository).existsById((Long) any());
        verify(itemRepository, atLeast(1)).getReferenceById((Long) any());
        verify(user5).getId();
        verify(user5).setEmail((String) any());
        verify(user5).setId((Long) any());
        verify(user5).setName((String) any());
    }

    /**
     * Method under test: {@link BookingController#updateBookingStatus(Long, Long, Boolean)}
     */
    @Test
    void testUpdateBookingStatus() throws Exception {
        BookingDto.UserDto userDto = new BookingDto.UserDto();
        userDto.setId(123L);

        BookingDto.ItemDto itemDto = new BookingDto.ItemDto();
        itemDto.setId(123L);
        itemDto.setName("Name");

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(userDto);
        bookingDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        bookingDto.setId(123L);
        bookingDto.setItem(itemDto);
        bookingDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        bookingDto.setStatus(Booking.Status.WAITING);
        when(bookingService.updateBookingStatus((Long) any(), (Boolean) any(), (Long) any())).thenReturn(bookingDto);
        MockHttpServletRequestBuilder patchResult = MockMvcRequestBuilders.patch("/bookings/{bookingId}", 123L);
        MockHttpServletRequestBuilder requestBuilder = patchResult.param("approved", String.valueOf(true))
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"start\":[1,1,1,1,1],\"end\":[1,1,1,1,1],\"status\":\"WAITING\",\"booker\":{\"id\":123},\"item\":{\"id\""
                                        + ":123,\"name\":\"Name\"}}"));
    }

    /**
     * Method under test: {@link BookingController#getAllByBookerId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerId() throws Exception {
        when(bookingService.getAllByBookerId((Long) any(), (String) any(), (Integer) any(), (Integer) any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings")
                .param("state", "foo")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link BookingController#getAllByBookerItems(Long, String, Integer, Integer)}
     */
    @Test
    void testGetAllByBookerItems() throws Exception {
        when(bookingService.getAllByBookerItems((Long) any(), (String) any(), (Integer) any(), (Integer) any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/owner")
                .param("state", "foo")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link BookingController#getBooking(Long, Long)}
     */
    @Test
    void testGetBooking() throws Exception {
        BookingDto.UserDto userDto = new BookingDto.UserDto();
        userDto.setId(123L);

        BookingDto.ItemDto itemDto = new BookingDto.ItemDto();
        itemDto.setId(123L);
        itemDto.setName("Name");

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(userDto);
        bookingDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        bookingDto.setId(123L);
        bookingDto.setItem(itemDto);
        bookingDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        bookingDto.setStatus(Booking.Status.WAITING);
        when(bookingService.getBookingByIdOnlyForOwnerOrBooker((Long) any(), (Long) any())).thenReturn(bookingDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/{bookingId}", 123L)
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"start\":[1,1,1,1,1],\"end\":[1,1,1,1,1],\"status\":\"WAITING\",\"booker\":{\"id\":123},\"item\":{\"id\""
                                        + ":123,\"name\":\"Name\"}}"));
    }
}

