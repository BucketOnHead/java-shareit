package ru.practicum.shareit.itemrequest.service;

import ru.practicum.shareit.itemrequest.dto.request.ItemRequestCreationDto;
import ru.practicum.shareit.itemrequest.dto.response.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(ItemRequestCreationDto requestDto, Long userId);

    ItemRequestDto getItemRequestById(Long requestId, Long userId);
    
    List<ItemRequestDto> getItemRequestsByRequesterId(Long userId);

    List<ItemRequestDto> getItemRequests(Long userId, Integer from, Integer size);
}
