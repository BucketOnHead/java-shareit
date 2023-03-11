package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.request.ItemRequestDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class ItemDtoMapper {

    public static Item toItemWithoutItemRequest(ItemRequestDto itemDto, User owner) {
        Item item = new Item();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        return item;
    }

    public static Item toItem(ItemRequestDto itemDto) {
        Item item = new Item();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());

        return item;
    }

    public static SimpleItemResponseDto toSimpleItemResponseDto(Item item, Long requestId) {
        SimpleItemResponseDto itemDto = new SimpleItemResponseDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setRequestId(requestId);

        return itemDto;
    }

    public static ItemDetailsResponseDto toDetailedItemDto(
            Item item,
            List<Comment> comments,
            Booking lastBooking,
            Booking nextBooking
    ) {
        ItemDetailsResponseDto itemDto = toDetailedItemDtoWithoutBookings(item, comments);

        itemDto.setLastBooking(toBookingDtoForDetailedItemDto(lastBooking));
        itemDto.setNextBooking(toBookingDtoForDetailedItemDto(nextBooking));

        return itemDto;
    }

    public static ItemDetailsResponseDto toDetailedItemDtoWithoutBookings(Item item, List<Comment> comments) {
        ItemDetailsResponseDto itemDto = new ItemDetailsResponseDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setComments(toCommentDtoForDetailedItemDto(comments));

        return itemDto;
    }

    public static ItemDetailsResponseDto toDetailedItemDtoWithoutLastBooking(Item item, List<Comment> comments,
                                                                             Booking nextBooking) {
        ItemDetailsResponseDto itemDto = toDetailedItemDtoWithoutBookings(item, comments);

        itemDto.setNextBooking(toBookingDtoForDetailedItemDto(nextBooking));

        return itemDto;
    }

    public static ItemDetailsResponseDto toDetailedItemDtoWithoutNextBooking(Item item, List<Comment> comments,
                                                                             Booking lastBooking) {
        ItemDetailsResponseDto itemDto = toDetailedItemDtoWithoutBookings(item, comments);

        itemDto.setLastBooking(toBookingDtoForDetailedItemDto(lastBooking));

        return itemDto;
    }

    public static List<SimpleItemResponseDto> toSimpleItemResponseDto(Collection<Item> items) {
        return items.stream()
                .map(item -> ItemDtoMapper.toSimpleItemResponseDto(item, null))
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
