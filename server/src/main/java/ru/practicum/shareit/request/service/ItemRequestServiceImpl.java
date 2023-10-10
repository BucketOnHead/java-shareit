package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.request.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.response.ItemRequestDto;
import ru.practicum.shareit.request.logger.ItemRequestServiceLoggerHelper;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestDtoMapper itemRequestMapper;

    @Override
    @Transactional
    public ItemRequestDto addItemRequest(RequestItemRequestDto requestItemDto, Long requesterId) {
        userRepository.existsByIdOrThrow(requesterId);

        ItemRequest itemRequest = getItemRequest(requestItemDto, requesterId);
        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);

        ItemRequestServiceLoggerHelper.itemRequestSaved(log, savedItemRequest);
        return itemRequestMapper.mapToItemRequestDto(savedItemRequest);
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByRequesterId(Long userId) {
        userRepository.existsByIdOrThrow(userId);

        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterId(userId);
        List<ItemRequestDto> itemRequestsDto = getItemRequestsDto(itemRequests);

        ItemRequestServiceLoggerHelper.itemRequestByRequesterIdReturned(log, itemRequestsDto, userId);
        return itemRequestsDto;
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByRequesterId(Long userId,
                                                             Integer from, Integer size) {
        userRepository.existsByIdOrThrow(userId);

        List<ItemRequestDto> itemRequestDtos
                = getItemRequestsByRequesterIdWithPagination(userId, from, size);

        ItemRequestServiceLoggerHelper.itemRequestPageByRequesterIdReturned(log, from, size, itemRequestDtos, userId);
        return itemRequestDtos;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long itemRequestId, Long userId) {
        itemRequestRepository.validateItemRequestExistsById(itemRequestId);
        userRepository.existsByIdOrThrow(userId);

        ItemRequestDto itemRequestDto = getItemRequestDto(itemRequestId);

        ItemRequestServiceLoggerHelper.itemRequestReturned(log, itemRequestDto, userId);
        return itemRequestDto;
    }

    private ItemRequestDto getItemRequestDto(ItemRequest itemRequest,
                                                    Map<Long, List<Item>> itemsByItemRequestId) {
        List<Item> items = itemsByItemRequestId.getOrDefault(itemRequest.getId(), new ArrayList<>());
        return itemRequestMapper.mapToItemRequestDto(itemRequest, items);
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

        return itemRequestMapper.mapToItemRequestDto(itemRequest, items);
    }

    private List<ItemRequestDto> getItemRequestsByRequesterIdWithPagination(Long userId,
                                                                            Integer from, Integer size) {
        Page<ItemRequest> itemRequestsPage =
                itemRequestRepository.findAllByRequesterIdIsNot(userId, PageRequest.of(from, size));

        return getItemRequestsDto(itemRequestsPage.toList());
    }

    private ItemRequest getItemRequest(RequestItemRequestDto requestItemDto, Long requesterId) {
        var now = LocalDateTime.now();
        var requester = userRepository.getReferenceById(requesterId);
        return itemRequestMapper.mapToItemRequest(requestItemDto, requester, now);
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
