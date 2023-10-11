package ru.practicum.shareit.itemrequest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.itemrequest.dto.request.ItemRequestCreationDto;
import ru.practicum.shareit.itemrequest.dto.response.ItemRequestDto;
import ru.practicum.shareit.itemrequest.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.itemrequest.repository.ItemRequestRepository;
import ru.practicum.shareit.itemrequest.utils.ItemRequestUtils;
import ru.practicum.shareit.user.repository.UserRepository;

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

        log.info("Item request with id: {} retrieved", request.getId());
        log.debug("Item request retrieved: {}", requestDto);

        return requestDto;
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByRequesterId(Long requesterId) {
        userRepository.existsByIdOrThrow(requesterId);

        var requests = requestRepository.findAllByRequesterId(requesterId);
        var requestIds = ItemRequestUtils.toIdsSet(requests);
        var items = itemRepository.findAllByItemRequestIdIn(requestIds);
        var requestsDto = requestMapper.mapToItemRequestDto(requests, items);

        log.info("Item requests for user with id: {} returned, count: {}", requesterId, requestsDto.size());
        log.debug("Item requests for user with id: {} returned, {}", requesterId, requestsDto);

        return requestsDto;
    }

    @Override
    public List<ItemRequestDto> getItemRequests(Long userId, Integer from, Integer size) {
        userRepository.existsByIdOrThrow(userId);

        var page = PageRequest.of(from / size, size);
        var requests = requestRepository.findAllByRequesterIdIsNot(userId, page);
        var requestsIds = ItemRequestUtils.toIdsSet(requests);
        var items = itemRepository.findAllByItemRequestIdIn(requestsIds);
        var requestsDto = requestMapper.mapToItemRequestDto(requests, items);

        log.info("Item requests page returned: (from: {}, size: {}), count: {}", from, size, requestsDto.size());
        log.debug("Item requests page returned: {}", requestsDto);

        return requestsDto;
    }
}
