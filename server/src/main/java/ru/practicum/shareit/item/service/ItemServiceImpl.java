package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.request.ItemRequestDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemResponseDto;
import ru.practicum.shareit.item.logger.ItemServiceLoggerHelper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
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

    @Override
    @Transactional
    public SimpleItemResponseDto addItem(ItemRequestDto itemDto, Long ownerUserId) {
        userRepository.validateUserExistsById(ownerUserId);
        if (itemDto.getRequestId() != null) {
            ItemRequestServiceImpl.validateItemRequestExistsById(itemRequestRepository, itemDto.getRequestId());
        }

        Item item = getItem(itemDto, ownerUserId);
        Item savedItem = itemRepository.save(item);

        ItemServiceLoggerHelper.itemSaved(log, savedItem);
        return ItemDtoMapper.toSimpleItemResponseDto(savedItem);
    }

    @Override
    @Transactional
    public SimpleItemResponseDto updateItem(ItemRequestDto itemDto, Long itemId, Long currentUserId) {
        itemRepository.validateItemExistsById(itemId);
        userRepository.validateUserExistsById(currentUserId);
        itemRepository.validateUserIdIsItemOwner(itemId, currentUserId);

        Item updatedItem = getUpdatedItem(itemDto, itemId, currentUserId);
        Item savedItem = itemRepository.save(updatedItem);

        ItemServiceLoggerHelper.itemUpdated(log, savedItem);
        return ItemDtoMapper.toSimpleItemResponseDto(savedItem);
    }

    @Override
    public ItemDetailsResponseDto getItemById(Long itemId, Long currentUserId) {
        userRepository.validateUserExistsById(currentUserId);
        itemRepository.validateItemExistsById(itemId);

        Item item = itemRepository.getReferenceById(itemId);

        ItemDetailsResponseDto itemDto;

        if (item.getOwner().getId().equals(currentUserId)) {
            itemDto = getDetailedItemDto(item);
        } else {
            itemDto = getDetailedItemDtoWithoutBookings(item);
        }

        ItemServiceLoggerHelper.itemDtoReturned(log, itemDto);
        return itemDto;
    }

    @Override
    public List<ItemDetailsResponseDto> getItemsByOwnerUserId(Long ownerUserId, Integer from, Integer size) {
        userRepository.validateUserExistsById(ownerUserId);

        Page<Item> items = itemRepository.findAllByOwnerId(ownerUserId, PageRequest.of(from, size));
        List<ItemDetailsResponseDto> ownerItemDtos = getItemDetailsResponseDtos(items.toList());

        ItemServiceLoggerHelper.itemDtosReturned(log, ownerItemDtos);
        return ownerItemDtos;
    }

    @Override
    public List<SimpleItemResponseDto> searchItemsByNameOrDescriptionIgnoreCase(
            String text, Integer from, Integer size
    ) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }

        List<Item> foundItems = findItemsByText(text, PageRequest.of(from, size));
        List<SimpleItemResponseDto> itemDtos = ItemDtoMapper.toSimpleItemResponseDto(foundItems);

        ItemServiceLoggerHelper.itemDtosByTextReturned(log, text, itemDtos);
        return itemDtos;
    }

    private Item getItem(ItemRequestDto itemDto, Long ownerUserId) {
        Item item = ItemDtoMapper.toItem(itemDto);

        item.setOwner(userRepository.getReferenceById(ownerUserId));

        if (itemDto.getRequestId() != null) {
            item.setItemRequest(itemRequestRepository.getReferenceById(itemDto.getRequestId()));
        }

        return item;
    }

    private Item getUpdatedItem(ItemRequestDto itemDto, Long itemId, Long ownerUserId) {
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

    private ItemDetailsResponseDto getDetailedItemDtoWithoutBookings(Item item) {
        return ItemDtoMapper.toItemDetailsResponseDto(
                item,
                commentRepository.findAllByItemId(item.getId()),
                null,
                null);
    }

    private List<ItemDetailsResponseDto> getItemDetailsResponseDtos(List<Item> items) {
        return items.stream()
                .map(this::getDetailedItemDto)
                .collect(Collectors.toList());
    }

    private ItemDetailsResponseDto getDetailedItemDto(Item item) {
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());
        LocalDateTime time = LocalDateTime.now();

        Booking lastBooking = bookingRepository
                .findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc(item.getId(), time)
                .orElse(null);

        Booking nextBooking = bookingRepository
                .findFirstByItemIdAndStartTimeIsAfter(item.getId(), time)
                .orElse(null);

        return ItemDtoMapper.toItemDetailsResponseDto(item, comments, lastBooking, nextBooking);
    }
}
