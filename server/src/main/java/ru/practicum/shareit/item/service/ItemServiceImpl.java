package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.request.ItemCreationDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsDto;
import ru.practicum.shareit.item.dto.response.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.item.utils.ItemUtils;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

        return itemMapper.mapToSimpleItemResponseDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemCreationDto itemDto, Long itemId, Long userId) {
        userRepository.existsByIdOrThrow(userId);
        var item = itemRepository.findByIdOrThrow(itemId);
        if (!isOwner(itemId, userId)) {
            throw ItemNotFoundException.fromItemIdAndUserId(itemId, userId);
        }

        var updatedItem = updateItem(item, itemDto);
        var savedItem = itemRepository.save(updatedItem);

        log.info("Item with id: {} updated", savedItem.getId());
        log.debug("Item updated: {}", savedItem);

        return itemMapper.mapToSimpleItemResponseDto(savedItem);
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
                    .findLastBookingByTime(itemId, Status.APPROVED, now)
                    .orElse(null);
            var nextBooking = bookingRepository
                    .findNextBookingByTime(itemId, Status.APPROVED, now)
                    .orElse(null);

            itemDto = itemMapper.mapToItemDetailsResponseDto(item, comments, lastBooking, nextBooking);
        } else {
            itemDto = itemMapper.mapToItemDetailsResponseDto(item, comments);
        }

        log.info("Item with id: {} retrieved", item.getId());
        log.debug("Item retrieved: {}", itemDto);

        return itemDto;
    }

    @Override
    public List<ItemDetailsDto> getItemsByUserId(Long userId, Integer from, Integer size) {
        userRepository.existsByIdOrThrow(userId);

        var now = LocalDateTime.now();
        var page = PageRequest.of(from / size, size);
        var items = itemRepository.findAllByOwnerId(userId, page);
        var lastBookings = bookingRepository.findAllLastBookingByTime(ItemUtils.toIdsSet(items), Status.APPROVED, now);
        var nextBookings = bookingRepository.findAllNextBookingByTime(ItemUtils.toIdsSet(items), Status.APPROVED, now);

        var itemsDto = items.stream()
                .map(item -> itemMapper.mapToItemDetailsResponseDto(item,
                        lastBookings.getOrDefault(item.getId(), null).orElse(null),
                        nextBookings.getOrDefault(item.getId(), null).orElse(null)))
                .collect(Collectors.toList());

        log.info("Item with pagination retrieved: (from: {}, size: {}), count: {}", from, size, itemsDto.size());
        log.debug("Item with pagination retrieved: {}", itemsDto);

        return itemsDto;
    }

    @Override
    public List<ItemDto> getItemsByText(String text, Integer from, Integer size) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }

        var page = PageRequest.of(from / size, size);
        var items = itemRepository.findAllByText(text, page);
        var itemsDto = itemMapper.mapToSimpleItemResponseDto(items);

        log.info("Items by text with pagination retrieved: (from: {}, size: {}), count: {}", from, size,
                itemsDto.size());
        log.debug("Items by text with pagination retrieved: {}", itemsDto);

        return itemsDto;
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
