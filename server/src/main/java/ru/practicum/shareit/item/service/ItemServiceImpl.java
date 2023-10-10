package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.request.ItemCreationDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemResponseDto;
import ru.practicum.shareit.item.logger.ItemServiceLoggerHelper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.item.utils.ItemUtils;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    public SimpleItemResponseDto addItem(ItemCreationDto itemDto, Long ownerUserId) {
        userRepository.existsByIdOrThrow(ownerUserId);
        if (itemDto.getRequestId() != null) {
            itemRepository.validateItemExistsById(itemDto.getRequestId());
        }

        Item item = getItem(itemDto, ownerUserId);
        Item savedItem = itemRepository.save(item);

        ItemServiceLoggerHelper.itemSaved(log, savedItem);
        return itemMapper.mapToSimpleItemResponseDto(savedItem);
    }

    @Override
    @Transactional
    public SimpleItemResponseDto updateItem(ItemCreationDto itemDto, Long itemId, Long currentUserId) {
        itemRepository.validateItemExistsById(itemId);
        userRepository.existsByIdOrThrow(currentUserId);
        itemRepository.validateUserIdIsItemOwner(itemId, currentUserId);

        Item updatedItem = getUpdatedItem(itemDto, itemId, currentUserId);
        Item savedItem = itemRepository.save(updatedItem);

        ItemServiceLoggerHelper.itemUpdated(log, savedItem);
        return itemMapper.mapToSimpleItemResponseDto(savedItem);
    }

    @Override
    public ItemDetailsResponseDto getItemById(Long itemId, Long userId) {
        userRepository.existsByIdOrThrow(userId);
        itemRepository.validateItemExistsById(itemId);

        Item item = itemRepository.getReferenceById(itemId);

        ItemDetailsResponseDto itemDto;
        var comments = commentRepository.findAllByItemId(item.getId());
        if (item.isOwner(userId)) {
            var now = LocalDateTime.now();

            Booking lastBooking = bookingRepository
                    .findLastBookingByTime(itemId, Status.APPROVED, now)
                    .orElse(null);

            Booking nextBooking = bookingRepository
                    .findNextBookingByTime(itemId, Status.APPROVED, now)
                    .orElse(null);

            itemDto = itemMapper.mapToItemDetailsResponseDto(item, comments, lastBooking, nextBooking);
        } else {
            itemDto = itemMapper.mapToItemDetailsResponseDto(item, comments);
        }

        ItemServiceLoggerHelper.itemDtoReturned(log, itemDto);
        return itemDto;
    }

    @Override
    public List<ItemDetailsResponseDto> getItemsByOwnerUserId(Long ownerUserId, Integer from, Integer size) {
        userRepository.existsByIdOrThrow(ownerUserId);

        var now = LocalDateTime.now();
        var page = PageRequest.of(from / size, size);
        var items = itemRepository.findAllByOwnerId(ownerUserId, page);
        var lastBookings = bookingRepository.findAllLastBookingByTime(ItemUtils.toIdsSet(items), Status.APPROVED, now);
        var nextBookings = bookingRepository.findAllNextBookingByTime(ItemUtils.toIdsSet(items), Status.APPROVED, now);

        var itemsDto = items.stream()
                .map(i -> itemMapper.mapToItemDetailsResponseDto(
                        i,
                        lastBookings.getOrDefault(i.getId(), null).orElse(null),
                        nextBookings.getOrDefault(i.getId(), null).orElse(null)))
                .collect(Collectors.toList());

        ItemServiceLoggerHelper.itemDtosReturned(log, itemsDto);
        return itemsDto;
    }

    @Override
    public List<SimpleItemResponseDto> searchItemsByNameOrDescriptionIgnoreCase(String text,
                                                                                Integer from, Integer size) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }

        List<Item> foundItems = findItemsByText(text, PageRequest.of(from, size));

        var itemsDto = itemMapper.mapToSimpleItemResponseDto(foundItems);
        ItemServiceLoggerHelper.itemDtosByTextReturned(log, text, itemsDto);
        return itemsDto;
    }

    private Item getItem(ItemCreationDto itemDto, Long ownerUserId) {
        Item item = itemMapper.mapToItem(itemDto);

        item.setOwner(userRepository.getReferenceById(ownerUserId));

        if (itemDto.getRequestId() != null) {
            item.setItemRequest(itemRequestRepository.getReferenceById(itemDto.getRequestId()));
        }

        return item;
    }

    private Item getUpdatedItem(ItemCreationDto itemDto, Long itemId, Long ownerUserId) {
        Item item = itemRepository.getReferenceById(itemId);

        item.setOwner(userRepository.getReferenceById(ownerUserId));

        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setIsAvailable);

        return item;
    }

    private List<Item> findItemsByText(String text, Pageable page) {
        return itemRepository.findAllByText(text, page).toList();
    }
}
