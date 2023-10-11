package ru.practicum.shareit.itemrequest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.itemrequest.dto.request.ItemRequestCreationDto;
import ru.practicum.shareit.itemrequest.dto.response.ItemRequestDto;
import ru.practicum.shareit.itemrequest.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationTime", source = "time")
    ItemRequest mapToItemRequest(ItemRequestCreationDto requestDto, User requester, LocalDateTime time);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "created", source = "creationTime")
    ItemRequestDto mapToItemRequestDto(ItemRequest request);

    @Mapping(target = "created", source = "request.creationTime")
    ItemRequestDto mapToItemRequestDto(ItemRequest request, Iterable<Item> items);

    @Mapping(target = "available", source = "item.isAvailable")
    @Mapping(target = "requestId", source = "item.itemRequest.id")
    ItemRequestDto.ItemDto mapToItemRequestItemDto(Item item);

    List<ItemRequestDto.ItemDto> mapToItemRequestItemDto(Iterable<Item> item);
}
