package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.request.ItemCreationDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsDto;
import ru.practicum.shareit.item.dto.response.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.itemrequest.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isAvailable", source = "itemDto.available")
    @Mapping(target = "name", source = "itemDto.name")
    @Mapping(target = "description", source = "itemDto.description")
    Item mapToItem(ItemCreationDto itemDto, User owner, ItemRequest itemRequest);

    @Mapping(target = "available", source = "isAvailable")
    @Mapping(target = "requestId", source = "item.itemRequest.id")
    ItemDto mapToItemDto(Item item);

    List<ItemDto> mapToItemDto(Iterable<Item> items);

    @Mapping(target = "authorName", source = "comment.author.name")
    ItemDetailsDto.CommentDto mapToCommentDto(Comment comment);

    List<ItemDetailsDto.CommentDto> mapToCommentDto(Iterable<Comment> comment);

    @Mapping(target = "bookerId", source = "booking.booker.id")
    ItemDetailsDto.BookingDto mapToBookingDto(Booking booking);

    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "available", source = "item.isAvailable")
    ItemDetailsDto mapToItemDetailsDto(Item item, Iterable<Comment> comments);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "available", source = "item.isAvailable")
    @Mapping(target = "lastBooking", source = "last")
    @Mapping(target = "nextBooking", source = "next")
    ItemDetailsDto mapToItemDetailsDto(Item item, Iterable<Comment> comments,
                                       Booking last, Booking next);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "available", source = "item.isAvailable")
    @Mapping(target = "lastBooking", source = "last")
    @Mapping(target = "nextBooking", source = "next")
    @Mapping(target = "comments", ignore = true)
    ItemDetailsDto mapToItemDetailsDto(Item item, Booking last, Booking next);
}
