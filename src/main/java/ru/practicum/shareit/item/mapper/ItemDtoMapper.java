package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.in.RequestItemDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto.ItemBookingDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto.ItemCommentDto;
import ru.practicum.shareit.item.dto.out.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ItemDtoMapper {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    // ╔══╗───╔═══╗───╔══╗───╔╗──╔╗──────╔══╗────╔════╗───╔══╗
    // ║╔═╝───║╔═╗║───║╔╗║───║║──║║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ║╚═╗───║╚═╝║───║║║║───║╚╗╔╝║──────║║╚╗║─────║║─────║║║║
    // ║╔═╝───║╔╗╔╝───║║║║───║╔╗╔╗║──────║║─║║─────║║─────║║║║
    // ║║─────║║║║────║╚╝║───║║╚╝║║──────║╚═╝║─────║║─────║╚╝║
    // ╚╝─────╚╝╚╝────╚══╝───╚╝──╚╝──────╚═══╝─────╚╝─────╚══╝

    public Item toItem(RequestItemDto itemDto, Long ownerId) {
        Item item = new Item();

        User owner = userRepository.getReferenceById(ownerId);

        itemDto.getName().ifPresent(item::setName);
        itemDto.getDescription().ifPresent(item::setDescription);
        itemDto.getAvailable().ifPresent(item::setIsAvailable);
        item.setOwner(owner);

        return item;
    }

    public Item toItem(RequestItemDto itemDto, Long itemId, Long ownerId) {
        Item item = itemRepository.getReferenceById(itemId);

        User owner = userRepository.getReferenceById(ownerId);

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

    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());

        return itemDto;
    }

    public DetailedItemDto toDetailedItemDtoWithoutBookings(Item item) {
        DetailedItemDto itemDto = new DetailedItemDto();

        List<Comment> comments = commentRepository.findCommentsByItemId(item.getId());

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setComments(toItemCommentDto(comments));

        return itemDto;
    }

    public DetailedItemDto toDetailedItemDto(Item item) {
        DetailedItemDto itemDto = toDetailedItemDtoWithoutBookings(item);

        LocalDateTime time = LocalDateTime.now();
        getLastBooking(item.getId(), time).ifPresent(itemDto::setLastBooking);
        getNextBooking(item.getId(), time).ifPresent(itemDto::setNextBooking);

        return itemDto;
    }

    public List<ItemDto> toItemDto(Collection<Item> items) {
        return items.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    public List<DetailedItemDto> toDetailedItemDto(Collection<Item> items) {
        return items.stream()
                .map(this::toDetailedItemDto)
                .collect(Collectors.toList());
    }

    private Optional<ItemBookingDto> getLastBooking(Long itemId, LocalDateTime time) {
        var lastBooking =
                bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc(itemId, time);
        return lastBooking.map(this::toItemBookingDto);
    }

    private Optional<ItemBookingDto> getNextBooking(Long itemId, LocalDateTime time) {
        var lastBooking =
                bookingRepository.findFirstByItemIdAndStartTimeIsAfter(itemId, time);
        return lastBooking.map(this::toItemBookingDto);
    }

    private ItemBookingDto toItemBookingDto(Booking booking) {
        ItemBookingDto shortBookingDto = new ItemBookingDto();

        shortBookingDto.setId(booking.getId());
        shortBookingDto.setBookerId(booking.getBooker().getId());

        return shortBookingDto;
    }

    private ItemCommentDto toItemCommentDto(Comment comment) {
        ItemCommentDto commentDto = new ItemCommentDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

    private List<ItemCommentDto> toItemCommentDto(Collection<Comment> comments) {
        return comments.stream()
                .map(this::toItemCommentDto)
                .collect(Collectors.toList());
    }
}
