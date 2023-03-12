package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.request.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.response.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * A utility class for mapping {@link ItemRequest} objects and their DTOs.
 */

@UtilityClass
public final class ItemRequestDtoMapper {

    /**
     * Converts a {@link RequestItemRequestDto} object to an {@link  ItemRequest} object.
     *
     * @param itemRequestDto the {@link RequestItemRequestDto} to convert.
     * @param requester      the {@link User} who made the request.
     * @param time           the LocalDateTime when the request was created.
     * @return the converted {@link ItemRequest} object.
     */
    public static ItemRequest toItemRequest(RequestItemRequestDto itemRequestDto,
                                            User requester, LocalDateTime time) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequester(requester);
        itemRequest.setCreationTime(time);

        return itemRequest;
    }

    /**
     * Converts an {@link ItemRequest} object to an
     * {@link ItemRequestDto} object without its items.
     *
     * @param itemRequest the {@link ItemRequest} to convert.
     * @return the converted {@link ItemRequestDto} object.
     */
    public static ItemRequestDto toItemRequestDtoWithoutItems(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreationTime());

        return itemRequestDto;
    }

    /**
     * Converts an {@link ItemRequest} object and a collection
     * of its items to an {@link ItemRequestDto} object.
     *
     * @param itemRequest the {@link ItemRequest} to convert.
     * @param items       the collection of {@link Item} objects
     *                    associated with the request.
     * @return the converted {@link ItemRequestDto} object.
     */
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, Collection<Item> items) {
        ItemRequestDto itemRequestDto = toItemRequestDtoWithoutItems(itemRequest);

        itemRequestDto.setItems(ItemRequestDto.ItemDto.fromItem(items));

        return itemRequestDto;
    }
}
