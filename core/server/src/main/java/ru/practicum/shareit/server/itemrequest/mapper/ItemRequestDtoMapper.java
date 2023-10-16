package ru.practicum.shareit.server.itemrequest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.server.dto.itemrequest.request.ItemRequestCreationDto;
import ru.practicum.shareit.server.dto.itemrequest.response.ItemRequestDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.itemrequest.model.ItemRequest;
import ru.practicum.shareit.server.user.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", nullValueMappingStrategy = RETURN_DEFAULT)
public interface ItemRequestDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    ItemRequest mapToItemRequest(ItemRequestCreationDto requestDto, User requester);

    @Mapping(target = "items", ignore = true)
    ItemRequestDto mapToItemRequestDto(ItemRequest request);

    ItemRequestDto mapToItemRequestDto(ItemRequest request, Iterable<Item> items);

    @Mapping(target = "available", source = "item.isAvailable")
    @Mapping(target = "requestId", source = "item.itemRequest.id")
    ItemRequestDto.ItemDto mapToItemRequestItemDto(Item item);

    List<ItemRequestDto.ItemDto> mapToItemRequestItemDto(Iterable<Item> item);

    default List<ItemRequestDto> mapToItemRequestDto(Iterable<ItemRequest> requests,
                                                     Map<Long, List<Item>> itemsByRequestId) {
        if (requests == null) {
            return Collections.emptyList();
        }

        List<ItemRequestDto> list = new ArrayList<>();
        for (var request : requests) {
            List<Item> items = null;
            if (itemsByRequestId != null) {
                items = itemsByRequestId.get(request.getId());
            }
            list.add(mapToItemRequestDto(request, items));
        }

        return list;
    }
}