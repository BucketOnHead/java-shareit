package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.request.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.response.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class ItemRequestDtoMapper {
    private ItemRequestDtoMapper() {
        throw new AssertionError("This is a utility class and cannot be instantiated");
    }

    // ╔══╗───╔═══╗───╔══╗───╔╗──╔╗──────╔══╗────╔════╗───╔══╗
    // ║╔═╝───║╔═╗║───║╔╗║───║║──║║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ║╚═╗───║╚═╝║───║║║║───║╚╗╔╝║──────║║╚╗║─────║║─────║║║║
    // ║╔═╝───║╔╗╔╝───║║║║───║╔╗╔╗║──────║║─║║─────║║─────║║║║
    // ║║─────║║║║────║╚╝║───║║╚╝║║──────║╚═╝║─────║║─────║╚╝║
    // ╚╝─────╚╝╚╝────╚══╝───╚╝──╚╝──────╚═══╝─────╚╝─────╚══╝

    public static ItemRequest toItemRequest(RequestItemRequestDto itemRequestDto,
                                            User requester, LocalDateTime time) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequester(requester);
        itemRequest.setCreationTime(time);

        return itemRequest;
    }

    // ╔════╗───╔══╗──────╔══╗────╔════╗───╔══╗
    // ╚═╗╔═╝───║╔╗║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ──║║─────║║║║──────║║╚╗║─────║║─────║║║║
    // ──║║─────║║║║──────║║─║║─────║║─────║║║║
    // ──║║─────║╚╝║──────║╚═╝║─────║║─────║╚╝║
    // ──╚╝─────╚══╝──────╚═══╝─────╚╝─────╚══╝

    public static ItemRequestDto toItemRequestDtoWithoutItems(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreationTime());

        return itemRequestDto;
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, Collection<Item> items) {
        ItemRequestDto itemRequestDto = toItemRequestDtoWithoutItems(itemRequest);

        itemRequestDto.setItems(toItemDtoForItemRequestDto(items));

        return itemRequestDto;
    }

    private static List<ItemRequestDto.ItemDto> toItemDtoForItemRequestDto(Collection<Item> items) {
        return items.stream()
                .map(ItemRequestDtoMapper::toItemDtoForItemRequestDto)
                .collect(Collectors.toList());
    }

    private static ItemRequestDto.ItemDto toItemDtoForItemRequestDto(Item item) {
        ItemRequestDto.ItemDto itemDto = new ItemRequestDto.ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setRequestId(item.getItemRequest().getId());

        return itemDto;
    }
}
