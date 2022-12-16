package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.in.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.out.ItemRequestDto;
import ru.practicum.shareit.request.dto.out.ItemRequestDto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestDtoMapperTest {
    /**
     * Method under test: {@link ItemRequestDtoMapper#toItemRequest(RequestItemRequestDto, User, LocalDateTime)}
     */
    @Test
    void testToItemRequest() {
        RequestItemRequestDto itemRequestDto = getRequestItemRequestDto(1L);
        User                  requester      = getRequester(1L);
        LocalDateTime         time           = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);

        ItemRequest itemRequest = ItemRequestDtoMapper.toItemRequest(itemRequestDto, requester, time);

        requester      = getRequester(1L);
        itemRequestDto = getRequestItemRequestDto(1L);
        assertEquals(time, itemRequest.getCreationTime());
        assertItemRequestDtoEquals(itemRequestDto, itemRequest);
        assertUserEquals(requester, itemRequest.getRequester());

    }

    /**
     * Method under test: {@link ItemRequestDtoMapper#toItemRequestDtoWithoutItems(ItemRequest)}
     */
    @Test
    void testToItemRequestDtoWithoutItems() {
        User        requester   = getRequester(1L);
        ItemRequest itemRequest = getItemRequest(1L, requester);

        ItemRequestDto itemRequestDto = ItemRequestDtoMapper.toItemRequestDtoWithoutItems(itemRequest);

        requester   = getRequester(1L);
        itemRequest = getItemRequest(1L, requester);
        assertItemRequestDtoWithoutItemsEquals(itemRequest, itemRequestDto);
    }

    /**
     * Method under test: {@link ItemRequestDtoMapper#toItemRequestDto(ItemRequest, Collection)}
     */
    @Test
    void testToItemRequestWithEmptyItems() {
        User        requester   = getRequester(1L);
        ItemRequest itemRequest = getItemRequest(1L, requester);
        List<Item>  items       = getItems();

        ItemRequestDto itemRequestDto = ItemRequestDtoMapper.toItemRequestDto(itemRequest, items);

        requester   = getRequester(1L);
        itemRequest = getItemRequest(1L, requester);
        items       = getItems();
        assertItemRequestDtoEquals(itemRequest, itemRequestDto, items);
    }

    /**
     * Method under test: {@link ItemRequestDtoMapper#toItemRequestDto(ItemRequest, Collection)}
     */
    @Test
    void testToItemRequestDtoWithOneItem() {
        User        requester   = getRequester(1L);
        User        owner       = getOwner(2L);
        ItemRequest itemRequest = getItemRequest(1L, requester);
        Item        item        = getItem(1L, Boolean.TRUE, owner, itemRequest);
        List<Item>  items       = getItems(item);

        ItemRequestDto itemRequestDto = ItemRequestDtoMapper.toItemRequestDto(itemRequest, items);

        requester   = getRequester(1L);
        owner       = getOwner(2L);
        itemRequest = getItemRequest(1L, requester);
        item        = getItem(1L, Boolean.TRUE, owner, itemRequest);
        items       = getItems(item);
        assertItemRequestDtoEquals(itemRequest, itemRequestDto, items);
    }

    /**
     * Method under test: {@link ItemRequestDtoMapper#toItemRequestDto(ItemRequest, Collection)}
     */
    @Test
    void testToItemRequestDtoWithTwoItems() {
        User        requester    = getRequester(1L);
        User        owner        = getOwner(2L);
        User        owner2       = getOwner(3L);
        ItemRequest itemRequest  = getItemRequest(1L, requester);
        ItemRequest itemRequest2 = getItemRequest(1L, requester);
        Item        item         = getItem(1L, Boolean.TRUE, owner, itemRequest);
        Item        item2        = getItem(2L, Boolean.FALSE, owner2, itemRequest2);
        List<Item>  items        = getItems(item, item2);

        ItemRequestDto itemRequestDto = ItemRequestDtoMapper.toItemRequestDto(itemRequest, items);

        requester    = getRequester(1L);
        owner        = getOwner(2L);
        owner2       = getOwner(3L);
        itemRequest  = getItemRequest(1L, requester);
        itemRequest2 = getItemRequest(1L, requester);
        item         = getItem(1L, Boolean.TRUE, owner, itemRequest);
        item2        = getItem(2L, Boolean.FALSE, owner2, itemRequest2);
        items        = getItems(item, item2);
        assertItemRequestDtoEquals(itemRequest, itemRequestDto, items);
    }

    /**
     * Method under test: {@link ItemRequestDtoMapper#ItemRequestDtoMapper()}
     */
    @Test
    void testConstructor() {
        var constructor = assertDoesNotThrow(() ->
                ItemRequestDtoMapper.class.getDeclaredConstructor()
        );

        constructor.setAccessible(true);

        var ex = assertThrows(InvocationTargetException.class,
                constructor::newInstance
        );

        String message = "This is a utility class and cannot be instantiated";
        assertTrue(ex.getCause() instanceof AssertionError);
        assertEquals(message, ex.getCause().getMessage());
    }

    private static RequestItemRequestDto getRequestItemRequestDto(Long n) {
        final String description = String.format("Item request dto description %d", n);

        RequestItemRequestDto itemRequestDto = new RequestItemRequestDto();

        itemRequestDto.setDescription(description);

        return itemRequestDto;
    }

    private static User getRequester(Long requesterId) {
        final String requesterName  = String.format("RequesterName%d", requesterId);
        final String requesterEmail = String.format("requester%d@example.org", requesterId);

        User user = new User();

        user.setId(requesterId);
        user.setName(requesterName);
        user.setEmail(requesterEmail);

        return user;
    }

    private User getOwner(long ownerId) {
        final String ownerName  = String.format("OwnerName%d", ownerId);
        final String ownerEmail = String.format("owner%d@example.org", ownerId);

        User user = new User();

        user.setId(ownerId);
        user.setName(ownerName);
        user.setEmail(ownerEmail);

        return user;
    }

    private Item getItem(long itemId, Boolean isAvailable, User owner, ItemRequest itemRequest) {
        final String name        = String.format("ItemName%d", itemId);
        final String description = String.format("Item description %d", itemId);

        Item item = new Item();

        item.setName(name);
        item.setDescription(description);
        item.setIsAvailable(isAvailable);
        item.setOwner(owner);
        item.setItemRequest(itemRequest);

        return item;
    }

    private static ItemRequest getItemRequest(Long itemRequestId, User requester) {
        final String        description = String.format("Item request description %d", itemRequestId);
        final LocalDateTime time        = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setId(itemRequestId);
        itemRequest.setDescription(description);
        itemRequest.setCreationTime(time);
        itemRequest.setRequester(requester);

        return itemRequest;
    }

    private static List<Item> getItems(Item... items) {
        return Arrays.asList(items);
    }

    private static void assertUserEquals(User user1, User user2) {
        assertEquals(user1.getId(),    user2.getId());
        assertEquals(user1.getName(),  user2.getName());
        assertEquals(user1.getEmail(), user2.getEmail());
    }

    private static void assertItemEquals(Item item, ItemDto itemDto) {
        if (item.getItemRequest() != null) {
            assertEquals(item.getItemRequest().getId(), itemDto.getRequestId());
        }

        assertEquals(item.getId(),          itemDto.getId());
        assertEquals(item.getName(),        itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getIsAvailable(), itemDto.getAvailable());
    }

    private void assertItemRequestDtoEquals(RequestItemRequestDto dto, ItemRequest itemRequest) {
        assertEquals(dto.getDescription(), itemRequest.getDescription());
    }

    private void assertItemRequestDtoWithoutItemsEquals(ItemRequest itemRequest, ItemRequestDto dto) {
        assertNull(dto.getItems());
        assertEquals(itemRequest.getId(),           dto.getId());
        assertEquals(itemRequest.getDescription(),  dto.getDescription());
        assertEquals(itemRequest.getCreationTime(), dto.getCreated());
    }

    private void assertItemRequestDtoEquals(ItemRequest itemRequest, ItemRequestDto dto, List<Item> items) {
        assertNotNull(dto.getItems());
        assertEquals(itemRequest.getId(),           dto.getId());
        assertEquals(itemRequest.getDescription(),  dto.getDescription());
        assertEquals(itemRequest.getCreationTime(), dto.getCreated());
        assertItemEquals(items,                     dto.getItems());
    }

    private static void assertItemEquals(List<Item> items, List<ItemDto> itemsDto) {
        if (items.size() != itemsDto.size()) {
            throw new RuntimeException("Size of the lists should be the same");
        }

        for (int i = 0; i < items.size(); i++) {
            assertItemEquals(items.get(i), itemsDto.get(i));
        }
    }
}
