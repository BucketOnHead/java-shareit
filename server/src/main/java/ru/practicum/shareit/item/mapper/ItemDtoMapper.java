package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.request.ItemRequestDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@link ItemDtoMapper} class provides methods to convert
 * between {@link Item}-related DTOs and {@link Item} entities.
 */
@UtilityClass
public final class ItemDtoMapper {

    /**
     * Converts an {@link ItemRequestDto} to an {@link Item} entity.
     *
     * @param itemDto The {@link ItemRequestDto} to convert.
     * @return The resulting {@link Item} entity.
     */
    public static Item toItem(ItemRequestDto itemDto) {
        Item item = new Item();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());

        return item;
    }

    /**
     * Converts an {@link Item} entity to a {@link SimpleItemResponseDto}.
     *
     * @param item The {@link Item} entity to convert.
     * @return The resulting {@link SimpleItemResponseDto}.
     */
    public static SimpleItemResponseDto toSimpleItemResponseDto(Item item) {
        var itemDto = new SimpleItemResponseDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());

        if (item.getItemRequest() != null) {
            itemDto.setRequestId(item.getItemRequest().getId());
        }

        return itemDto;
    }

    /**
     * Converts an {@link Item} entity to an {@link ItemDetailsResponseDto}.
     *
     * @param item        The {@link Item} entity to convert.
     * @param comments    The comments associated with the item.
     * @param lastBooking The last booking associated with the item.
     * @param nextBooking The next booking associated with the item.
     * @return The resulting {@link ItemDetailsResponseDto}.
     */
    public static ItemDetailsResponseDto toItemDetailsResponseDto(
            Item item,
            List<Comment> comments,
            Booking lastBooking,
            Booking nextBooking
    ) {
        var itemDto = toItemDetailsResponseDto(item, comments);

        if (lastBooking != null) {
            var bookingDto = ItemDetailsResponseDto.BookingDto.fromBooking(lastBooking);
            itemDto.setLastBooking(bookingDto);
        }

        if (nextBooking != null) {
            var bookingDto = ItemDetailsResponseDto.BookingDto.fromBooking(nextBooking);
            itemDto.setNextBooking(bookingDto);
        }

        return itemDto;
    }

    /**
     * Converts an {@link Item} entity to an {@link ItemDetailsResponseDto}.
     *
     * @param item     The {@link Item} entity to convert.
     * @param comments The comments associated with the item.
     * @return The resulting {@link ItemDetailsResponseDto}.
     */
    public static ItemDetailsResponseDto toItemDetailsResponseDto(Item item, List<Comment> comments) {
        var itemDto = new ItemDetailsResponseDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setComments(ItemDetailsResponseDto.CommentDto.fromComment(comments));

        return itemDto;
    }

    /**
     * Converts a collection of {@link Item} entities to a list
     * of {@link SimpleItemResponseDto}s.
     *
     * @param items The collection of {@link Item} entities to convert.
     * @return The resulting list of {@link SimpleItemResponseDto}s.
     */
    public static List<SimpleItemResponseDto> toSimpleItemResponseDto(Collection<Item> items) {
        return items.stream()
                .map(ItemDtoMapper::toSimpleItemResponseDto)
                .collect(Collectors.toList());
    }
}
