package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.in.RequestItemDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoMapperTest {
    /**
     * Method under test: {@link ItemDtoMapper#toItemWithoutItemRequest(RequestItemDto, User)}
     */
    @Test
    void testToItemWithoutItemRequest() {
        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setAvailable(true);
        requestItemDto.setDescription("The characteristics of someone or something");
        requestItemDto.setName("Name");
        requestItemDto.setRequestId(123L);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");
        Item actualToItemWithoutItemRequestResult = ItemDtoMapper.toItemWithoutItemRequest(requestItemDto, user);
        assertEquals("The characteristics of someone or something", actualToItemWithoutItemRequestResult.getDescription());
        assertSame(user, actualToItemWithoutItemRequestResult.getOwner());
        assertEquals("Name", actualToItemWithoutItemRequestResult.getName());
        assertTrue(actualToItemWithoutItemRequestResult.getIsAvailable());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toItem(RequestItemDto, User, ItemRequest)}
     */
    @Test
    void testToItem() {
        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setAvailable(true);
        requestItemDto.setDescription("The characteristics of someone or something");
        requestItemDto.setName("Name");
        requestItemDto.setRequestId(123L);

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
        Item actualToItemResult = ItemDtoMapper.toItem(requestItemDto, user, itemRequest);
        assertEquals("The characteristics of someone or something", actualToItemResult.getDescription());
        assertSame(user, actualToItemResult.getOwner());
        assertEquals("Name", actualToItemResult.getName());
        assertSame(itemRequest, actualToItemResult.getItemRequest());
        assertTrue(actualToItemResult.getIsAvailable());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toItemDto(Collection)}
     */
    @Test
    void testToItemDto() {
        assertTrue(ItemDtoMapper.toItemDto(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toItemDto(Collection)}
     */
    @Test
    void testToItemDto2() {
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

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        List<ItemDto> actualToItemDtoResult = ItemDtoMapper.toItemDto(itemList);
        assertEquals(1, actualToItemDtoResult.size());
        ItemDto getResult = actualToItemDtoResult.get(0);
        assertTrue(getResult.getAvailable());
        assertEquals("Name", getResult.getName());
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toItemDto(Collection)}
     */
    @Test
    void testToItemDto3() {
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

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user2);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("Name");
        item1.setOwner(user3);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item);
        List<ItemDto> actualToItemDtoResult = ItemDtoMapper.toItemDto(itemList);
        assertEquals(2, actualToItemDtoResult.size());
        ItemDto getResult = actualToItemDtoResult.get(1);
        assertEquals("Name", getResult.getName());
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertTrue(getResult.getAvailable());
        ItemDto getResult1 = actualToItemDtoResult.get(0);
        assertEquals("Name", getResult1.getName());
        assertEquals(123L, getResult1.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult1.getDescription());
        assertTrue(getResult1.getAvailable());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toItemDto(Item, Long)}
     */
    @Test
    void testToItemDto4() {
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
        ItemDto actualToItemDtoResult = ItemDtoMapper.toItemDto(item, 123L);
        assertTrue(actualToItemDtoResult.getAvailable());
        assertEquals(123L, actualToItemDtoResult.getRequestId().longValue());
        assertEquals("Name", actualToItemDtoResult.getName());
        assertEquals(123L, actualToItemDtoResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToItemDtoResult.getDescription());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toItemDtoWithoutItemRequestId(Item)}
     */
    @Test
    void testToItemDtoWithoutItemRequestId() {
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
        ItemDto actualToItemDtoWithoutItemRequestIdResult = ItemDtoMapper.toItemDtoWithoutItemRequestId(item);
        assertTrue(actualToItemDtoWithoutItemRequestIdResult.getAvailable());
        assertEquals("Name", actualToItemDtoWithoutItemRequestIdResult.getName());
        assertEquals(123L, actualToItemDtoWithoutItemRequestIdResult.getId().longValue());
        assertEquals("The characteristics of someone or something",
                actualToItemDtoWithoutItemRequestIdResult.getDescription());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDto(Item, List, Booking, Booking)}
     */
    @Test
    void testToDetailedItemDto() {
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
        ArrayList<Comment> commentList = new ArrayList<>();

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
        DetailedItemDto actualToDetailedItemDtoResult = ItemDtoMapper.toDetailedItemDto(item, commentList, booking,
                booking1);
        assertTrue(actualToDetailedItemDtoResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoResult.getName());
        assertEquals(commentList, actualToDetailedItemDtoResult.getComments());
        assertEquals(123L, actualToDetailedItemDtoResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToDetailedItemDtoResult.getDescription());
        DetailedItemDto.BookingDto lastBooking = actualToDetailedItemDtoResult.getLastBooking();
        assertEquals(123L, lastBooking.getId().longValue());
        assertEquals(123L, lastBooking.getBookerId().longValue());
        DetailedItemDto.BookingDto nextBooking = actualToDetailedItemDtoResult.getNextBooking();
        assertEquals(123L, nextBooking.getId().longValue());
        assertEquals(123L, nextBooking.getBookerId().longValue());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDto(Item, List, Booking, Booking)}
     */
    @Test
    void testToDetailedItemDto2() {
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

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item1);
        comment.setText("Text");

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);

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
        DetailedItemDto actualToDetailedItemDtoResult = ItemDtoMapper.toDetailedItemDto(item, commentList, booking,
                booking1);
        assertTrue(actualToDetailedItemDtoResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoResult.getName());
        List<DetailedItemDto.CommentDto> comments = actualToDetailedItemDtoResult.getComments();
        assertEquals(1, comments.size());
        assertEquals(123L, actualToDetailedItemDtoResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToDetailedItemDtoResult.getDescription());
        DetailedItemDto.BookingDto lastBooking = actualToDetailedItemDtoResult.getLastBooking();
        assertEquals(123L, lastBooking.getId().longValue());
        assertEquals(123L, lastBooking.getBookerId().longValue());
        DetailedItemDto.BookingDto nextBooking = actualToDetailedItemDtoResult.getNextBooking();
        assertEquals(123L, nextBooking.getId().longValue());
        assertEquals(123L, nextBooking.getBookerId().longValue());
        DetailedItemDto.CommentDto getResult = comments.get(0);
        assertEquals("Name", getResult.getAuthorName());
        assertEquals("Text", getResult.getText());
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("0001-01-01", getResult.getCreated().toLocalDate().toString());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDto(Item, List, Booking, Booking)}
     */
    @Test
    void testToDetailedItemDto3() {
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

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item1);
        comment.setText("Text");

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

        Comment comment1 = new Comment();
        comment1.setAuthor(user5);
        comment1.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment1.setId(123L);
        comment1.setItem(item2);
        comment1.setText("Text");

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment);

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
        DetailedItemDto actualToDetailedItemDtoResult = ItemDtoMapper.toDetailedItemDto(item, commentList, booking,
                booking1);
        assertTrue(actualToDetailedItemDtoResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoResult.getName());
        List<DetailedItemDto.CommentDto> comments = actualToDetailedItemDtoResult.getComments();
        assertEquals(2, comments.size());
        assertEquals(123L, actualToDetailedItemDtoResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToDetailedItemDtoResult.getDescription());
        DetailedItemDto.BookingDto lastBooking = actualToDetailedItemDtoResult.getLastBooking();
        assertEquals(123L, lastBooking.getId().longValue());
        assertEquals(123L, lastBooking.getBookerId().longValue());
        DetailedItemDto.BookingDto nextBooking = actualToDetailedItemDtoResult.getNextBooking();
        assertEquals(123L, nextBooking.getBookerId().longValue());
        assertEquals(123L, nextBooking.getId().longValue());
        DetailedItemDto.CommentDto getResult = comments.get(0);
        assertEquals("Text", getResult.getText());
        DetailedItemDto.CommentDto getResult1 = comments.get(1);
        assertEquals("Text", getResult1.getText());
        assertEquals(123L, getResult1.getId().longValue());
        assertEquals("Name", getResult1.getAuthorName());
        assertEquals("01:01", getResult1.getCreated().toLocalTime().toString());
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("Name", getResult.getAuthorName());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDtoWithoutBookings(Item, List)}
     */
    @Test
    void testToDetailedItemDtoWithoutBookings() {
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
        ArrayList<Comment> commentList = new ArrayList<>();
        DetailedItemDto actualToDetailedItemDtoWithoutBookingsResult = ItemDtoMapper
                .toDetailedItemDtoWithoutBookings(item, commentList);
        assertTrue(actualToDetailedItemDtoWithoutBookingsResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoWithoutBookingsResult.getName());
        assertEquals(123L, actualToDetailedItemDtoWithoutBookingsResult.getId().longValue());
        assertEquals("The characteristics of someone or something",
                actualToDetailedItemDtoWithoutBookingsResult.getDescription());
        assertEquals(commentList, actualToDetailedItemDtoWithoutBookingsResult.getComments());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDtoWithoutBookings(Item, List)}
     */
    @Test
    void testToDetailedItemDtoWithoutBookings2() {
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

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item1);
        comment.setText("Text");

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        DetailedItemDto actualToDetailedItemDtoWithoutBookingsResult = ItemDtoMapper
                .toDetailedItemDtoWithoutBookings(item, commentList);
        assertTrue(actualToDetailedItemDtoWithoutBookingsResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoWithoutBookingsResult.getName());
        assertEquals(123L, actualToDetailedItemDtoWithoutBookingsResult.getId().longValue());
        assertEquals("The characteristics of someone or something",
                actualToDetailedItemDtoWithoutBookingsResult.getDescription());
        List<DetailedItemDto.CommentDto> comments = actualToDetailedItemDtoWithoutBookingsResult.getComments();
        assertEquals(1, comments.size());
        DetailedItemDto.CommentDto getResult = comments.get(0);
        assertEquals("Name", getResult.getAuthorName());
        assertEquals("Text", getResult.getText());
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("0001-01-01", getResult.getCreated().toLocalDate().toString());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDtoWithoutBookings(Item, List)}
     */
    @Test
    void testToDetailedItemDtoWithoutBookings3() {
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

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item1);
        comment.setText("Text");

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

        Comment comment1 = new Comment();
        comment1.setAuthor(user5);
        comment1.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment1.setId(123L);
        comment1.setItem(item2);
        comment1.setText("Text");

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment);
        DetailedItemDto actualToDetailedItemDtoWithoutBookingsResult = ItemDtoMapper
                .toDetailedItemDtoWithoutBookings(item, commentList);
        assertTrue(actualToDetailedItemDtoWithoutBookingsResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoWithoutBookingsResult.getName());
        assertEquals(123L, actualToDetailedItemDtoWithoutBookingsResult.getId().longValue());
        assertEquals("The characteristics of someone or something",
                actualToDetailedItemDtoWithoutBookingsResult.getDescription());
        List<DetailedItemDto.CommentDto> comments = actualToDetailedItemDtoWithoutBookingsResult.getComments();
        assertEquals(2, comments.size());
        DetailedItemDto.CommentDto getResult = comments.get(0);
        assertEquals("Text", getResult.getText());
        DetailedItemDto.CommentDto getResult1 = comments.get(1);
        assertEquals("Text", getResult1.getText());
        assertEquals(123L, getResult1.getId().longValue());
        assertEquals("Name", getResult1.getAuthorName());
        assertEquals("01:01", getResult1.getCreated().toLocalTime().toString());
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("Name", getResult.getAuthorName());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDtoWithoutLastBooking(Item, List, Booking)}
     */
    @Test
    void testToDetailedItemDtoWithoutLastBooking() {
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
        ArrayList<Comment> commentList = new ArrayList<>();

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
        DetailedItemDto actualToDetailedItemDtoWithoutLastBookingResult = ItemDtoMapper
                .toDetailedItemDtoWithoutLastBooking(item, commentList, booking);
        assertTrue(actualToDetailedItemDtoWithoutLastBookingResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoWithoutLastBookingResult.getName());
        assertEquals(commentList, actualToDetailedItemDtoWithoutLastBookingResult.getComments());
        assertEquals(123L, actualToDetailedItemDtoWithoutLastBookingResult.getId().longValue());
        assertEquals("The characteristics of someone or something",
                actualToDetailedItemDtoWithoutLastBookingResult.getDescription());
        DetailedItemDto.BookingDto nextBooking = actualToDetailedItemDtoWithoutLastBookingResult.getNextBooking();
        assertEquals(123L, nextBooking.getId().longValue());
        assertEquals(123L, nextBooking.getBookerId().longValue());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDtoWithoutLastBooking(Item, List, Booking)}
     */
    @Test
    void testToDetailedItemDtoWithoutLastBooking2() {
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

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item1);
        comment.setText("Text");

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);

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
        DetailedItemDto actualToDetailedItemDtoWithoutLastBookingResult = ItemDtoMapper
                .toDetailedItemDtoWithoutLastBooking(item, commentList, booking);
        assertTrue(actualToDetailedItemDtoWithoutLastBookingResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoWithoutLastBookingResult.getName());
        List<DetailedItemDto.CommentDto> comments = actualToDetailedItemDtoWithoutLastBookingResult.getComments();
        assertEquals(1, comments.size());
        assertEquals(123L, actualToDetailedItemDtoWithoutLastBookingResult.getId().longValue());
        assertEquals("The characteristics of someone or something",
                actualToDetailedItemDtoWithoutLastBookingResult.getDescription());
        DetailedItemDto.BookingDto nextBooking = actualToDetailedItemDtoWithoutLastBookingResult.getNextBooking();
        assertEquals(123L, nextBooking.getId().longValue());
        assertEquals(123L, nextBooking.getBookerId().longValue());
        DetailedItemDto.CommentDto getResult = comments.get(0);
        assertEquals("Name", getResult.getAuthorName());
        assertEquals("Text", getResult.getText());
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("0001-01-01", getResult.getCreated().toLocalDate().toString());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDtoWithoutLastBooking(Item, List, Booking)}
     */
    @Test
    void testToDetailedItemDtoWithoutLastBooking3() {
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

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item1);
        comment.setText("Text");

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

        Comment comment1 = new Comment();
        comment1.setAuthor(user5);
        comment1.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment1.setId(123L);
        comment1.setItem(item2);
        comment1.setText("Text");

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment);

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
        DetailedItemDto actualToDetailedItemDtoWithoutLastBookingResult = ItemDtoMapper
                .toDetailedItemDtoWithoutLastBooking(item, commentList, booking);
        assertTrue(actualToDetailedItemDtoWithoutLastBookingResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoWithoutLastBookingResult.getName());
        List<DetailedItemDto.CommentDto> comments = actualToDetailedItemDtoWithoutLastBookingResult.getComments();
        assertEquals(2, comments.size());
        assertEquals(123L, actualToDetailedItemDtoWithoutLastBookingResult.getId().longValue());
        assertEquals("The characteristics of someone or something",
                actualToDetailedItemDtoWithoutLastBookingResult.getDescription());
        DetailedItemDto.BookingDto nextBooking = actualToDetailedItemDtoWithoutLastBookingResult.getNextBooking();
        assertEquals(123L, nextBooking.getBookerId().longValue());
        assertEquals(123L, nextBooking.getId().longValue());
        DetailedItemDto.CommentDto getResult = comments.get(0);
        assertEquals("Text", getResult.getText());
        DetailedItemDto.CommentDto getResult1 = comments.get(1);
        assertEquals("Text", getResult1.getText());
        assertEquals(123L, getResult1.getId().longValue());
        assertEquals("Name", getResult1.getAuthorName());
        assertEquals("01:01", getResult1.getCreated().toLocalTime().toString());
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("Name", getResult.getAuthorName());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDtoWithoutNextBooking(Item, List, Booking)}
     */
    @Test
    void testToDetailedItemDtoWithoutNextBooking() {
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
        ArrayList<Comment> commentList = new ArrayList<>();

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
        DetailedItemDto actualToDetailedItemDtoWithoutNextBookingResult = ItemDtoMapper
                .toDetailedItemDtoWithoutNextBooking(item, commentList, booking);
        assertTrue(actualToDetailedItemDtoWithoutNextBookingResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoWithoutNextBookingResult.getName());
        assertEquals(123L, actualToDetailedItemDtoWithoutNextBookingResult.getId().longValue());
        assertEquals(commentList, actualToDetailedItemDtoWithoutNextBookingResult.getComments());
        assertEquals("The characteristics of someone or something",
                actualToDetailedItemDtoWithoutNextBookingResult.getDescription());
        DetailedItemDto.BookingDto lastBooking = actualToDetailedItemDtoWithoutNextBookingResult.getLastBooking();
        assertEquals(123L, lastBooking.getBookerId().longValue());
        assertEquals(123L, lastBooking.getId().longValue());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDtoWithoutNextBooking(Item, List, Booking)}
     */
    @Test
    void testToDetailedItemDtoWithoutNextBooking2() {
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

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item1);
        comment.setText("Text");

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);

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
        DetailedItemDto actualToDetailedItemDtoWithoutNextBookingResult = ItemDtoMapper
                .toDetailedItemDtoWithoutNextBooking(item, commentList, booking);
        assertTrue(actualToDetailedItemDtoWithoutNextBookingResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoWithoutNextBookingResult.getName());
        assertEquals(123L, actualToDetailedItemDtoWithoutNextBookingResult.getId().longValue());
        List<DetailedItemDto.CommentDto> comments = actualToDetailedItemDtoWithoutNextBookingResult.getComments();
        assertEquals(1, comments.size());
        assertEquals("The characteristics of someone or something",
                actualToDetailedItemDtoWithoutNextBookingResult.getDescription());
        DetailedItemDto.BookingDto lastBooking = actualToDetailedItemDtoWithoutNextBookingResult.getLastBooking();
        assertEquals(123L, lastBooking.getBookerId().longValue());
        assertEquals(123L, lastBooking.getId().longValue());
        DetailedItemDto.CommentDto getResult = comments.get(0);
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("Name", getResult.getAuthorName());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
        assertEquals("Text", getResult.getText());
    }

    /**
     * Method under test: {@link ItemDtoMapper#toDetailedItemDtoWithoutNextBooking(Item, List, Booking)}
     */
    @Test
    void testToDetailedItemDtoWithoutNextBooking3() {
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

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item1);
        comment.setText("Text");

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

        Comment comment1 = new Comment();
        comment1.setAuthor(user5);
        comment1.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment1.setId(123L);
        comment1.setItem(item2);
        comment1.setText("Text");

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment);

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
        DetailedItemDto actualToDetailedItemDtoWithoutNextBookingResult = ItemDtoMapper
                .toDetailedItemDtoWithoutNextBooking(item, commentList, booking);
        assertTrue(actualToDetailedItemDtoWithoutNextBookingResult.getAvailable());
        assertEquals("Name", actualToDetailedItemDtoWithoutNextBookingResult.getName());
        assertEquals(123L, actualToDetailedItemDtoWithoutNextBookingResult.getId().longValue());
        List<DetailedItemDto.CommentDto> comments = actualToDetailedItemDtoWithoutNextBookingResult.getComments();
        assertEquals(2, comments.size());
        assertEquals("The characteristics of someone or something",
                actualToDetailedItemDtoWithoutNextBookingResult.getDescription());
        DetailedItemDto.BookingDto lastBooking = actualToDetailedItemDtoWithoutNextBookingResult.getLastBooking();
        assertEquals(123L, lastBooking.getId().longValue());
        assertEquals(123L, lastBooking.getBookerId().longValue());
        DetailedItemDto.CommentDto getResult = comments.get(0);
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("Name", getResult.getAuthorName());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
        DetailedItemDto.CommentDto getResult1 = comments.get(1);
        assertEquals(123L, getResult1.getId().longValue());
        assertEquals("Name", getResult1.getAuthorName());
        assertEquals("01:01", getResult1.getCreated().toLocalTime().toString());
        assertEquals("Text", getResult.getText());
        assertEquals("Text", getResult1.getText());
    }
}

