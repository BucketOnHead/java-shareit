package ru.practicum.shareit.server.itemrequest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.dto.itemrequest.request.ItemRequestCreationDto;
import ru.practicum.shareit.server.dto.itemrequest.response.ItemRequestDto;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.item.utils.ItemUtils;
import ru.practicum.shareit.server.itemrequest.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.server.itemrequest.repository.ItemRequestRepository;
import ru.practicum.shareit.server.itemrequest.utils.ItemRequestUtils;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestDtoMapper requestMapper;

    @Override
    @Transactional
    public ItemRequestDto addItemRequest(ItemRequestCreationDto requestDto, Long requesterId) {
        var requester = userRepository.findByIdOrThrow(requesterId);
        var request = requestMapper.mapToItemRequest(requestDto, requester);
        var savedRequest = requestRepository.save(request);

        log.info("Item request with id: {} added", savedRequest.getId());
        log.debug("Item request added: {}", savedRequest);

        return requestMapper.mapToItemRequestDto(savedRequest);
    }

    @Override
    public ItemRequestDto getItemRequestById(Long requestId, Long userId) {
        userRepository.existsByIdOrThrow(userId);

        var request = requestRepository.findByIdOrThrow(requestId);
        var items = itemRepository.findAllByItemRequestId(requestId);
        var requestDto = requestMapper.mapToItemRequestDto(request, items);

        log.info("Item request with id: {} returned", request.getId());
        log.debug("Item request returned: {}", requestDto);

        return requestDto;
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByRequesterId(Long requesterId) {
        userRepository.existsByIdOrThrow(requesterId);

        var requests = requestRepository.findAllByRequesterId(requesterId);
        var requestIds = ItemRequestUtils.toIdsSet(requests);
        var items = itemRepository.findAllByItemRequestIdIn(requestIds);

        var itemsByRequestId = ItemUtils.toItemsByRequestId(items);
        var requestsDto = requestMapper.mapToItemRequestDto(requests, itemsByRequestId);

        log.info("Item requests for user with id: {} returned, count: {}", requesterId, requestsDto.size());
        log.debug("Item requests for user with id: {} returned, {}", requesterId, requestsDto);

        return requestsDto;
    }

    @Override
    public List<ItemRequestDto> getItemRequests(Long userId, Integer from, Integer size) {
        userRepository.existsByIdOrThrow(userId);

        var page = PageRequest.of(from / size, size);
        var requests = requestRepository.findAllByRequesterIdIsNot(userId, page);

        var requestIds = ItemRequestUtils.toIdsSet(requests);
        var items = itemRepository.findAllByItemRequestIdIn(requestIds);

        var itemsByRequestId = ItemUtils.toItemsByRequestId(items);
        var requestsDto = requestMapper.mapToItemRequestDto(requests, itemsByRequestId);

        log.info("Item requests page with from: {} and size: {} returned, count: {}", from, size, requestsDto.size());
        log.debug("Item requests page returned: {}", requestsDto);

        return requestsDto;
    }
}
