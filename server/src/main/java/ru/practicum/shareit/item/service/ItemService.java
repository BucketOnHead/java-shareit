package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.request.ItemCreationDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsDto;
import ru.practicum.shareit.item.dto.response.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemCreationDto itemDto, Long ownerId);

    ItemDetailsDto getItemById(Long itemId, Long userId);

    List<ItemDetailsDto> getItemsByUserId(Long userId, Integer from, Integer size);

    List<ItemDto> getItemsByText(String text, Integer from, Integer size);

    ItemDto updateItem(ItemCreationDto itemDto, Long itemId, Long userId);
}
