package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.in.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.out.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ItemRequestServiceImpl.class})
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestServiceImplTest {
    private final ItemRequestServiceImpl itemRequestServiceImpl;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link ItemRequestServiceImpl#checkItemRequestExistsById(ItemRequestRepository, Long)}
     */
    @Test
    void testCheckItemRequestExistsById() {
        final Long itemRequestId = 1L;

        when(itemRequestRepository.existsById(anyLong()))
                .thenReturn(true);

        ItemRequestServiceImpl.checkItemRequestExistsById(itemRequestRepository, itemRequestId);

        verify(itemRequestRepository).existsById(itemRequestId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#checkItemRequestExistsById(ItemRequestRepository, Long)}
     */
    @Test
    void testCheckItemRequestExistsByIdThrowException() {
        final Long itemRequestId = 1L;

        when(itemRequestRepository.existsById(anyLong()))
                .thenThrow(ItemRequestNotFoundException.class);

        assertThrows(ItemRequestNotFoundException.class, () ->
                ItemRequestServiceImpl.checkItemRequestExistsById(itemRequestRepository, itemRequestId));

        verify(itemRequestRepository).existsById(itemRequestId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#addItemRequest(RequestItemRequestDto, Long)}
     */
    @Test
    void testAddItemRequest() {
        // IN
        final RequestItemRequestDto itemRequestDto = getRequestItemRequestDto(1L);

        // OUT
        final User                  requester      = getRequester(1L);
        final ItemRequest           itemRequest    = getItemRequest(1L, requester);

        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(userRepository.getReferenceById(anyLong()))
                .thenReturn(requester);
        when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);

        ItemRequestDto dto = itemRequestServiceImpl.addItemRequest(itemRequestDto, requester.getId());

        assertItemRequestDtoWithoutItemsEquals(itemRequest, dto);
        verify(userRepository).existsById(requester.getId());
        verify(userRepository).getReferenceById(anyLong());
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#addItemRequest(RequestItemRequestDto, Long)}
     */
    @Test
    void testAddItemRequestThrowException() {
        // IN
        final Long                  requesterId    = 1L;
        final RequestItemRequestDto itemRequestDto = getRequestItemRequestDto(1L);

        when(userRepository.existsById(anyLong()))
                .thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                itemRequestServiceImpl.addItemRequest(itemRequestDto, requesterId));

        verify(userRepository).existsById(requesterId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterIdWithEmptyItemRequests() {
        when(itemRequestRepository.findAllByRequesterId(anyLong()))
                .thenReturn(new ArrayList<>());
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<ItemRequestDto> itemRequestsByRequesterId =
                itemRequestServiceImpl.getItemRequestsByRequesterId(1L);

        assertTrue(itemRequestsByRequesterId.isEmpty());
        verify(itemRequestRepository).findAllByRequesterId(anyLong());
        verify(userRepository).existsById((anyLong()));
        verify(itemRepository).findAll();
    }


    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterIdWithOneItemRequest() {
        // IN
        final User              requester     = getRequester(1L);

        // OUT
        final ItemRequest       itemRequest   = getItemRequest(1L, requester);
        final List<ItemRequest> itemRequests  = getItemRequests(itemRequest);
        final List<Item>        items         = getItems();

        when(itemRequestRepository.findAllByRequesterId(anyLong()))
                .thenReturn(itemRequests);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.findAll())
                .thenReturn(items);

        List<ItemRequestDto> itemRequestsByRequesterId =
                itemRequestServiceImpl.getItemRequestsByRequesterId(requester.getId());

        assertEquals(1, itemRequestsByRequesterId.size());
        assertItemRequestDtoEquals(itemRequest, itemRequestsByRequesterId.get(0), items);
        verify(itemRequestRepository).findAllByRequesterId(requester.getId());
        verify(userRepository).existsById(requester.getId());
        verify(itemRepository).findAll();
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterWithTwoItemRequests() {
        // IN
        final User              requester     = getRequester(1L);

        // OUT
        final ItemRequest       itemRequest  = getItemRequest(1L, requester);
        final ItemRequest       itemRequest2 = getItemRequest(2L, requester);
        final List<ItemRequest> itemRequests = getItemRequests(itemRequest, itemRequest2);
        final List<Item>        items        = getItems();

        when(itemRequestRepository.findAllByRequesterId(anyLong()))
                .thenReturn(itemRequests);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.findAll())
                .thenReturn(items);

        List<ItemRequestDto> itemRequestsByRequesterId =
                itemRequestServiceImpl.getItemRequestsByRequesterId(requester.getId());

        assertEquals(2, itemRequestsByRequesterId.size());
        assertItemRequestDtoEquals(itemRequest, itemRequestsByRequesterId.get(0), items);
        assertItemRequestDtoEquals(itemRequest2, itemRequestsByRequesterId.get(1), items);
        verify(itemRequestRepository).findAllByRequesterId(requester.getId());
        verify(userRepository).existsById(requester.getId());
        verify(itemRepository).findAll();
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterIdThrowException() {
        // IN
        final Long requesterId = 1L;

        when(userRepository.existsById(anyLong()))
                .thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                itemRequestServiceImpl.getItemRequestsByRequesterId(requesterId));

        verify(userRepository).existsById(requesterId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterIdWithOtherItem() {
        // IN
        final User        requester   = getRequester(1L);

        // OUT
        final User        requester2  = getRequester(2L);
        final ItemRequest itemRequest = getItemRequest(1L, requester);
        final Item        item        = getItem(1L, Boolean.TRUE, requester2, itemRequest);
        final List<Item>  items       = getItems(item);

        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRequestRepository.findAllByRequesterId(anyLong()))
                .thenReturn(new ArrayList<>());
        when(itemRepository.findAll())
                .thenReturn(items);

        List<ItemRequestDto> itemRequestsByRequesterId =
                itemRequestServiceImpl.getItemRequestsByRequesterId(requester.getId());

        assertTrue(itemRequestsByRequesterId.isEmpty());
        verify(itemRequestRepository).findAllByRequesterId(requester.getId());
        verify(userRepository).existsById(requester.getId());
        verify(itemRepository).findAll();
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterIdWithItem() {
        // IN
        final User              requester       = getRequester(2L);

        // OUT
        final User              owner        = getOwner(2L);
        final ItemRequest       itemRequest  = getItemRequest(1L, requester);
        final Item              item         = getItem(1L, Boolean.TRUE, owner, itemRequest);
        final List<ItemRequest> itemRequests = getItemRequests(itemRequest);
        final List<Item>        items        = getItems(item);

        when(itemRequestRepository.findAllByRequesterId(anyLong()))
                .thenReturn(itemRequests);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.findAll())
                .thenReturn(items);

        List<ItemRequestDto> itemRequestsByRequesterId =
                itemRequestServiceImpl.getItemRequestsByRequesterId(requester.getId());

        assertEquals(1, itemRequestsByRequesterId.size());
        assertItemRequestDtoEquals(itemRequest, itemRequestsByRequesterId.get(0), items);
        verify(itemRequestRepository).findAllByRequesterId(requester.getId());
        verify(userRepository).existsById(requester.getId());
        verify(itemRepository).findAll();
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestById(Long, Long)}
     */
    @Test
    void testGetItemRequestByIdThrowUserNotFoundException() {
        // IN
        final Long itemRequestId = 1L;
        final Long userId        = 1L;

        when(itemRequestRepository.existsById(anyLong()))
                .thenReturn(true);
        when(userRepository.existsById(anyLong()))
                .thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                itemRequestServiceImpl.getItemRequestById(itemRequestId, userId));

        verify(itemRequestRepository).existsById(itemRequestId);
        verify(userRepository).existsById(userId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestById(Long, Long)}
     */
    @Test
    void testGetItemRequestByIdThrowItemRequestNotFoundException() {
        // IN
        final Long itemRequestId = 1L;
        final Long userId        = 1L;

        when(itemRequestRepository.existsById(anyLong()))
                .thenReturn(false);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);

        assertThrows(ItemRequestNotFoundException.class, () ->
                itemRequestServiceImpl.getItemRequestById(itemRequestId, userId));

        verify(itemRequestRepository).existsById(itemRequestId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestById(Long, Long)}
     */
    @Test
    void testGetItemRequestById() {
        // IN
        final Long itemRequestId = 1L;
        final Long userId        = 1L;

        // OUT
        final User        requester   = getRequester(userId);
        final ItemRequest itemRequest = getItemRequest(itemRequestId, requester);
        final List<Item>  items       = getItems();

        when(itemRequestRepository.existsById(anyLong()))
                .thenReturn(true);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRequestRepository.getReferenceById(anyLong()))
                .thenReturn(itemRequest);
        when(itemRepository.findAllByItemRequestId(anyLong()))
                .thenReturn(items);

        ItemRequestDto itemRequestById = itemRequestServiceImpl.getItemRequestById(itemRequestId, userId);

        assertItemRequestDtoEquals(itemRequest, itemRequestById, items);
        verify(itemRequestRepository).existsById(itemRequestId);
        verify(userRepository).existsById(userId);
        verify(itemRequestRepository).getReferenceById(itemRequestId);
        verify(itemRepository).findAllByItemRequestId(itemRequestId);
    }

    private static RequestItemRequestDto getRequestItemRequestDto(long n) {
        final String description = String.format("Request item request dto description %d", n);

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

    private User getOwner(Long ownerId) {
        final String ownerName  = String.format("OwnerName%d", ownerId);
        final String ownerEmail = String.format("owner%d@example.org", ownerId);

        User user = new User();

        user.setId(ownerId);
        user.setName(ownerName);
        user.setEmail(ownerEmail);

        return user;
    }

    private Item getItem(Long itemId, Boolean isAvailable, User owner, ItemRequest itemRequest) {
        final String name        = String.format("ItemName%d", itemId);
        final String description = String.format("Item description %d", itemId);

        Item item = new Item();

        item.setId(itemId);
        item.setName(name);
        item.setDescription(description);
        item.setIsAvailable(isAvailable);
        item.setOwner(owner);
        item.setItemRequest(itemRequest);

        return item;
    }

    private static ItemRequest getItemRequest(Long itemRequestId, User requester) {
        final String        description  = String.format("Item request description %d", itemRequestId);
        final LocalDateTime time         = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setId(itemRequestId);
        itemRequest.setDescription(description);
        itemRequest.setCreationTime(time);
        itemRequest.setRequester(requester);

        return itemRequest;
    }

    private List<ItemRequest> getItemRequests(ItemRequest... itemRequests) {
        return Arrays.asList(itemRequests);
    }

    private List<Item> getItems(Item... items) {
        return Arrays.asList(items);
    }

    private static void assertItemRequestDtoWithoutItemsEquals(ItemRequest itemRequest, ItemRequestDto dto) {
        assertEquals(itemRequest.getId(), dto.getId());
        assertEquals(itemRequest.getDescription(), dto.getDescription());
        assertEquals(itemRequest.getCreationTime(), dto.getCreated());
        assertNull(dto.getItems());
    }

    private static void assertItemRequestDtoEquals(ItemRequest itemRequest, ItemRequestDto dto, List<Item> items) {
        assertNotNull(dto.getItems());
        assertEquals(itemRequest.getId(),           dto.getId());
        assertEquals(itemRequest.getDescription(),  dto.getDescription());
        assertEquals(itemRequest.getCreationTime(), dto.getCreated());
        assertItemEquals(items,                     dto.getItems());
    }

    private static void assertItemEquals(List<Item> items, List<ItemRequestDto.ItemDto> itemsDto) {
        if (items.size() != itemsDto.size()) {
            throw new RuntimeException("Size of the lists should be the same");
        }

        for (int i = 0; i < items.size(); i++) {
            assertItemEquals(items.get(i), itemsDto.get(i));
        }
    }

    private static void assertItemEquals(Item item, ItemRequestDto.ItemDto itemDto) {
        if (item.getItemRequest() != null) {
            assertEquals(item.getItemRequest().getId(), itemDto.getRequestId());
        }

        assertEquals(item.getId(),          itemDto.getId());
        assertEquals(item.getName(),        itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getIsAvailable(), itemDto.getAvailable());
    }
}
