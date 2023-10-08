package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.request.ItemRequestDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemResponseDto;
import ru.practicum.shareit.item.logger.ItemServiceLoggerHelper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public SimpleItemResponseDto addItem(ItemRequestDto itemDto, Long ownerUserId) {
        userRepository.validateUserExistsById(ownerUserId);
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
    public SimpleItemResponseDto updateItem(ItemRequestDto itemDto, Long itemId, Long currentUserId) {
        itemRepository.validateItemExistsById(itemId);
        userRepository.validateUserExistsById(currentUserId);
        itemRepository.validateUserIdIsItemOwner(itemId, currentUserId);

        Item updatedItem = getUpdatedItem(itemDto, itemId, currentUserId);
        Item savedItem = itemRepository.save(updatedItem);

        ItemServiceLoggerHelper.itemUpdated(log, savedItem);
        return itemMapper.mapToSimpleItemResponseDto(savedItem);
    }

    @Override
    public ItemDetailsResponseDto getItemById(Long itemId, Long userId) {
        userRepository.validateUserExistsById(userId);
        itemRepository.validateItemExistsById(itemId);

        Item item = itemRepository.getReferenceById(itemId);

        ItemDetailsResponseDto itemDto;
        var comments = commentRepository.findAllByItemId(item.getId());
        if (item.isOwner(userId)) {
            var now = LocalDateTime.now();

            var lastSort = Sort.by(Sort.Direction.DESC, "startTime");
            Booking lastBooking = bookingRepository
                    .findTopByItemIdAndStartTimeLessThanAndStatus(itemId, now, Status.APPROVED, lastSort)
                    .orElse(null);

            var nextSort = Sort.by(Sort.Direction.ASC, "startTime");
            Booking nextBooking = bookingRepository
                    .findTopByItemIdAndStartTimeGreaterThanEqualAndStatus(itemId, now, Status.APPROVED, nextSort)
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
        userRepository.validateUserExistsById(ownerUserId);

        var page = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Item> items = itemRepository.findAllByOwnerId(ownerUserId, page);
        List<ItemDetailsResponseDto> ownerItemDtos = itemMapper.mapToItemDetailsResponseDto(items);

        ItemServiceLoggerHelper.itemDtosReturned(log, ownerItemDtos);
        return ownerItemDtos;
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

    private Item getItem(ItemRequestDto itemDto, Long ownerUserId) {
        Item item = itemMapper.mapToItem(itemDto);

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
}
