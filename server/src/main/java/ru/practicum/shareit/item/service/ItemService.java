package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.request.ItemCreationDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemResponseDto;

import java.util.List;

public interface ItemService {
    SimpleItemResponseDto addItem(ItemCreationDto itemDto, Long ownerUserId);

    SimpleItemResponseDto updateItem(ItemCreationDto itemDto, Long itemId, Long currentUserId);

    ItemDetailsResponseDto getItemById(Long itemId, Long currentUserId);

    List<ItemDetailsResponseDto> getItemsByOwnerUserId(Long ownerUserId, Integer from, Integer size);

    List<SimpleItemResponseDto> searchItemsByNameOrDescriptionIgnoreCase(String text, Integer from, Integer size);
}
