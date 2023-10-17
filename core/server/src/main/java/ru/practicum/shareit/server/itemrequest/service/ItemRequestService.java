package ru.practicum.shareit.server.itemrequest.service;

import ru.practicum.shareit.server.dto.itemrequest.request.ItemRequestCreationDto;
import ru.practicum.shareit.server.dto.itemrequest.response.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(ItemRequestCreationDto requestDto, Long userId);

    ItemRequestDto getItemRequestById(Long requestId, Long userId);

    List<ItemRequestDto> getItemRequestsByRequesterId(Long userId);

    List<ItemRequestDto> getItemRequests(Long userId, Integer from, Integer size);
}
