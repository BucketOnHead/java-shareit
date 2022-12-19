package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.in.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.out.ItemRequestDto;
import ru.practicum.shareit.request.dto.out.ItemRequestDto.ItemDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestServiceImplTest {
    private final ItemRequestServiceImpl itemRequestService;
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
        // test parameters
        final Long itemRequestId = 1L;
        // test context
        when(itemRequestRepository.existsById(anyLong())).thenReturn(true);

        ItemRequestServiceImpl.checkItemRequestExistsById(itemRequestRepository, itemRequestId);

        verify(itemRequestRepository).existsById(itemRequestId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#checkItemRequestExistsById(ItemRequestRepository, Long)}
     */
    @Test
    void testCheckItemRequestExistsById_UserNotFound() {
        // test parameters
        final Long itemRequestId = 1L;
        // test context
        when(itemRequestRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ItemRequestNotFoundException.class, () ->
                ItemRequestServiceImpl.checkItemRequestExistsById(itemRequestRepository, itemRequestId));

        verify(itemRequestRepository).existsById(itemRequestId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#addItemRequest(RequestItemRequestDto, Long)}
     */
    @Test
    void testAddItemRequest() {
        // test parameters
        final Long requesterId   = 1L;
        final Long itemRequestId = 1L;
        // test context
        final User                  requester             = getRequester(requesterId);
        final ItemRequest           itemRequest           = getItemRequest(itemRequestId, requester);
        final RequestItemRequestDto requestItemRequestDto = getRequestItemRequestDto(itemRequestId);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.getReferenceById(anyLong())).thenReturn(requester);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto itemRequestDto = itemRequestService.addItemRequest(requestItemRequestDto, requesterId);

        assertEquals(itemRequest.getId(),           itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(),  itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreationTime(), itemRequestDto.getCreated());
        verify(userRepository).existsById(requesterId);
        verify(userRepository).getReferenceById(requesterId);
        verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#addItemRequest(RequestItemRequestDto, Long)}
     */
    @Test
    void testAddItemRequest_UserNotFound() {
        // test parameters
        final Long requesterId   = 1L;
        final Long itemRequestId = 1L;
        // test context
        final RequestItemRequestDto requestItemRequestDto = getRequestItemRequestDto(itemRequestId);
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                itemRequestService.addItemRequest(requestItemRequestDto, requesterId));

        verify(userRepository).existsById(requesterId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterId_Empty() {
        // test parameters
        final Long requesterId = 1L;
        // test context
        final List<ItemRequest> itemRequests = getItemRequests();
        final List<Item>        items        = getItems();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findAllByRequesterId(anyLong())).thenReturn(itemRequests);
        when(itemRepository.findAll()).thenReturn(items);

        List<ItemRequestDto> itemRequestsByRequesterId =
                itemRequestService.getItemRequestsByRequesterId(requesterId);

        assertTrue(itemRequestsByRequesterId.isEmpty());
        verify(userRepository).existsById(requesterId);
        verify(itemRequestRepository).findAllByRequesterId(requesterId);
        verify(itemRepository).findAll();
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterId_OneItemRequest() {
        // test parameters
        final Long requesterId   = 1L;
        final Long itemRequestId = 1L;
        // test context
        final User              requester    = getRequester(requesterId);
        final ItemRequest       itemRequest  = getItemRequest(itemRequestId, requester);
        final List<ItemRequest> itemRequests = getItemRequests(itemRequest);
        final List<Item>        items        = getItems();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findAllByRequesterId(anyLong())).thenReturn(itemRequests);
        when(itemRepository.findAll()).thenReturn(items);

        List<ItemRequestDto> itemRequestsDto =
                itemRequestService.getItemRequestsByRequesterId(requesterId);

        assertEquals(1, itemRequestsDto.size());

        ItemRequestDto itemRequestDto = itemRequestsDto.get(0);
        assertEquals(itemRequest.getId(),           itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(),  itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreationTime(), itemRequestDto.getCreated());
        assertNotNull(itemRequestDto.getItems());
        assertTrue(itemRequestDto.getItems().isEmpty());

        verify(userRepository).existsById(requesterId);
        verify(itemRequestRepository).findAllByRequesterId(requesterId);
        verify(itemRepository).findAll();
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterId_TwoItemRequests() {
        // test parameters
        final Long requesterId    = 1L;
        final Long itemRequestId  = 1L;
        final Long itemRequestId2 = 2L;
        // test context
        final User              requester     = getRequester(requesterId);
        final ItemRequest       itemRequest   = getItemRequest(itemRequestId, requester);
        final ItemRequest       itemRequest2  = getItemRequest(itemRequestId2, requester);
        final List<ItemRequest> itemRequests  = getItemRequests(itemRequest, itemRequest2);
        final List<Item>        items         = getItems();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findAllByRequesterId(anyLong())).thenReturn(itemRequests);
        when(itemRepository.findAll()).thenReturn(items);

        List<ItemRequestDto> itemRequestsDto =
                itemRequestService.getItemRequestsByRequesterId(requesterId);

        assertEquals(2, itemRequestsDto.size());

        ItemRequestDto itemRequestDto = itemRequestsDto.get(0);
        assertEquals(itemRequest.getId(),           itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(),  itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreationTime(), itemRequestDto.getCreated());
        assertNotNull(itemRequestDto.getItems());
        assertTrue(itemRequestDto.getItems().isEmpty());

        ItemRequestDto itemRequestDto2 = itemRequestsDto.get(1);
        assertEquals(itemRequest2.getId(),           itemRequestDto2.getId());
        assertEquals(itemRequest2.getDescription(),  itemRequestDto2.getDescription());
        assertEquals(itemRequest2.getCreationTime(), itemRequestDto2.getCreated());
        assertNotNull(itemRequestDto2.getItems());
        assertTrue(itemRequestDto2.getItems().isEmpty());

        verify(userRepository).existsById(requesterId);
        verify(itemRequestRepository).findAllByRequesterId(requesterId);
        verify(itemRepository).findAll();
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterId_UserNotFound() {
        // test parameters
        final Long requesterId = 1L;
        // test context
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                itemRequestService.getItemRequestsByRequesterId(requesterId));

        verify(userRepository).existsById(requesterId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterId_OneItemRequestAndOther() {
        // test parameters
        final Long requesterId   = 1L;
        final Long ownerId       = 2L;
        final Long itemRequestId = 1L;
        final Long itemId        = 1L;
        // test context
        final User              requester    = getRequester(requesterId);
        final User              owner        = getOwner(ownerId);
        final ItemRequest       itemRequest  = getItemRequest(itemRequestId, requester);
        final Item              item         = getItem(itemId, Boolean.TRUE, owner, null);
        final List<ItemRequest> itemRequests = getItemRequests(itemRequest);
        final List<Item>        items        = getItems(item);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findAllByRequesterId(anyLong())).thenReturn(itemRequests);
        when(itemRepository.findAll()).thenReturn(items);

        List<ItemRequestDto> itemRequestsDto = itemRequestService.getItemRequestsByRequesterId(requesterId);

        assertEquals(1, itemRequestsDto.size());

        ItemRequestDto itemRequestDto = itemRequestsDto.get(0);
        assertEquals(itemRequest.getId(),           itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(),  itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreationTime(), itemRequestDto.getCreated());
        assertNotNull(itemRequestDto.getItems());

        assertEquals(0, itemRequestDto.getItems().size());

        verify(userRepository).existsById(requesterId);
        verify(itemRequestRepository).findAllByRequesterId(requesterId);
        verify(itemRepository).findAll();
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long, Integer, Integer)}
     */
    @Test
    void testGetItemRequestsByRequesterIdPage_Empty() {
        // test parameters
        final Long requesterId = 1L;
        // test context
        final Integer           from         = 1;
        final Integer           size         = 2;
        final List<ItemRequest> itemRequests = getItemRequests();
        final List<Item>        items = getItems();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findAllByRequesterIdIsNotLike(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(itemRequests));
        when(itemRepository.findAll()).thenReturn(items);

        List<ItemRequestDto> itemRequestsDtoByRequesterId =
                itemRequestService.getItemRequestsByRequesterId(requesterId, from, size);

        assertTrue(itemRequestsDtoByRequesterId.isEmpty());
        verify(userRepository).existsById(requesterId);
        verify(itemRequestRepository).findAllByRequesterIdIsNotLike(anyLong(), any(Pageable.class));
        verify(itemRepository).findAll();
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long, Integer, Integer)}
     */
    @Test
    void testGetItemRequestsByRequesterIdPage_NullFromAndNullSize() {
        // test parameters
        final Long requesterId = 1L;
        // test context
        final Integer from = null;
        final Integer size = null;
        when(userRepository.existsById(anyLong())).thenReturn(true);

        List<ItemRequestDto> itemRequestsDto =
                itemRequestService.getItemRequestsByRequesterId(requesterId, from, size);

        assertNotNull(itemRequestsDto);
        assertTrue(itemRequestsDto.isEmpty());
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long, Integer, Integer)}
     */
    @Test
    void testGetItemRequestsByRequesterIdPage_UserNotFound() {
        // test parameters
        final Long requesterId = 1L;
        // test context
        final Integer from = 1;
        final Integer size = 2;
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                itemRequestService.getItemRequestsByRequesterId(requesterId, from, size));

        verify(userRepository).existsById(requesterId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long, Integer, Integer)}
     */
    @Test
    void testGetItemRequestsByRequesterId_NullSize() {
        // test parameters
        final Long    requesterId = 1L;
        final Integer from        = 1;
        // test context
        final Integer size = null;
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(IncorrectDataException.class, () ->
                itemRequestService.getItemRequestsByRequesterId(requesterId, from, size));

        verify(userRepository).existsById(requesterId);
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestsByRequesterId(Long, Integer, Integer)}
     */
    @Test
    void testGetItemRequestsByRequesterId_WithTwoItems() {
        // test parameters
        final Long requesterId    = 1L;
        final Long requester2Id   = 2L;
        final Long requester3Id   = 3L;
        final Long ownerId        = 4L;
        final Long owner2Id       = 5L;
        final Long itemRequestId  = 2L;
        final Long itemRequest2Id = 3L;
        final Long itemId         = 1L;
        final Long item2Id        = 2L;
        // test context
        final User              requester    = getRequester(requesterId);
        final User              requester2   = getRequester(requester2Id);
        final User              owner        = getOwner(ownerId);
        final User              owner2       = getOwner(owner2Id);
        final ItemRequest       itemRequest  = getItemRequest(itemRequestId, requester);
        final ItemRequest       itemRequest2 = getItemRequest(itemRequest2Id, requester2);
        final List<ItemRequest> itemRequests = getItemRequests(itemRequest, itemRequest2);
        final Item              item         = getItem(itemId, Boolean.TRUE, owner, itemRequest);
        final Item              item2        = getItem(item2Id, Boolean.TRUE, owner2, itemRequest2);
        final List<Item>        items        = getItems(item, item2);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findAllByRequesterIdIsNotLike(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(itemRequests));
        when(itemRepository.findAll()).thenReturn(items);

        List<ItemRequestDto> itemRequestsDto =
                itemRequestService.getItemRequestsByRequesterId(requester3Id, 1, 3);

        assertEquals(2, itemRequestsDto.size());

        ItemRequestDto itemRequestDto = itemRequestsDto.get(0);
        assertEquals(itemRequest.getId(),           itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(),  itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreationTime(), itemRequestDto.getCreated());
        List<ItemDto> itemsDto = itemRequestDto.getItems();
        assertEquals(1,                    itemsDto.size());
        assertEquals(item.getId(),                  itemsDto.get(0).getId());
        assertEquals(item.getName(),                itemsDto.get(0).getName());
        assertEquals(item.getDescription(),         itemsDto.get(0).getDescription());
        assertEquals(item.getItemRequest().getId(), itemsDto.get(0).getRequestId());
        assertEquals(item.getIsAvailable(),         itemsDto.get(0).getAvailable());

        ItemRequestDto itemRequestDto2 = itemRequestsDto.get(1);
        assertEquals(itemRequest2.getId(),           itemRequestDto2.getId());
        assertEquals(itemRequest2.getDescription(),  itemRequestDto2.getDescription());
        assertEquals(itemRequest2.getCreationTime(), itemRequestDto2.getCreated());
        List<ItemDto> itemsDto2 = itemRequestDto2.getItems();
        assertEquals(1,                     itemsDto2.size());
        assertEquals(item2.getId(),                  itemsDto2.get(0).getId());
        assertEquals(item2.getName(),                itemsDto2.get(0).getName());
        assertEquals(item2.getDescription(),         itemsDto2.get(0).getDescription());
        assertEquals(item2.getIsAvailable(),         itemsDto2.get(0).getAvailable());
        assertEquals(item2.getItemRequest().getId(), itemsDto2.get(0).getRequestId());

        verify(userRepository).existsById(requester3Id);
        verify(itemRequestRepository).findAllByRequesterIdIsNotLike(anyLong(), any(Pageable.class));
        verify(itemRepository).findAll();
    }

    /**
     * Method under test: {@link ItemRequestServiceImpl#getItemRequestById(Long, Long)}
     */
    @Test
    void testGetItemRequestById6() {
        // test parameters
        final Long requesterId    = 1L;
        final Long requester2Id   = 2L;
        final Long requester3Id   = 3L;
        final Long ownerId        = 4L;
        final Long owner2Id       = 5L;
        final Long itemRequestId  = 1L;
        final Long itemRequest2Id = 2L;
        final Long itemRequest3Id = 3L;
        final Long itemId         = 1L;
        final Long item2Id        = 2L;
        // test context
        final User        requester    = getRequester(requesterId);
        final User        requester2   = getRequester(requester2Id);
        final User        requester3   = getOwner(requester3Id);
        final User        owner        = getOwner(ownerId);
        final User        owner2       = getOwner(owner2Id);
        final ItemRequest itemRequest  = getItemRequest(itemRequestId, requester);
        final ItemRequest itemRequest2 = getItemRequest(itemRequest2Id, requester2);
        final ItemRequest itemRequest3 = getItemRequest(itemRequest3Id, requester3);
        final Item        item         = getItem(itemId, Boolean.TRUE, owner, itemRequest2);
        final Item        item2        = getItem(item2Id, Boolean.TRUE, owner2, itemRequest3);
        final List<Item>  items        = getItems(item, item2);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.getReferenceById(anyLong())).thenReturn(itemRequest);
        when(itemRepository.findAllByItemRequestId(anyLong())).thenReturn(items);

        ItemRequestDto itemRequestDto = itemRequestService.getItemRequestById(itemRequestId, requesterId);

        assertEquals(itemRequest.getId(),           itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(),  itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreationTime(), itemRequestDto.getCreated());

        List<ItemDto> itemsDto = itemRequestDto.getItems();
        assertEquals(2,                     itemsDto.size());
        assertEquals(item.getId(),                   itemsDto.get(0).getId());
        assertEquals(item.getName(),                 itemsDto.get(0).getName());
        assertEquals(item.getDescription(),          itemsDto.get(0).getDescription());
        assertEquals(item.getItemRequest().getId(),  itemsDto.get(0).getRequestId());
        assertEquals(item.getIsAvailable(),          itemsDto.get(0).getAvailable());
        assertEquals(item2.getId(),                  itemsDto.get(1).getId());
        assertEquals(item2.getName(),                itemsDto.get(1).getName());
        assertEquals(item2.getDescription(),         itemsDto.get(1).getDescription());
        assertEquals(item2.getItemRequest().getId(), itemsDto.get(1).getRequestId());
        assertEquals(item2.getIsAvailable(),         itemsDto.get(1).getAvailable());

        verify(userRepository).existsById(requesterId);
        verify(itemRequestRepository).existsById(itemRequestId);
        verify(itemRequestRepository).getReferenceById(itemRequestId);
        verify(itemRepository).findAllByItemRequestId(itemRequestId);
    }

    private User getRequester(Long requesterId) {
        final String name  = String.format("Requester%dName", requesterId);
        final String email = String.format("requester%d@example.org", requesterId);

        User requester = new User();
        requester.setId(requesterId);
        requester.setName(name);
        requester.setEmail(email);

        return requester;
    }

    private User getOwner(Long ownerId) {
        final String name  = String.format("Owner%dName", ownerId);
        final String email = String.format("owner%d@example.org", ownerId);

        User owner = new User();
        owner.setId(ownerId);
        owner.setName(name);
        owner.setEmail(email);

        return owner;
    }

    private ItemRequest getItemRequest(Long itemRequestId, User requester) {
        final String        description = String.format("Item request %d description", itemRequestId);
        final LocalDateTime time        = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestId);
        itemRequest.setDescription(description);
        itemRequest.setCreationTime(time);
        itemRequest.setRequester(requester);

        return itemRequest;
    }

    private Item getItem(Long itemId, Boolean isAvailable, User owner, ItemRequest itemRequest) {
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

    private RequestItemRequestDto getRequestItemRequestDto(Long itemRequestId) {
        final String description = String.format("Request item request dto %d description", itemRequestId);

        RequestItemRequestDto itemRequestDto = new RequestItemRequestDto();
        itemRequestDto.setDescription(description);

        return itemRequestDto;
    }

    private List<ItemRequest> getItemRequests(ItemRequest... itemRequests) {
        return Arrays.asList(itemRequests);
    }

    private List<Item> getItems(Item... items) {
        return Arrays.asList(items);
    }
}
