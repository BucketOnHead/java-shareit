package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.request.ItemRequestDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "itemRequest", ignore = true)
    @Mapping(target = "isAvailable", source = "available")
    Item mapToItem(ItemRequestDto itemDto);

    @Mapping(target = "available", source = "isAvailable")
    @Mapping(target = "requestId", source = "item.itemRequest.id")
    SimpleItemResponseDto mapToSimpleItemResponseDto(Item item);

    List<SimpleItemResponseDto> mapToSimpleItemResponseDto(Iterable<Item> items);

    @Mapping(target = "authorName", source = "comment.author.name")
    ItemDetailsResponseDto.CommentDto mapToCommentDto(Comment comment);

    List<ItemDetailsResponseDto.CommentDto> mapToCommentDto(Iterable<Comment> comment);

    @Mapping(target = "bookerId", source = "booking.booker.id")
    ItemDetailsResponseDto.BookingDto mapToBookingDto(Booking booking);

    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "available", source = "item.isAvailable")
    ItemDetailsResponseDto mapToItemDetailsResponseDto(Item item, Iterable<Comment> comments);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "available", source = "item.isAvailable")
    @Mapping(target = "lastBooking", source = "last")
    @Mapping(target = "nextBooking", source = "next")
    ItemDetailsResponseDto mapToItemDetailsResponseDto(Item item, Iterable<Comment> comments,
                                                       Booking last, Booking next);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "available", source = "item.isAvailable")
    @Mapping(target = "lastBooking", source = "last")
    @Mapping(target = "nextBooking", source = "next")
    @Mapping(target = "comments", ignore = true)
    ItemDetailsResponseDto mapToItemDetailsResponseDto(Item item, Booking last, Booking next);
}
