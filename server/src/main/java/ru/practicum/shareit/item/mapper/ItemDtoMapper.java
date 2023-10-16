package ru.practicum.shareit.item.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.commondto.item.request.ItemCreationDto;
import ru.practicum.shareit.commondto.item.response.ItemDetailsDto;
import ru.practicum.shareit.commondto.item.response.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.itemrequest.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.NullValueMappingStrategy.RETURN_NULL;

@Mapper(componentModel = "spring", nullValueMappingStrategy = RETURN_DEFAULT)
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

    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "available", source = "item.isAvailable")
    ItemDetailsDto mapToItemDetailsDto(Item item, Iterable<Comment> comments);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "available", source = "item.isAvailable")
    @Mapping(target = "lastBooking", source = "last")
    @Mapping(target = "nextBooking", source = "next")
    ItemDetailsDto mapToItemDetailsDto(Item item, Iterable<Comment> comments, Booking last, Booking next);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "available", source = "item.isAvailable")
    @Mapping(target = "lastBooking", source = "last")
    @Mapping(target = "nextBooking", source = "next")
    @Mapping(target = "comments", ignore = true)
    ItemDetailsDto mapToItemDetailsDto(Item item, Booking last, Booking next);

    @Mapping(target = "authorName", source = "comment.author.name")
    @BeanMapping(nullValueMappingStrategy = RETURN_NULL)
    ItemDetailsDto.CommentDto mapToItemDetailsCommentDto(Comment comment);

    @BeanMapping(nullValueMappingStrategy = RETURN_NULL)
    List<ItemDetailsDto.CommentDto> mapToItemDetailsCommentDto(Iterable<Comment> comment);

    @Mapping(target = "bookerId", source = "booking.booker.id")
    @BeanMapping(nullValueMappingStrategy = RETURN_NULL)
    ItemDetailsDto.BookingDto mapToItemDetailsBookingDto(Booking booking);

    default List<ItemDetailsDto> mapToItemDetailsDto(Iterable<Item> items,
                                                     Map<Long, Booking> lastBookingByItemId,
                                                     Map<Long, Booking> nextBookingByItemId) {
        if (items == null) {
            return Collections.emptyList();
        }

        List<ItemDetailsDto> list = new ArrayList<>();

        for (var item : items) {
            Booking last = null;
            Booking next = null;
            if (lastBookingByItemId != null) {
                last = lastBookingByItemId.get(item.getId());
            }
            if (nextBookingByItemId != null) {
                next = nextBookingByItemId.get(item.getId());
            }

            list.add(mapToItemDetailsDto(item, last, next));
        }

        return list;
    }
}
