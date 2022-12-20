package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.in.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.out.ItemRequestDto;
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
        // test parameters
        final Long          requestItemRequestId = 1L;
        final Long          requesterId          = 1L;
        final LocalDateTime time                 = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        // test context
        User                  requester      = getRequester(requesterId);
        RequestItemRequestDto itemRequestDto = getRequestItemRequestDto(requestItemRequestId);

        ItemRequest itemRequest = ItemRequestDtoMapper.toItemRequest(itemRequestDto, requester, time);

        requester      = getRequester(requesterId);
        itemRequestDto = getRequestItemRequestDto(requestItemRequestId);
        assertEquals(requester.getId(),               itemRequest.getRequester().getId());
        assertEquals(requester.getName(),             itemRequest.getRequester().getName());
        assertEquals(requester.getEmail(),            itemRequest.getRequester().getEmail());
        assertEquals(time,                            itemRequest.getCreationTime());
        assertEquals(itemRequestDto.getDescription(), itemRequest.getDescription());
    }

    /**
     * Method under test: {@link ItemRequestDtoMapper#toItemRequestDtoWithoutItems(ItemRequest)}
     */
    @Test
    void testToItemRequestDtoWithoutItems() {
        // test parameters
        final Long requesterId   = 1L;
        final Long itemRequestId = 1L;
        // test context
        User        requester   = getRequester(requesterId);
        ItemRequest itemRequest = getItemRequest(itemRequestId, requester);

        ItemRequestDto itemRequestDto = ItemRequestDtoMapper.toItemRequestDtoWithoutItems(itemRequest);

        requester   = getRequester(requesterId);
        itemRequest = getItemRequest(itemRequestId, requester);
        assertNull(itemRequestDto.getItems());
        assertEquals(itemRequest.getId(),           itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(),  itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreationTime(), itemRequestDto.getCreated());
    }

    /**
     * Method under test: {@link ItemRequestDtoMapper#toItemRequestDto(ItemRequest, Collection)}
     */
    @Test
    void testToItemRequestDto_WithEmptyItems() {
        // test parameters
        final Long requesterId   = 1L;
        final Long itemRequestId = 1L;
        // test context
        User        requester   = getRequester(requesterId);
        ItemRequest itemRequest = getItemRequest(itemRequestId, requester);
        List<Item>  items       = getItems();

        ItemRequestDto itemRequestDto = ItemRequestDtoMapper.toItemRequestDto(itemRequest, items);

        assertNotNull(itemRequestDto.getItems());
        assertTrue(itemRequestDto.getItems().isEmpty());
        assertEquals(itemRequest.getId(),           itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(),  itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreationTime(), itemRequestDto.getCreated());
    }

    /**
     * Method under test: {@link ItemRequestDtoMapper#toItemRequestDto(ItemRequest, Collection)}
     */
    @Test
    void testToItemRequestDto2() {
        // test parameters
        final Long requesterId   = 1L;
        final Long ownerId       = 2L;
        final Long itemRequestId = 1L;
        final Long itemId        = 1L;
        // test context
        User        requester   = getRequester(requesterId);
        User        owner       = getOwner(ownerId);
        ItemRequest itemRequest = getItemRequest(itemRequestId, requester);
        Item        item        = getItem(itemId, Boolean.TRUE, owner, itemRequest);
        List<Item>  items       = getItems(item);


        ItemRequestDto itemRequestDto = ItemRequestDtoMapper.toItemRequestDto(itemRequest, items);

        requester   = getRequester(requesterId);
        owner       = getOwner(ownerId);
        itemRequest = getItemRequest(itemRequestId, requester);
        item        = getItem(itemId, Boolean.TRUE, owner, itemRequest);
        assertNotNull(itemRequestDto.getItems());
        assertEquals(1,                    itemRequestDto.getItems().size());
        assertEquals(itemRequest.getId(),           itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(),  itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreationTime(), itemRequestDto.getCreated());
        ItemRequestDto.ItemDto itemDto = itemRequestDto.getItems().get(0);
        assertEquals(item.getId(),                                 itemDto.getId());
        assertEquals(item.getName(),                               itemDto.getName());
        assertEquals(item.getDescription(),                        itemDto.getDescription());
        assertEquals(item.getIsAvailable(),                        itemDto.getAvailable());
        assertEquals(item.getItemRequest().getRequester().getId(), itemDto.getRequestId());
    }

    /**
     * Method under test: {@link ItemRequestDtoMapper#toItemRequestDto(ItemRequest, Collection)}
     */
    @Test
    void testToItemRequestDto_WithTwoItems() {
        // test parameters
        final Long requesterId    = 1L;
        final Long requesterId2   = 2L;
        final Long requesterId3   = 3L;
        final Long ownerId        = 3L;
        final Long ownerId2       = 4L;
        final Long itemRequestId  = 1L;
        final Long itemRequestId2 = 2L;
        final Long itemRequestId3 = 3L;
        final Long itemId         = 1L;
        // test context
        User requester           = getRequester(requesterId);
        User requester2          = getRequester(requesterId2);
        User requester3          = getRequester(requesterId3);
        User owner               = getOwner(ownerId);
        User owner2              = getOwner(ownerId2);
        ItemRequest itemRequest  = getItemRequest(itemRequestId, requester);
        ItemRequest itemRequest2 = getItemRequest(itemRequestId2, requester2);
        ItemRequest itemRequest3 = getItemRequest(itemRequestId3, requester3);
        Item item                = getItem(itemId, Boolean.TRUE, owner, itemRequest2);
        Item item2               = getItem(itemId, Boolean.FALSE, owner2, itemRequest3);
        List<Item> items         = getItems(item, item2);

        ItemRequestDto itemRequestDto = ItemRequestDtoMapper.toItemRequestDto(itemRequest, items);

        requester    = getRequester(requesterId);
        requester2   = getRequester(requesterId2);
        requester3   = getRequester(requesterId3);
        owner        = getOwner(ownerId);
        owner2       = getOwner(ownerId2);
        itemRequest  = getItemRequest(itemRequestId, requester);
        itemRequest2 = getItemRequest(itemRequestId2, requester2);
        itemRequest3 = getItemRequest(itemRequestId3, requester3);
        item         = getItem(itemId, Boolean.TRUE, owner, itemRequest2);
        item2        = getItem(itemId, Boolean.FALSE, owner2, itemRequest3);
        assertNotNull(itemRequestDto.getItems());
        assertEquals(2,                    itemRequestDto.getItems().size());
        assertEquals(itemRequest.getId(),           itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(),  itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreationTime(), itemRequestDto.getCreated());
        ItemRequestDto.ItemDto itemDto = itemRequestDto.getItems().get(0);
        assertEquals(item.getId(),                                 itemDto.getId());
        assertEquals(item.getName(),                               itemDto.getName());
        assertEquals(item.getDescription(),                        itemDto.getDescription());
        assertEquals(item.getIsAvailable(),                        itemDto.getAvailable());
        assertEquals(item.getItemRequest().getRequester().getId(), itemDto.getRequestId());
        ItemRequestDto.ItemDto itemDto2 = itemRequestDto.getItems().get(1);
        assertEquals(item2.getId(),                                 itemDto2.getId());
        assertEquals(item2.getName(),                               itemDto2.getName());
        assertEquals(item2.getDescription(),                        itemDto2.getDescription());
        assertEquals(item2.getIsAvailable(),                        itemDto2.getAvailable());
        assertEquals(item2.getItemRequest().getRequester().getId(), itemDto2.getRequestId());
    }

    /**
     * Method under test: {@link ItemRequestDtoMapper#ItemRequestDtoMapper()}
     */
    @Test
    void testConstructor() throws NoSuchMethodException {
        // test context
        var constructor = ItemRequestDtoMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        var ex = assertThrows(InvocationTargetException.class, constructor::newInstance);

        String message = "This is a utility class and cannot be instantiated";
        assertTrue(ex.getCause() instanceof AssertionError);
        assertEquals(message, ex.getCause().getMessage());
    }

    private User getRequester(Long requesterId) {
        final String name       = String.format("RequesterName%d", requesterId);
        final String email      = String.format("requester%d@example.org", requesterId);
        final User   requester  = new User();

        requester.setId(requesterId);
        requester.setName(name);
        requester.setEmail(email);

        return requester;
    }

    private User getOwner(Long ownerId) {
        final String name   = String.format("OwnerName%d", ownerId);
        final String email  = String.format("owner%d@example.org", ownerId);
        final User   owner  = new User();

        owner.setId(ownerId);
        owner.setName(name);
        owner.setEmail(email);

        return owner;
    }

    private Item getItem(Long itemId, Boolean isAvailable, User owner, ItemRequest itemRequest) {
        final String name        = String.format("Item%dName", itemId);
        final String description = String.format("Item %d description", itemId);
        final Item   item        = new Item();

        item.setId(itemId);
        item.setName(name);
        item.setDescription(description);
        item.setIsAvailable(isAvailable);
        item.setOwner(owner);
        item.setItemRequest(itemRequest);

        return item;
    }

    private ItemRequest getItemRequest(Long itemRequestId, User requester) {
        final String        description = String.format("Item request %d description", itemRequestId);
        final LocalDateTime time        = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        final ItemRequest   itemRequest = new ItemRequest();

        itemRequest.setId(itemRequestId);
        itemRequest.setDescription(description);
        itemRequest.setCreationTime(time);
        itemRequest.setRequester(requester);

        return itemRequest;
    }

    private RequestItemRequestDto getRequestItemRequestDto(Long id) {
        final String                description    = String.format("Request item request dto description %d", id);
        final RequestItemRequestDto itemRequestDto = new RequestItemRequestDto();

        itemRequestDto.setDescription(description);

        return itemRequestDto;
    }

    private List<Item> getItems(Item... items) {
        return Arrays.asList(items);
    }
}
