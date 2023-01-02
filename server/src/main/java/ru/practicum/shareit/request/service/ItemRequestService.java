package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.request.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.response.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(RequestItemRequestDto requestDto, Long userId);

    List<ItemRequestDto> getItemRequestsByRequesterId(Long userId);

    List<ItemRequestDto> getItemRequestsByRequesterId(Long userId, Integer from, Integer size);

    ItemRequestDto getItemRequestById(Long itemRequestId, Long userId);
}
