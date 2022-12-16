package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.in.RequestItemDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ItemServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ItemServiceImplTest {
    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link ItemServiceImpl#checkItemExistsById(ItemRepository, Long)}
     */
    @Test
    void testCheckItemExistsById() {
        ItemRepository itemRepository1 = mock(ItemRepository.class);
        when(itemRepository1.existsById((Long) any())).thenReturn(true);
        ItemServiceImpl.checkItemExistsById(itemRepository1, 123L);
        verify(itemRepository1).existsById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#checkItemExistsById(ItemRepository, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCheckItemExistsById2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.item.exception.ItemNotFoundException: ITEM[ID_123] not found
        //       at ru.practicum.shareit.item.exception.ItemNotFoundException.getFromItemId(ItemNotFoundException.java:20)
        //       at ru.practicum.shareit.item.service.ItemServiceImpl.checkItemExistsById(ItemServiceImpl.java:46)
        //   See https://diff.blue/R013 to resolve this issue.

        ItemRepository itemRepository1 = mock(ItemRepository.class);
        when(itemRepository1.existsById((Long) any())).thenReturn(false);
        ItemServiceImpl.checkItemExistsById(itemRepository1, 123L);
    }

    /**
     * Method under test: {@link ItemServiceImpl#checkItemExistsById(ItemRepository, Long)}
     */
    @Test
    void testCheckItemExistsById3() {
        ItemRepository itemRepository1 = mock(ItemRepository.class);
        when(itemRepository1.existsById((Long) any())).thenThrow(new ItemNotFoundException("An error occurred"));
        assertThrows(ItemNotFoundException.class, () -> ItemServiceImpl.checkItemExistsById(itemRepository1, 123L));
        verify(itemRepository1).existsById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#checkOwnerOfItemByItemIdAndUserId(ItemRepository, Long, Long)}
     */
    @Test
    void testCheckOwnerOfItemByItemIdAndUserId() {
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
        ItemRepository itemRepository1 = mock(ItemRepository.class);
        when(itemRepository1.getReferenceById((Long) any())).thenReturn(item);
        ItemServiceImpl.checkOwnerOfItemByItemIdAndUserId(itemRepository1, 123L, 123L);
        verify(itemRepository1).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#checkOwnerOfItemByItemIdAndUserId(ItemRepository, Long, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCheckOwnerOfItemByItemIdAndUserId2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.item.exception.ItemNotFoundException: ITEM[ID_123] with owner-USER[ID_123] not found
        //       at ru.practicum.shareit.item.exception.ItemNotFoundException.getFromItemIdAndUserId(ItemNotFoundException.java:25)
        //       at ru.practicum.shareit.item.service.ItemServiceImpl.checkOwnerOfItemByItemIdAndUserId(ItemServiceImpl.java:55)
        //   See https://diff.blue/R013 to resolve this issue.

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
        User user2 = mock(User.class);
        when(user2.getId()).thenReturn(1L);
        doNothing().when(user2).setEmail((String) any());
        doNothing().when(user2).setId((Long) any());
        doNothing().when(user2).setName((String) any());
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");
        Item item = mock(Item.class);
        when(item.getOwner()).thenReturn(user2);
        doNothing().when(item).setDescription((String) any());
        doNothing().when(item).setId((Long) any());
        doNothing().when(item).setIsAvailable((Boolean) any());
        doNothing().when(item).setItemRequest((ItemRequest) any());
        doNothing().when(item).setName((String) any());
        doNothing().when(item).setOwner((User) any());
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user1);
        ItemRepository itemRepository1 = mock(ItemRepository.class);
        when(itemRepository1.getReferenceById((Long) any())).thenReturn(item);
        ItemServiceImpl.checkOwnerOfItemByItemIdAndUserId(itemRepository1, 123L, 123L);
    }

    /**
     * Method under test: {@link ItemServiceImpl#addItem(RequestItemDto, Long)}
     */
    @Test
    void testAddItem() {
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
        when(itemRepository.save((Item) any())).thenReturn(item);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");
        when(userRepository.getReferenceById((Long) any())).thenReturn(user2);
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
        when(itemRequestRepository.getReferenceById((Long) any())).thenReturn(itemRequest1);
        when(itemRequestRepository.existsById((Long) any())).thenReturn(true);

        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setAvailable(true);
        requestItemDto.setDescription("The characteristics of someone or something");
        requestItemDto.setName("Name");
        requestItemDto.setRequestId(123L);
        ItemDto actualAddItemResult = itemServiceImpl.addItem(requestItemDto, 123L);
        assertTrue(actualAddItemResult.getAvailable());
        assertEquals(123L, actualAddItemResult.getRequestId().longValue());
        assertEquals("Name", actualAddItemResult.getName());
        assertEquals(123L, actualAddItemResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualAddItemResult.getDescription());
        verify(itemRepository).save((Item) any());
        verify(userRepository).existsById((Long) any());
        verify(userRepository).getReferenceById((Long) any());
        verify(itemRequestRepository).existsById((Long) any());
        verify(itemRequestRepository).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#addItem(RequestItemDto, Long)}
     */
    @Test
    void testAddItem2() {
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
        when(itemRepository.save((Item) any())).thenReturn(item);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");
        when(userRepository.getReferenceById((Long) any())).thenReturn(user2);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(itemRequestRepository.getReferenceById((Long) any()))
                .thenThrow(new ItemNotFoundException("An error occurred"));
        when(itemRequestRepository.existsById((Long) any())).thenReturn(true);

        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setAvailable(true);
        requestItemDto.setDescription("The characteristics of someone or something");
        requestItemDto.setName("Name");
        requestItemDto.setRequestId(123L);
        assertThrows(ItemNotFoundException.class, () -> itemServiceImpl.addItem(requestItemDto, 123L));
        verify(userRepository).existsById((Long) any());
        verify(userRepository).getReferenceById((Long) any());
        verify(itemRequestRepository).existsById((Long) any());
        verify(itemRequestRepository).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#addItem(RequestItemDto, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddItem3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.user.exception.UserNotFoundException: USER[ID_123] not found
        //       at ru.practicum.shareit.user.exception.UserNotFoundException.getFromUserId(UserNotFoundException.java:18)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById(UserServiceImpl.java:26)
        //       at ru.practicum.shareit.item.service.ItemServiceImpl.addItem(ItemServiceImpl.java:62)
        //   See https://diff.blue/R013 to resolve this issue.

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
        when(itemRepository.save((Item) any())).thenReturn(item);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");
        when(userRepository.getReferenceById((Long) any())).thenReturn(user2);
        when(userRepository.existsById((Long) any())).thenReturn(false);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user3);
        when(itemRequestRepository.getReferenceById((Long) any())).thenReturn(itemRequest1);
        when(itemRequestRepository.existsById((Long) any())).thenReturn(true);

        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setAvailable(true);
        requestItemDto.setDescription("The characteristics of someone or something");
        requestItemDto.setName("Name");
        requestItemDto.setRequestId(123L);
        itemServiceImpl.addItem(requestItemDto, 123L);
    }

    /**
     * Method under test: {@link ItemServiceImpl#addItem(RequestItemDto, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddItem4() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.request.exception.ItemRequestNotFoundException: ITEM_REQUEST[ID_123] not found
        //       at ru.practicum.shareit.request.exception.ItemRequestNotFoundException.getFromId(ItemRequestNotFoundException.java:18)
        //       at ru.practicum.shareit.request.service.ItemRequestServiceImpl.checkItemRequestExistsById(ItemRequestServiceImpl.java:39)
        //       at ru.practicum.shareit.item.service.ItemServiceImpl.checkItemRequestExistsById(ItemServiceImpl.java:144)
        //       at ru.practicum.shareit.item.service.ItemServiceImpl.addItem(ItemServiceImpl.java:63)
        //   See https://diff.blue/R013 to resolve this issue.

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
        when(itemRepository.save((Item) any())).thenReturn(item);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");
        when(userRepository.getReferenceById((Long) any())).thenReturn(user2);
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
        when(itemRequestRepository.getReferenceById((Long) any())).thenReturn(itemRequest1);
        when(itemRequestRepository.existsById((Long) any())).thenReturn(false);

        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setAvailable(true);
        requestItemDto.setDescription("The characteristics of someone or something");
        requestItemDto.setName("Name");
        requestItemDto.setRequestId(123L);
        itemServiceImpl.addItem(requestItemDto, 123L);
    }

    /**
     * Method under test: {@link ItemServiceImpl#addItem(RequestItemDto, Long)}
     */
    @Test
    void testAddItem5() {
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
        when(itemRepository.save((Item) any())).thenReturn(item);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");
        when(userRepository.getReferenceById((Long) any())).thenReturn(user2);
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
        when(itemRequestRepository.getReferenceById((Long) any())).thenReturn(itemRequest1);
        when(itemRequestRepository.existsById((Long) any())).thenReturn(true);

        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setAvailable(true);
        requestItemDto.setDescription("The characteristics of someone or something");
        requestItemDto.setName("Name");
        requestItemDto.setRequestId(null);
        ItemDto actualAddItemResult = itemServiceImpl.addItem(requestItemDto, 123L);
        assertTrue(actualAddItemResult.getAvailable());
        assertEquals("Name", actualAddItemResult.getName());
        assertEquals(123L, actualAddItemResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualAddItemResult.getDescription());
        verify(itemRepository).save((Item) any());
        verify(userRepository).existsById((Long) any());
        verify(userRepository).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemById(Long, Long)}
     */
    @Test
    void testGetItemById() {
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
        ArrayList<Comment> commentList = new ArrayList<>();
        when(commentRepository.findAllByItemId((Long) any())).thenReturn(commentList);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

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

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item1);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");

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

        Booking booking1 = new Booking();
        booking1.setBooker(user5);
        booking1.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(123L);
        booking1.setItem(item2);
        booking1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingRepository.findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);
        DetailedItemDto actualItemById = itemServiceImpl.getItemById(123L, 123L);
        assertTrue(actualItemById.getAvailable());
        assertEquals("Name", actualItemById.getName());
        assertEquals(commentList, actualItemById.getComments());
        assertEquals(123L, actualItemById.getId().longValue());
        assertEquals("The characteristics of someone or something", actualItemById.getDescription());
        DetailedItemDto.BookingDto lastBooking = actualItemById.getLastBooking();
        assertEquals(123L, lastBooking.getId().longValue());
        assertEquals(123L, lastBooking.getBookerId().longValue());
        DetailedItemDto.BookingDto nextBooking = actualItemById.getNextBooking();
        assertEquals(123L, nextBooking.getId().longValue());
        assertEquals(123L, nextBooking.getBookerId().longValue());
        verify(itemRepository).existsById((Long) any());
        verify(itemRepository).getReferenceById((Long) any());
        verify(commentRepository).findAllByItemId((Long) any());
        verify(bookingRepository).findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(),
                (LocalDateTime) any());
        verify(bookingRepository).findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemById(Long, Long)}
     */
    @Test
    void testGetItemById2() {
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
        when(commentRepository.findAllByItemId((Long) any())).thenReturn(new ArrayList<>());
        when(bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(), (LocalDateTime) any()))
                .thenThrow(new ItemNotFoundException("An error occurred"));
        when(bookingRepository.findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any()))
                .thenThrow(new ItemNotFoundException("An error occurred"));
        assertThrows(ItemNotFoundException.class, () -> itemServiceImpl.getItemById(123L, 123L));
        verify(itemRepository).existsById((Long) any());
        verify(itemRepository).getReferenceById((Long) any());
        verify(commentRepository).findAllByItemId((Long) any());
        verify(bookingRepository).findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(),
                (LocalDateTime) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemById(Long, Long)}
     */
    @Test
    void testGetItemById3() {
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

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");
        Item item = mock(Item.class);
        when(item.getIsAvailable()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getId()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getDescription()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getName()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getOwner()).thenReturn(user2);
        doNothing().when(item).setDescription((String) any());
        doNothing().when(item).setId((Long) any());
        doNothing().when(item).setIsAvailable((Boolean) any());
        doNothing().when(item).setItemRequest((ItemRequest) any());
        doNothing().when(item).setName((String) any());
        doNothing().when(item).setOwner((User) any());
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user1);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemRepository.existsById((Long) any())).thenReturn(true);
        when(commentRepository.findAllByItemId((Long) any())).thenReturn(new ArrayList<>());

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

        Booking booking = new Booking();
        booking.setBooker(user3);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item1);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);

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

        Booking booking1 = new Booking();
        booking1.setBooker(user6);
        booking1.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(123L);
        booking1.setItem(item2);
        booking1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingRepository.findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);
        assertThrows(ItemNotFoundException.class, () -> itemServiceImpl.getItemById(123L, 123L));
        verify(itemRepository).existsById((Long) any());
        verify(itemRepository).getReferenceById((Long) any());
        verify(item).getId();
        verify(item).getOwner();
        verify(item).setDescription((String) any());
        verify(item).setId((Long) any());
        verify(item).setIsAvailable((Boolean) any());
        verify(item).setItemRequest((ItemRequest) any());
        verify(item).setName((String) any());
        verify(item).setOwner((User) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemById(Long, Long)}
     */
    @Test
    void testGetItemById4() {
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
        User user2 = mock(User.class);
        when(user2.getId()).thenReturn(1L);
        doNothing().when(user2).setEmail((String) any());
        doNothing().when(user2).setId((Long) any());
        doNothing().when(user2).setName((String) any());
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");
        Item item = mock(Item.class);
        when(item.getIsAvailable()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getId()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getDescription()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getName()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getOwner()).thenReturn(user2);
        doNothing().when(item).setDescription((String) any());
        doNothing().when(item).setId((Long) any());
        doNothing().when(item).setIsAvailable((Boolean) any());
        doNothing().when(item).setItemRequest((ItemRequest) any());
        doNothing().when(item).setName((String) any());
        doNothing().when(item).setOwner((User) any());
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user1);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemRepository.existsById((Long) any())).thenReturn(true);
        when(commentRepository.findAllByItemId((Long) any())).thenReturn(new ArrayList<>());

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

        Booking booking = new Booking();
        booking.setBooker(user3);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item1);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);

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

        Booking booking1 = new Booking();
        booking1.setBooker(user6);
        booking1.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(123L);
        booking1.setItem(item2);
        booking1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingRepository.findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);
        assertThrows(ItemNotFoundException.class, () -> itemServiceImpl.getItemById(123L, 123L));
        verify(itemRepository).existsById((Long) any());
        verify(itemRepository).getReferenceById((Long) any());
        verify(item).getId();
        verify(item).getOwner();
        verify(item).setDescription((String) any());
        verify(item).setId((Long) any());
        verify(item).setIsAvailable((Boolean) any());
        verify(item).setItemRequest((ItemRequest) any());
        verify(item).setName((String) any());
        verify(item).setOwner((User) any());
        verify(user2).getId();
        verify(user2).setEmail((String) any());
        verify(user2).setId((Long) any());
        verify(user2).setName((String) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemById(Long, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetItemById5() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.item.exception.ItemNotFoundException: ITEM[ID_123] not found
        //       at ru.practicum.shareit.item.exception.ItemNotFoundException.getFromItemId(ItemNotFoundException.java:20)
        //       at ru.practicum.shareit.item.service.ItemServiceImpl.checkItemExistsById(ItemServiceImpl.java:46)
        //       at ru.practicum.shareit.item.service.ItemServiceImpl.getItemById(ItemServiceImpl.java:96)
        //   See https://diff.blue/R013 to resolve this issue.

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
        User user2 = mock(User.class);
        when(user2.getId()).thenReturn(123L);
        doNothing().when(user2).setEmail((String) any());
        doNothing().when(user2).setId((Long) any());
        doNothing().when(user2).setName((String) any());
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");
        Item item = mock(Item.class);
        when(item.getIsAvailable()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getId()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getDescription()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getName()).thenThrow(new ItemNotFoundException("An error occurred"));
        when(item.getOwner()).thenReturn(user2);
        doNothing().when(item).setDescription((String) any());
        doNothing().when(item).setId((Long) any());
        doNothing().when(item).setIsAvailable((Boolean) any());
        doNothing().when(item).setItemRequest((ItemRequest) any());
        doNothing().when(item).setName((String) any());
        doNothing().when(item).setOwner((User) any());
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user1);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemRepository.existsById((Long) any())).thenReturn(false);
        when(commentRepository.findAllByItemId((Long) any())).thenReturn(new ArrayList<>());

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

        Booking booking = new Booking();
        booking.setBooker(user3);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item1);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);

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

        Booking booking1 = new Booking();
        booking1.setBooker(user6);
        booking1.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(123L);
        booking1.setItem(item2);
        booking1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingRepository.findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);
        itemServiceImpl.getItemById(123L, 123L);
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemsByOwnerId(Long)}
     */
    @Test
    void testGetItemsByOwnerId() {
        when(itemRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.getItemsByOwnerId(123L).isEmpty());
        verify(itemRepository).findAllByOwnerId((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemsByOwnerId(Long)}
     */
    @Test
    void testGetItemsByOwnerId2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("All ITEM<DTO> returned, {} in total.");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("All ITEM<DTO> returned, {} in total.");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("All ITEM<DTO> returned, {} in total.");
        item.setOwner(user1);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findAllByOwnerId((Long) any())).thenReturn(itemList);
        when(commentRepository.findAllByItemId((Long) any())).thenReturn(new ArrayList<>());

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

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

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item1);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");

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

        Booking booking1 = new Booking();
        booking1.setBooker(user5);
        booking1.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(123L);
        booking1.setItem(item2);
        booking1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingRepository.findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);
        assertEquals(1, itemServiceImpl.getItemsByOwnerId(123L).size());
        verify(itemRepository).findAllByOwnerId((Long) any());
        verify(commentRepository).findAllByItemId((Long) any());
        verify(bookingRepository).findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(),
                (LocalDateTime) any());
        verify(bookingRepository).findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemsByOwnerId(Long)}
     */
    @Test
    void testGetItemsByOwnerId3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("All ITEM<DTO> returned, {} in total.");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("All ITEM<DTO> returned, {} in total.");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("All ITEM<DTO> returned, {} in total.");
        item.setOwner(user1);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findAllByOwnerId((Long) any())).thenReturn(itemList);
        when(commentRepository.findAllByItemId((Long) any())).thenReturn(new ArrayList<>());
        when(bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(), (LocalDateTime) any()))
                .thenThrow(new ItemNotFoundException("An error occurred"));
        when(bookingRepository.findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any()))
                .thenThrow(new ItemNotFoundException("An error occurred"));
        assertThrows(ItemNotFoundException.class, () -> itemServiceImpl.getItemsByOwnerId(123L));
        verify(itemRepository).findAllByOwnerId((Long) any());
        verify(commentRepository).findAllByItemId((Long) any());
        verify(bookingRepository).findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(),
                (LocalDateTime) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemsByOwnerId(Long)}
     */
    @Test
    void testGetItemsByOwnerId4() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("All ITEM<DTO> returned, {} in total.");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("All ITEM<DTO> returned, {} in total.");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("All ITEM<DTO> returned, {} in total.");
        item.setOwner(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("All ITEM<DTO> returned, {} in total.");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user2);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("All ITEM<DTO> returned, {} in total.");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("All ITEM<DTO> returned, {} in total.");
        item1.setOwner(user3);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item);
        when(itemRepository.findAllByOwnerId((Long) any())).thenReturn(itemList);
        when(commentRepository.findAllByItemId((Long) any())).thenReturn(new ArrayList<>());

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest2.setDescription("The characteristics of someone or something");
        itemRequest2.setId(123L);
        itemRequest2.setRequester(user5);

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(123L);
        user6.setName("Name");

        Item item2 = new Item();
        item2.setDescription("The characteristics of someone or something");
        item2.setId(123L);
        item2.setIsAvailable(true);
        item2.setItemRequest(itemRequest2);
        item2.setName("Name");
        item2.setOwner(user6);

        Booking booking = new Booking();
        booking.setBooker(user4);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item2);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);

        User user7 = new User();
        user7.setEmail("jane.doe@example.org");
        user7.setId(123L);
        user7.setName("Name");

        User user8 = new User();
        user8.setEmail("jane.doe@example.org");
        user8.setId(123L);
        user8.setName("Name");

        ItemRequest itemRequest3 = new ItemRequest();
        itemRequest3.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest3.setDescription("The characteristics of someone or something");
        itemRequest3.setId(123L);
        itemRequest3.setRequester(user8);

        User user9 = new User();
        user9.setEmail("jane.doe@example.org");
        user9.setId(123L);
        user9.setName("Name");

        Item item3 = new Item();
        item3.setDescription("The characteristics of someone or something");
        item3.setId(123L);
        item3.setIsAvailable(true);
        item3.setItemRequest(itemRequest3);
        item3.setName("Name");
        item3.setOwner(user9);

        Booking booking1 = new Booking();
        booking1.setBooker(user7);
        booking1.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(123L);
        booking1.setItem(item3);
        booking1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingRepository.findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);
        assertEquals(2, itemServiceImpl.getItemsByOwnerId(123L).size());
        verify(itemRepository).findAllByOwnerId((Long) any());
        verify(commentRepository, atLeast(1)).findAllByItemId((Long) any());
        verify(bookingRepository, atLeast(1)).findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(),
                (LocalDateTime) any());
        verify(bookingRepository, atLeast(1)).findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemsByOwnerId(Long)}
     */
    @Test
    void testGetItemsByOwnerId5() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("All ITEM<DTO> returned, {} in total.");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("All ITEM<DTO> returned, {} in total.");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("All ITEM<DTO> returned, {} in total.");
        item.setOwner(user1);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findAllByOwnerId((Long) any())).thenReturn(itemList);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("All ITEM<DTO> returned, {} in total.");

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("All ITEM<DTO> returned, {} in total.");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user3);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("All ITEM<DTO> returned, {} in total.");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("All ITEM<DTO> returned, {} in total.");
        item1.setOwner(user4);

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item1);
        comment.setText("All ITEM<DTO> returned, {} in total.");

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        when(commentRepository.findAllByItemId((Long) any())).thenReturn(commentList);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");

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

        Booking booking = new Booking();
        booking.setBooker(user5);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item2);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);

        User user8 = new User();
        user8.setEmail("jane.doe@example.org");
        user8.setId(123L);
        user8.setName("Name");

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

        Booking booking1 = new Booking();
        booking1.setBooker(user8);
        booking1.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(123L);
        booking1.setItem(item3);
        booking1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingRepository.findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);
        assertEquals(1, itemServiceImpl.getItemsByOwnerId(123L).size());
        verify(itemRepository).findAllByOwnerId((Long) any());
        verify(commentRepository).findAllByItemId((Long) any());
        verify(bookingRepository).findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(),
                (LocalDateTime) any());
        verify(bookingRepository).findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemsByOwnerId(Long)}
     */
    @Test
    void testGetItemsByOwnerId6() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("All ITEM<DTO> returned, {} in total.");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("All ITEM<DTO> returned, {} in total.");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("All ITEM<DTO> returned, {} in total.");
        item.setOwner(user1);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findAllByOwnerId((Long) any())).thenReturn(itemList);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("All ITEM<DTO> returned, {} in total.");

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("All ITEM<DTO> returned, {} in total.");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user3);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("All ITEM<DTO> returned, {} in total.");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("All ITEM<DTO> returned, {} in total.");
        item1.setOwner(user4);

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item1);
        comment.setText("All ITEM<DTO> returned, {} in total.");

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("All ITEM<DTO> returned, {} in total.");

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(123L);
        user6.setName("All ITEM<DTO> returned, {} in total.");

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest2.setDescription("The characteristics of someone or something");
        itemRequest2.setId(123L);
        itemRequest2.setRequester(user6);

        User user7 = new User();
        user7.setEmail("jane.doe@example.org");
        user7.setId(123L);
        user7.setName("All ITEM<DTO> returned, {} in total.");

        Item item2 = new Item();
        item2.setDescription("The characteristics of someone or something");
        item2.setId(123L);
        item2.setIsAvailable(true);
        item2.setItemRequest(itemRequest2);
        item2.setName("All ITEM<DTO> returned, {} in total.");
        item2.setOwner(user7);

        Comment comment1 = new Comment();
        comment1.setAuthor(user5);
        comment1.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment1.setId(123L);
        comment1.setItem(item2);
        comment1.setText("All ITEM<DTO> returned, {} in total.");

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment);
        when(commentRepository.findAllByItemId((Long) any())).thenReturn(commentList);

        User user8 = new User();
        user8.setEmail("jane.doe@example.org");
        user8.setId(123L);
        user8.setName("Name");

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

        Booking booking = new Booking();
        booking.setBooker(user8);
        booking.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(123L);
        booking.setItem(item3);
        booking.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);

        User user11 = new User();
        user11.setEmail("jane.doe@example.org");
        user11.setId(123L);
        user11.setName("Name");

        User user12 = new User();
        user12.setEmail("jane.doe@example.org");
        user12.setId(123L);
        user12.setName("Name");

        ItemRequest itemRequest4 = new ItemRequest();
        itemRequest4.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest4.setDescription("The characteristics of someone or something");
        itemRequest4.setId(123L);
        itemRequest4.setRequester(user12);

        User user13 = new User();
        user13.setEmail("jane.doe@example.org");
        user13.setId(123L);
        user13.setName("Name");

        Item item4 = new Item();
        item4.setDescription("The characteristics of someone or something");
        item4.setId(123L);
        item4.setIsAvailable(true);
        item4.setItemRequest(itemRequest4);
        item4.setName("Name");
        item4.setOwner(user13);

        Booking booking1 = new Booking();
        booking1.setBooker(user11);
        booking1.setEndTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(123L);
        booking1.setItem(item4);
        booking1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingRepository.findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);
        assertEquals(1, itemServiceImpl.getItemsByOwnerId(123L).size());
        verify(itemRepository).findAllByOwnerId((Long) any());
        verify(commentRepository).findAllByItemId((Long) any());
        verify(bookingRepository).findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc((Long) any(),
                (LocalDateTime) any());
        verify(bookingRepository).findFirstByItemIdAndStartTimeIsAfter((Long) any(), (LocalDateTime) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#searchItemsByNameOrDescription(String)}
     */
    @Test
    void testSearchItemsByNameOrDescription() {
        when(itemRepository.findAllByIsAvailableIsTrue()).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.searchItemsByNameOrDescription("Text").isEmpty());
        verify(itemRepository).findAllByIsAvailableIsTrue();
    }

    /**
     * Method under test: {@link ItemServiceImpl#searchItemsByNameOrDescription(String)}
     */
    @Test
    void testSearchItemsByNameOrDescription2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("All ITEM<DTO> containing '{}' returned, {} in total.");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("All ITEM<DTO> containing '{}' returned, {} in total.");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("All ITEM<DTO> containing '{}' returned, {} in total.");
        item.setOwner(user1);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findAllByIsAvailableIsTrue()).thenReturn(itemList);
        assertTrue(itemServiceImpl.searchItemsByNameOrDescription("Text").isEmpty());
        verify(itemRepository).findAllByIsAvailableIsTrue();
    }

    /**
     * Method under test: {@link ItemServiceImpl#searchItemsByNameOrDescription(String)}
     */
    @Test
    void testSearchItemsByNameOrDescription3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("All ITEM<DTO> containing '{}' returned, {} in total.");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("All ITEM<DTO> containing '{}' returned, {} in total.");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("All ITEM<DTO> containing '{}' returned, {} in total.");
        item.setOwner(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("All ITEM<DTO> containing '{}' returned, {} in total.");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(4, 4, 4, 4, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user2);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("All ITEM<DTO> containing '{}' returned, {} in total.");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("All ITEM<DTO> containing '{}' returned, {} in total.");
        item1.setOwner(user3);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item);
        when(itemRepository.findAllByIsAvailableIsTrue()).thenReturn(itemList);
        assertTrue(itemServiceImpl.searchItemsByNameOrDescription("Text").isEmpty());
        verify(itemRepository).findAllByIsAvailableIsTrue();
    }

    /**
     * Method under test: {@link ItemServiceImpl#searchItemsByNameOrDescription(String)}
     */
    @Test
    void testSearchItemsByNameOrDescription4() {
        when(itemRepository.findAllByIsAvailableIsTrue()).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.searchItemsByNameOrDescription("").isEmpty());
    }

    /**
     * Method under test: {@link ItemServiceImpl#searchItemsByNameOrDescription(String)}
     */
    @Test
    void testSearchItemsByNameOrDescription5() {
        when(itemRepository.findAllByIsAvailableIsTrue()).thenThrow(new ItemNotFoundException("An error occurred"));
        assertThrows(ItemNotFoundException.class, () -> itemServiceImpl.searchItemsByNameOrDescription("Text"));
        verify(itemRepository).findAllByIsAvailableIsTrue();
    }
}

