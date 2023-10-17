package ru.practicum.shareit.server.item.service;

import ru.practicum.shareit.server.dto.item.request.ItemCreationDto;
import ru.practicum.shareit.server.dto.item.response.ItemDetailsDto;
import ru.practicum.shareit.server.dto.item.response.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemCreationDto itemDto, Long ownerId);

    ItemDetailsDto getItemById(Long itemId, Long userId);

    List<ItemDetailsDto> getItemsByUserId(Long userId, Integer from, Integer size);

    List<ItemDto> getItemsByText(String text, Integer from, Integer size);

    ItemDto updateItem(ItemCreationDto itemDto, Long itemId, Long userId);
}
