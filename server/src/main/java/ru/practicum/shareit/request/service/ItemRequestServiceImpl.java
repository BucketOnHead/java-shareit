package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.request.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.response.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.service.UserServiceImpl.validateUserExistsById;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public static void checkItemRequestExistsById(ItemRequestRepository itemRequestRepository, Long itemRequestId) {
        if (!itemRequestRepository.existsById(itemRequestId)) {
            throw ItemRequestNotFoundException.getFromId(itemRequestId);
        }
    }

    @Override
    @Transactional
    public ItemRequestDto addItemRequest(RequestItemRequestDto requestItemDto, Long userId) {
        validateUserExistsById(userRepository, userId);

        ItemRequest itemRequest = getItemRequest(requestItemDto, userId, LocalDateTime.now());
        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);

        log.debug("ITEM_REQUEST[ID_{}] saved.", savedItemRequest.getId());
        return ItemRequestDtoMapper.toItemRequestDtoWithoutItems(savedItemRequest);
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByRequesterId(Long userId) {
        validateUserExistsById(userRepository, userId);

        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterId(userId);
        List<ItemRequestDto> itemRequestsDto = getItemRequestsDto(itemRequests);

        log.debug("All ITEM_REQUEST<DTO> from USER[ID_{}] returned, {} in total.", userId, itemRequestsDto.size());
        return itemRequestsDto;
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByRequesterId(Long userId,
                                                             Integer from, Integer size) {
        validateUserExistsById(userRepository, userId);

        List<ItemRequestDto> itemRequestsDto
                = getItemRequestsByRequesterIdWithPagination(userId, from, size);

        log.debug("All ITEM_REQUEST<DTO> from USER[ID_{}] returned, {} in total.", userId, itemRequestsDto.size());
        return itemRequestsDto;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long itemRequestId, Long userId) {
        checkItemRequestExistsById(itemRequestRepository, itemRequestId);
        validateUserExistsById(userRepository, userId);

        ItemRequestDto itemRequestDto = getItemRequestDto(itemRequestId);

        log.debug("ITEM_REQUEST[ID_{}]<DTO> from USER[ID_{}] returned", itemRequestId, userId);
        return itemRequestDto;
    }

    private static ItemRequestDto getItemRequestDto(ItemRequest itemRequest,
                                                    Map<Long, List<Item>> itemsByItemRequestId) {
        List<Item> items = itemsByItemRequestId.getOrDefault(itemRequest.getId(), new ArrayList<>());
        return ItemRequestDtoMapper.toItemRequestDto(itemRequest, items);
    }

    private List<ItemRequestDto> getItemRequestsDto(List<ItemRequest> itemRequests) {
        Map<Long, List<Item>> itemsByRequestId = getItemsByItemRequestId();
        return itemRequests.stream()
                .map(itemRequest -> getItemRequestDto(itemRequest, itemsByRequestId))
                .collect(Collectors.toList());
    }

    private ItemRequestDto getItemRequestDto(Long itemRequestId) {
        ItemRequest itemRequest = itemRequestRepository.getReferenceById(itemRequestId);
        List<Item> items = itemRepository.findAllByItemRequestId(itemRequestId);

        return ItemRequestDtoMapper.toItemRequestDto(itemRequest, items);
    }

    private List<ItemRequestDto> getItemRequestsByRequesterIdWithPagination(Long userId,
                                                                            Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size);
        Page<ItemRequest> itemRequestsPage = getItemRequestsWithoutRequester(userId, page);
        return getItemRequestsDto(itemRequestsPage.toList());
    }

    private ItemRequest getItemRequest(RequestItemRequestDto requestItemDto, Long requesterId, LocalDateTime time) {
        User requester = userRepository.getReferenceById(requesterId);
        return ItemRequestDtoMapper.toItemRequest(requestItemDto, requester, time);
    }

    private Page<ItemRequest> getItemRequestsWithoutRequester(Long userId, Pageable page) {
        return itemRequestRepository.findAllByRequesterIdIsNot(userId, page);
    }

    private Map<Long, List<Item>> getItemsByItemRequestId() {
        Map<Long, List<Item>> itemsByItemRequestId = new HashMap<>();

        List<Item> allItems = itemRepository.findAll();
        for (Item item : allItems) {
            if (item.getItemRequest() == null) {
                continue;
            }
            Long currentItemRequestId = item.getItemRequest().getId();

            List<Item> itemsByCurrentItemRequestId =
                    itemsByItemRequestId.getOrDefault(currentItemRequestId, new ArrayList<>());
            itemsByCurrentItemRequestId.add(item);

            itemsByItemRequestId.put(currentItemRequestId, itemsByCurrentItemRequestId);
        }

        return itemsByItemRequestId;
    }
}
