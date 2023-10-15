package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.utils.BookingUtils;
import ru.practicum.shareit.commondto.item.request.ItemCreationDto;
import ru.practicum.shareit.commondto.item.response.ItemDetailsDto;
import ru.practicum.shareit.commondto.item.response.ItemDto;
import ru.practicum.shareit.item.exception.ItemAccessException;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.item.utils.ItemUtils;
import ru.practicum.shareit.itemrequest.model.ItemRequest;
import ru.practicum.shareit.itemrequest.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemDtoMapper itemMapper;

    @Override
    @Transactional
    public ItemDto addItem(ItemCreationDto itemDto, Long ownerId) {
        var owner = userRepository.findByIdOrThrow(ownerId);

        // 'itemRequest,' the rebellious teenager in my 'var' family portrait!
        ItemRequest itemRequest = null; // Come on, 'var', let's take a family photo!

        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestRepository.findByIdOrThrow(itemDto.getRequestId());
        }

        var item = itemMapper.mapToItem(itemDto, owner, itemRequest);
        var savedItem = itemRepository.save(item);

        log.info("item with id: {} added", savedItem.getId());
        log.debug("Item added: {}", savedItem);

        return itemMapper.mapToItemDto(savedItem);
    }

    @Override
    public ItemDetailsDto getItemById(Long itemId, Long userId) {
        userRepository.existsByIdOrThrow(userId);

        var item = itemRepository.findByIdOrThrow(itemId);
        var comments = commentRepository.findAllByItemId(itemId);

        ItemDetailsDto itemDto;
        if (ItemUtils.isOwner(item, userId)) {
            var now = LocalDateTime.now();
            var lastBooking = bookingRepository
                    .findItemLastBooking(itemId, BookingStatus.APPROVED, now)
                    .orElse(null);
            var nextBooking = bookingRepository
                    .findItemNextBooking(itemId, BookingStatus.APPROVED, now)
                    .orElse(null);

            itemDto = itemMapper.mapToItemDetailsDto(item, comments, lastBooking, nextBooking);
        } else {
            itemDto = itemMapper.mapToItemDetailsDto(item, comments);
        }

        log.info("Item with id: {} returned", item.getId());
        log.debug("Item returned: {}", itemDto);

        return itemDto;
    }

    @Override
    public List<ItemDetailsDto> getItemsByUserId(Long userId, Integer from, Integer size) {
        userRepository.existsByIdOrThrow(userId);

        var page = PageRequest.of(from / size, size);
        var items = itemRepository.findAllByOwnerId(userId, page);

        var now = LocalDateTime.now();
        var itemIds = ItemUtils.toIdsSet(items);
        var lastBookings = bookingRepository.findItemsLastBooking(itemIds, BookingStatus.APPROVED, now);
        var nextBookings = bookingRepository.findItemsNextBooking(itemIds, BookingStatus.APPROVED, now);

        var lastBookingByItemId = BookingUtils.toBookingByItemId(lastBookings);
        var nextBookingByItemId = BookingUtils.toBookingByItemId(nextBookings);
        var itemsDto = itemMapper.mapToItemDetailsDto(items, lastBookingByItemId, nextBookingByItemId);

        log.info("Items page with from: {} and size: {}, returned, count: {}", from, size, itemsDto.size());
        log.debug("Items page returned: {}", itemsDto);

        return itemsDto;
    }

    @Override
    public List<ItemDto> getItemsByText(String text, Integer from, Integer size) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }

        var page = PageRequest.of(from / size, size);
        var items = itemRepository.findAllByText(text, page);
        var itemsDto = itemMapper.mapToItemDto(items);

        log.info("Items page with from: {}, size: {}, by text: {} returned: , count: {}", from, size, text,
                itemsDto.size());
        log.debug("Items returned: {}", itemsDto);

        return itemsDto;
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemCreationDto itemDto, Long itemId, Long userId) {
        userRepository.existsByIdOrThrow(userId);
        var item = itemRepository.findByIdOrThrow(itemId);

        if (!isOwner(itemId, userId)) {
            throw new ItemAccessException(itemId, userId);
        }

        var updatedItem = updateItem(item, itemDto);
        var savedItem = itemRepository.save(updatedItem);

        log.info("Item with id: {} updated", savedItem.getId());
        log.debug("Item updated: {}", savedItem);

        return itemMapper.mapToItemDto(savedItem);
    }

    private Item updateItem(Item item, ItemCreationDto itemDto) {
        var name = itemDto.getName();
        if (name != null) {
            item.setName(name);
            log.trace("Item name updated: {}", item);
        }

        var description = itemDto.getDescription();
        if (description != null) {
            item.setDescription(description);
            log.trace("Item description updated: {}", item);
        }

        var available = itemDto.getAvailable();
        if (available != null) {
            item.setIsAvailable(available);
            log.trace("Item isAvailable updated: {}", item);
        }

        return item;
    }

    private boolean isOwner(Long itemId, Long userId) {
        var isOwner = itemRepository.existsByIdAndOwnerId(itemId, userId);

        log.trace("User with id: {} is {} owner of item with id: {}", userId, (isOwner ? "" : "not"), itemId);
        return isOwner;
    }
}
