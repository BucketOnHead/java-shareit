package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.in.RequestItemDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(RequestItemDto itemDto, Long ownerId);

    ItemDto updateItem(RequestItemDto itemDto, Long itemId, Long ownerId);

    DetailedItemDto getItemById(Long itemId, Long userId);

    List<DetailedItemDto> getItemsByOwnerId(Long ownerId);

    List<ItemDto> searchItemsByNameOrDescription(String text);
}
