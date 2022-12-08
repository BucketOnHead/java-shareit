package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.in.ItemCreationRequestDto;
import ru.practicum.shareit.item.dto.in.ItemUpdateRequestDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemCreationRequestDto itemDto, Long ownerId);

    ItemDto updateItem(ItemUpdateRequestDto itemDto, Long itemId, Long ownerId);

    DetailedItemDto getItemByItemId(Long itemId, Long userId);

    List<DetailedItemDto> getItemsByOwnerId(Long ownerId);

    List<ItemDto> searchItemsByNameOrDescription(String text);
}
