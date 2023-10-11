package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.request.ItemCreationDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsDto;
import ru.practicum.shareit.item.dto.response.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemCreationDto itemDto, Long ownerUserId);

    ItemDto updateItem(ItemCreationDto itemDto, Long itemId, Long currentUserId);

    ItemDetailsDto getItemById(Long itemId, Long currentUserId);

    List<ItemDetailsDto> getItemsByUserId(Long ownerUserId, Integer from, Integer size);

    List<ItemDto> getItemsByText(String text, Integer from, Integer size);
}
