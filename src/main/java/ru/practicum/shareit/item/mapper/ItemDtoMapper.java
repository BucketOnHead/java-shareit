package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.in.RequestItemDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto.ItemBookingDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto.ItemCommentDto;
import ru.practicum.shareit.item.dto.out.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@UtilityClass
public class ItemDtoMapper {

    // ╔══╗───╔═══╗───╔══╗───╔╗──╔╗──────╔══╗────╔════╗───╔══╗
    // ║╔═╝───║╔═╗║───║╔╗║───║║──║║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ║╚═╗───║╚═╝║───║║║║───║╚╗╔╝║──────║║╚╗║─────║║─────║║║║
    // ║╔═╝───║╔╗╔╝───║║║║───║╔╗╔╗║──────║║─║║─────║║─────║║║║
    // ║║─────║║║║────║╚╝║───║║╚╝║║──────║╚═╝║─────║║─────║╚╝║
    // ╚╝─────╚╝╚╝────╚══╝───╚╝──╚╝──────╚═══╝─────╚╝─────╚══╝

    public static Item toItem(RequestItemDto itemDto, User owner) {
        Item item = new Item();

        itemDto.getName().ifPresent(item::setName);
        itemDto.getDescription().ifPresent(item::setDescription);
        itemDto.getAvailable().ifPresent(item::setIsAvailable);
        item.setOwner(owner);

        return item;
    }

    public static Item toItem(RequestItemDto itemDto, Item item, User owner) {
        itemDto.getName().ifPresent(item::setName);
        itemDto.getDescription().ifPresent(item::setDescription);
        itemDto.getAvailable().ifPresent(item::setIsAvailable);
        item.setOwner(owner);

        return item;
    }

    // ╔════╗───╔══╗──────╔══╗────╔════╗───╔══╗
    // ╚═╗╔═╝───║╔╗║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ──║║─────║║║║──────║║╚╗║─────║║─────║║║║
    // ──║║─────║║║║──────║║─║║─────║║─────║║║║
    // ──║║─────║╚╝║──────║╚═╝║─────║║─────║╚╝║
    // ──╚╝─────╚══╝──────╚═══╝─────╚╝─────╚══╝

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());

        return itemDto;
    }

    public static DetailedItemDto toDetailedItemDtoWithoutBookings(Item item, List<Comment> comments) {
        DetailedItemDto itemDto = new DetailedItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setComments(toItemCommentDto(comments));

        return itemDto;
    }

    public static DetailedItemDto toDetailedItemDto(Item item, List<Comment> comments,
                                                    Booking lastBooking, Booking nextBooking) {
        DetailedItemDto itemDto = toDetailedItemDtoWithoutBookings(item, comments);

        Optional.ofNullable(lastBooking)
                .map(ItemDtoMapper::toItemBookingDto)
                .ifPresent(itemDto::setLastBooking);
        Optional.ofNullable(nextBooking)
                .map(ItemDtoMapper::toItemBookingDto)
                .ifPresent(itemDto::setNextBooking);

        return itemDto;
    }

    public static List<ItemDto> toItemDto(Collection<Item> items) {
        return items.stream()
                .map(ItemDtoMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private static ItemBookingDto toItemBookingDto(Booking booking) {
        ItemBookingDto bookingDto = new ItemBookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setBookerId(booking.getBooker().getId());

        return bookingDto;
    }

    private static ItemCommentDto toItemCommentDto(Comment comment) {
        ItemCommentDto commentDto = new ItemCommentDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

    private static List<ItemCommentDto> toItemCommentDto(Collection<Comment> comments) {
        return comments.stream()
                .map(ItemDtoMapper::toItemCommentDto)
                .collect(Collectors.toList());
    }
}
