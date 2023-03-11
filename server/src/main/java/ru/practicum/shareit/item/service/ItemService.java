package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.request.ItemRequestDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemDto;

import java.util.List;

public interface ItemService {
    SimpleItemDto addItem(ItemRequestDto itemDto, Long ownerUserId);

    SimpleItemDto updateItem(ItemRequestDto itemDto, Long itemId, Long currentUserId);

    ItemDetailsResponseDto getItemById(Long itemId, Long currentUserId);

    List<ItemDetailsResponseDto> getItemsByOwnerUserId(Long ownerUserId, Integer from, Integer size);

    List<SimpleItemDto> searchItemsByNameOrDescriptionIgnoreCase(String text, Integer from, Integer size);
}
