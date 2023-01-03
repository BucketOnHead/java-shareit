package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.request.RequestItemDto;
import ru.practicum.shareit.item.dto.response.DetailedItemDto;
import ru.practicum.shareit.item.dto.response.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public final class ItemDtoMapper {
    private ItemDtoMapper() {
        throw new AssertionError("This is a utility class and cannot be instantiated");
    }

    // ╔══╗───╔═══╗───╔══╗───╔╗──╔╗──────╔══╗────╔════╗───╔══╗
    // ║╔═╝───║╔═╗║───║╔╗║───║║──║║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ║╚═╗───║╚═╝║───║║║║───║╚╗╔╝║──────║║╚╗║─────║║─────║║║║
    // ║╔═╝───║╔╗╔╝───║║║║───║╔╗╔╗║──────║║─║║─────║║─────║║║║
    // ║║─────║║║║────║╚╝║───║║╚╝║║──────║╚═╝║─────║║─────║╚╝║
    // ╚╝─────╚╝╚╝────╚══╝───╚╝──╚╝──────╚═══╝─────╚╝─────╚══╝

    public static Item toItemWithoutItemRequest(RequestItemDto itemDto, User owner) {
        Item item = new Item();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        return item;
    }

    public static Item toItem(RequestItemDto itemDto, User owner, ItemRequest itemRequest) {
        Item item = toItemWithoutItemRequest(itemDto, owner);

        item.setItemRequest(itemRequest);

        return item;
    }

    // ╔════╗───╔══╗──────╔══╗────╔════╗───╔══╗
    // ╚═╗╔═╝───║╔╗║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ──║║─────║║║║──────║║╚╗║─────║║─────║║║║
    // ──║║─────║║║║──────║║─║║─────║║─────║║║║
    // ──║║─────║╚╝║──────║╚═╝║─────║║─────║╚╝║
    // ──╚╝─────╚══╝──────╚═══╝─────╚╝─────╚══╝

    public static ItemDto toItemDto(Item item, Long requestId) {
        ItemDto itemDto = toItemDtoWithoutItemRequestId(item);

        itemDto.setRequestId(requestId);

        return itemDto;
    }

    public static ItemDto toItemDtoWithoutItemRequestId(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());

        return itemDto;
    }

    public static DetailedItemDto toDetailedItemDto(Item item, List<Comment> comments,
                                                    Booking lastBooking, Booking nextBooking) {
        DetailedItemDto itemDto = toDetailedItemDtoWithoutBookings(item, comments);

        itemDto.setLastBooking(toBookingDtoForDetailedItemDto(lastBooking));
        itemDto.setNextBooking(toBookingDtoForDetailedItemDto(nextBooking));

        return itemDto;
    }

    public static DetailedItemDto toDetailedItemDtoWithoutBookings(Item item, List<Comment> comments) {
        DetailedItemDto itemDto = new DetailedItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setComments(toCommentDtoForDetailedItemDto(comments));

        return itemDto;
    }

    public static DetailedItemDto toDetailedItemDtoWithoutLastBooking(Item item, List<Comment> comments,
                                                                      Booking nextBooking) {
        DetailedItemDto itemDto = toDetailedItemDtoWithoutBookings(item, comments);

        itemDto.setNextBooking(toBookingDtoForDetailedItemDto(nextBooking));

        return itemDto;
    }

    public static DetailedItemDto toDetailedItemDtoWithoutNextBooking(Item item, List<Comment> comments,
                                                                      Booking lastBooking) {
        DetailedItemDto itemDto = toDetailedItemDtoWithoutBookings(item, comments);

        itemDto.setLastBooking(toBookingDtoForDetailedItemDto(lastBooking));

        return itemDto;
    }

    // ╔════╗───╔══╗──────╔══╗────╔════╗───╔══╗──────╔╗─────╔══╗───╔══╗───╔════╗
    // ╚═╗╔═╝───║╔╗║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║──────║║─────╚╗╔╝───║╔═╝───╚═╗╔═╝
    // ──║║─────║║║║──────║║╚╗║─────║║─────║║║║──────║║──────║║────║╚═╗─────║║──
    // ──║║─────║║║║──────║║─║║─────║║─────║║║║──────║║──────║║────╚═╗║─────║║──
    // ──║║─────║╚╝║──────║╚═╝║─────║║─────║╚╝║──────║╚═╗───╔╝╚╗───╔═╝║─────║║──
    // ──╚╝─────╚══╝──────╚═══╝─────╚╝─────╚══╝──────╚══╝───╚══╝───╚══╝─────╚╝──

    public static List<ItemDto> toItemDto(Collection<Item> items) {
        return items.stream()
                .map(ItemDtoMapper::toItemDtoWithoutItemRequestId)
                .collect(Collectors.toList());
    }

    private static DetailedItemDto.BookingDto toBookingDtoForDetailedItemDto(Booking booking) {
        DetailedItemDto.BookingDto bookingDto = new DetailedItemDto.BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setBookerId(booking.getBooker().getId());

        return bookingDto;
    }

    private static DetailedItemDto.CommentDto toCommentDtoForDetailedItemDto(Comment comment) {
        DetailedItemDto.CommentDto commentDto = new DetailedItemDto.CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

    private static List<DetailedItemDto.CommentDto> toCommentDtoForDetailedItemDto(Collection<Comment> comments) {
        return comments.stream()
                .map(ItemDtoMapper::toCommentDtoForDetailedItemDto)
                .collect(Collectors.toList());
    }
}
