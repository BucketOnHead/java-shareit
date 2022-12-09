package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.in.RequestItemDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mapper.ItemDtoMapper.*;
import static ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public static void checkItemExistsById(ItemRepository itemRepository, Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw ItemNotFoundException.getFromItemId(itemId);
        }
    }

    public static void checkOwnerOfItemByItemIdAndUserId(ItemRepository itemRepository,
                                                         Long itemId, Long userId) {
        Long ownerId = itemRepository.getReferenceById(itemId).getOwner().getId();
        if (!ownerId.equals(userId)) {
            throw ItemNotFoundException.getFromItemIdAndUserId(itemId, userId);
        }
    }

    @Override
    @Transactional
    public ItemDto addItem(RequestItemDto itemDto, Long ownerId) {
        checkUserExistsById(userRepository, ownerId);
        Item item = getItem(itemDto, ownerId);
        Item addedItem = itemRepository.save(item);
        log.debug("Item ID_{} added.", addedItem.getId());
        return toItemDto(addedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(RequestItemDto itemDto, Long itemId, Long userId) {
        checkItemExistsById(itemRepository, itemId);
        checkUserExistsById(userRepository, userId);
        checkOwnerOfItemByItemIdAndUserId(itemRepository, itemId, userId);
        Item item = getItem(itemDto, itemId, userId);
        Item updatedItem = itemRepository.save(item);
        log.debug("Item ID_{} updated.", itemId);
        return toItemDto(updatedItem);
    }

    @Override
    public DetailedItemDto getItemByItemId(Long itemId, Long userId) {
        checkItemExistsById(itemRepository, itemId);
        Item item = itemRepository.getReferenceById(itemId);
        log.debug("Item ID_{} returned.", item.getId());
        if (isOwner(item, userId)) {
            return getDetailedItemDto(item);
        }
        return getDetailedItemDtoWithoutBookings(item);
    }

    @Override
    public List<DetailedItemDto> getItemsByOwnerId(Long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        log.debug("All items have been returned, {} in total.", items.size());
        return items.stream()
                .map(this::getDetailedItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItemsByNameOrDescription(String text) {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        }

        List<Item> foundItems = itemRepository.findByIsAvailableIsTrue()
                .stream()
                .filter(itemDto -> StringUtils.containsIgnoreCase(itemDto.getName(), text)
                        || StringUtils.containsIgnoreCase(itemDto.getDescription(), text))
                .collect(Collectors.toList());
        log.debug("Returned items containing '{}', {} in total.", text, foundItems.size());

        return toItemDto(foundItems);
    }

    private Item getItem(RequestItemDto itemDto, Long itemId, Long ownerId) {
        Item item = itemRepository.getReferenceById(itemId);
        User owner = userRepository.getReferenceById(ownerId);
        return toItem(itemDto, item, owner);
    }

    private Item getItem(RequestItemDto itemDto, Long ownerId) {
        User owner = userRepository.getReferenceById(ownerId);
        return toItem(itemDto, owner);
    }

    private DetailedItemDto getDetailedItemDtoWithoutBookings(Item item) {
        List<Comment> comments = commentRepository.findCommentsByItemId(item.getId());
        return toDetailedItemDtoWithoutBookings(item, comments);
    }

    private DetailedItemDto getDetailedItemDto(Item item) {
        List<Comment> comments = commentRepository.findCommentsByItemId(item.getId());

        LocalDateTime time = LocalDateTime.now();
        Booking lastBooking = bookingRepository
                .findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc(item.getId(), time)
                .orElse(null);
        Booking nextBooking = bookingRepository
                .findFirstByItemIdAndStartTimeIsAfter(item.getId(), time)
                .orElse(null);

        return toDetailedItemDto(item, comments, lastBooking, nextBooking);
    }

    private static boolean isOwner(Item item, Long userId) {
        Long ownerId = item.getOwner().getId();
        return ownerId.equals(userId);
    }
}
