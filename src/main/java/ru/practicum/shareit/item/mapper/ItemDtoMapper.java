package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.out.ShortBookingDto;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.in.ItemCreationRequestDto;
import ru.practicum.shareit.item.dto.in.ItemUpdateRequestDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.ItemDto;
import ru.practicum.shareit.item.dto.out.ShortItemDto;
import ru.practicum.shareit.item.mapper.comment.CommentDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.comment.Comment;
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

    public Item toItem(ItemCreationRequestDto itemDto, Long ownerId) {
        Item item = new Item();

        User owner = userRepository.getReferenceById(ownerId);

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        return item;
    }

    public Item toItem(ItemUpdateRequestDto itemDto, Long itemId, Long ownerId) {
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

    public ShortItemDto toShortItemDto(Item item) {
        ShortItemDto itemDto = new ShortItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());

        return itemDto;
    }

    public DetailedItemDto toDetailedItemDto(Item item, CommentDtoMapper commentDtoMapper) {
        DetailedItemDto itemDto = new DetailedItemDto();

        List<Comment> comments = commentRepository.findCommentsByItemId(item.getId());

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setComments(commentDtoMapper.toCommentDto(comments));

        return itemDto;
    }

    public DetailedItemDto toDetailedItemDto(Item item,
                                             CommentDtoMapper commentDtoMapper,
                                             BookingDtoMapper bookingDtoMapper) {
        DetailedItemDto itemDto = toDetailedItemDto(item, commentDtoMapper);

        LocalDateTime time = LocalDateTime.now();
        getLastBooking(bookingDtoMapper, item.getId(), time).ifPresent(itemDto::setLastBooking);
        getNextBooking(bookingDtoMapper, item.getId(), time).ifPresent(itemDto::setNextBooking);

        return itemDto;
    }

    public List<ItemDto> toItemDto(Collection<Item> items) {
        return items.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    public List<DetailedItemDto> toDetailedItemDto(Collection<Item> items,
                                                   CommentDtoMapper commentDtoMapper,
                                                   BookingDtoMapper bookingDtoMapper) {
        return items.stream()
                .map(item -> toDetailedItemDto(item, commentDtoMapper, bookingDtoMapper))
                .collect(Collectors.toList());
    }

    private Optional<ShortBookingDto> getLastBooking(BookingDtoMapper bookingDtoMapper,
                                                     Long itemId, LocalDateTime time) {
        var lastBooking =
                bookingRepository.findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc(itemId, time);
        return lastBooking.map(bookingDtoMapper::toShortBookingDto);
    }

    private Optional<ShortBookingDto> getNextBooking(BookingDtoMapper bookingDtoMapper,
                                                     Long itemId, LocalDateTime time) {
        var lastBooking =
                bookingRepository.findFirstByItemIdAndStartTimeIsAfter(itemId, time);
        return lastBooking.map(bookingDtoMapper::toShortBookingDto);
    }
}
