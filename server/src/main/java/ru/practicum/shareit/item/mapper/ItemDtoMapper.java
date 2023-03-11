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

@UtilityClass
public final class ItemDtoMapper {

    public static Item toItem(ItemRequestDto itemDto) {
        Item item = new Item();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());

        return item;
    }

    public static SimpleItemResponseDto toSimpleItemResponseDto(Item item) {
        SimpleItemResponseDto itemDto = new SimpleItemResponseDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());

        if (item.getItemRequest() != null) {
            itemDto.setRequestId(item.getItemRequest().getId());
        }

        return itemDto;
    }

    public static ItemDetailsResponseDto toItemDetailsResponseDto(
            Item item,
            List<Comment> comments,
            Booking lastBooking,
            Booking nextBooking
    ) {
        ItemDetailsResponseDto itemDto = toItemDetailsResponseDto(item, comments);

        if (lastBooking != null) {
            itemDto.setLastBooking(toBookingDtoForDetailedItemDto(lastBooking));
        }

        if (nextBooking != null) {
            itemDto.setNextBooking(toBookingDtoForDetailedItemDto(nextBooking));
        }

        return itemDto;
    }

    public static ItemDetailsResponseDto toItemDetailsResponseDto(Item item, List<Comment> comments) {
        ItemDetailsResponseDto itemDto = new ItemDetailsResponseDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setComments(toCommentDtoForDetailedItemDto(comments));

        return itemDto;
    }

    public static List<SimpleItemResponseDto> toSimpleItemResponseDto(Collection<Item> items) {
        return items.stream()
                .map(item -> ItemDtoMapper.toSimpleItemResponseDto(item))
                .collect(Collectors.toList());
    }

    private static ItemDetailsResponseDto.BookingDto toBookingDtoForDetailedItemDto(Booking booking) {
        ItemDetailsResponseDto.BookingDto bookingDto = new ItemDetailsResponseDto.BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setBookerId(booking.getBooker().getId());

        return bookingDto;
    }

    private static ItemDetailsResponseDto.CommentDto toCommentDtoForDetailedItemDto(Comment comment) {
        ItemDetailsResponseDto.CommentDto commentDto = new ItemDetailsResponseDto.CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

    private static List<ItemDetailsResponseDto.CommentDto> toCommentDtoForDetailedItemDto(Collection<Comment> comments) {
        return comments.stream()
                .map(ItemDtoMapper::toCommentDtoForDetailedItemDto)
                .collect(Collectors.toList());
    }
}
