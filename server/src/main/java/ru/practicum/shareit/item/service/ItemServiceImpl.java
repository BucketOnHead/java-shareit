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
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final ItemRequestRepository itemRequestRepository;

    public static void checkOwnerOfItemByItemIdAndUserId(ItemRepository itemRepository,
                                                         Long itemId, Long userId) {
        Long ownerId = itemRepository.getReferenceById(itemId).getOwner().getId();

        if (!ownerId.equals(userId)) {
            throw ItemNotFoundException.fromItemIdAndUserId(itemId, userId);
        }
    }

    @Override
    @Transactional
    public SimpleItemResponseDto addItem(ItemRequestDto itemDto, Long ownerUserId) {
        checkUserExistsById(userRepository, ownerUserId);
        checkItemRequestExistsById(itemDto.getRequestId());

        Item item = getItem(itemDto, ownerUserId);
        Item savedItem = itemRepository.save(item);
        log.debug("ITEM[ID_{}] added.", savedItem.getId());

        return ItemDtoMapper.toSimpleItemResponseDto(savedItem, itemDto.getRequestId());
    }

    @Override
    @Transactional
    public SimpleItemResponseDto updateItem(ItemRequestDto itemDto, Long itemId, Long currentUserId) {
        itemRepository.validateItemExistsById(itemId);
        checkUserExistsById(userRepository, currentUserId);
        checkOwnerOfItemByItemIdAndUserId(itemRepository, itemId, currentUserId);

        Item updatedItem = getUpdatedItem(itemDto, itemId, currentUserId);
        Item savedItem = itemRepository.save(updatedItem);
        log.debug("ITEM[ID_{}] updated.", savedItem.getId());

        if (updatedItem.getItemRequest() == null) {
            return ItemDtoMapper.toSimpleItemResponseDto(updatedItem, null);
        } else {
            return ItemDtoMapper.toSimpleItemResponseDto(savedItem, updatedItem.getItemRequest().getId());
        }
    }

    @Override
    public ItemDetailsResponseDto getItemById(Long itemId, Long currentUserId) {
        itemRepository.validateItemExistsById(itemId);

        Item item = itemRepository.getReferenceById(itemId);

        ItemDetailsResponseDto itemDto;
        if (isOwner(item, currentUserId)) {
            itemDto = getDetailedItemDto(item);
        } else {
            itemDto = getDetailedItemDtoWithoutBookings(item);
        }
        log.debug("ITEM[ID_{}]<DTO> returned.", itemDto.getId());
        return itemDto;
    }

    @Override
    public List<ItemDetailsResponseDto> getItemsByOwnerUserId(Long ownerUserId, Integer from, Integer size) {
        checkUserExistsById(userRepository, ownerUserId);

        List<ItemDetailsResponseDto> itemsByOwnerId
                = getItemsByOwnerIdWithPagination(ownerUserId, from, size);

        log.debug("All ITEM<DTO> returned, {} in total.", itemsByOwnerId.size());
        return itemsByOwnerId;
    }

    @Override
    public List<SimpleItemResponseDto> searchItemsByNameOrDescriptionIgnoreCase(String text,
                                                                                Integer from, Integer size) {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        }

        List<SimpleItemResponseDto> foundItemsDto
                = getItemsByNameOrDescriptionWithPagination(text, from, size);

        log.debug("All ITEM<DTO> containing '{}' returned, {} in total.", text, foundItemsDto.size());
        return foundItemsDto;
    }

    private List<SimpleItemResponseDto> getItemsByNameOrDescriptionWithPagination(String text,
                                                                                  Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size);
        List<Item> foundItems = findItemsByText(text, page);
        return ItemDtoMapper.toSimpleItemResponseDto(foundItems);
    }

    private static boolean itemContainsTextInNameOrDescription(Item item, String text) {
        boolean containsTextInName = StringUtils.containsIgnoreCase(item.getName(), text);
        boolean containsTextInDescription = StringUtils.containsIgnoreCase(item.getDescription(), text);
        return containsTextInName || containsTextInDescription;
    }

    private static boolean isOwner(Item item, Long userId) {
        Long ownerId = item.getOwner().getId();
        return ownerId.equals(userId);
    }

    private void checkItemRequestExistsById(Long requestId) {
        if (requestId != null) {
            ItemRequestServiceImpl.checkItemRequestExistsById(itemRequestRepository, requestId);
        }
    }

    private Item getItem(ItemRequestDto itemDto, Long ownerUserId) {
        Item item = ItemDtoMapper.toItem(itemDto);

        item.setOwner(userRepository.getReferenceById(ownerUserId));

        if (itemDto.getRequestId() != null) {
            item.setItemRequest(itemRequestRepository.getReferenceById(itemDto.getRequestId()));
        }

        return item;
    }

    private Item getUpdatedItem(ItemRequestDto itemDto, Long itemId, Long ownerId) {
        Item item = itemRepository.getReferenceById(itemId);

        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setIsAvailable);

        User owner = userRepository.getReferenceById(ownerId);
        item.setOwner(owner);

        return item;
    }

    private List<Item> findItemsByText(String text, Pageable page) {
        return itemRepository.findAllByIsAvailableIsTrue(page).toList()
                .stream()
                .filter(item -> itemContainsTextInNameOrDescription(item, text))
                .collect(Collectors.toList());
    }

    private ItemDetailsResponseDto getDetailedItemDtoWithoutBookings(Item item) {
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());
        return ItemDtoMapper.toDetailedItemDtoWithoutBookings(item, comments);
    }

    private List<ItemDetailsResponseDto> getItemsByOwnerIdWithPagination(Long ownerId,
                                                                         Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size);
        Page<Item> items = itemRepository.findAllByOwnerId(ownerId, page);
        return toDetailedItemDto(items.toList());
    }

    private List<ItemDetailsResponseDto> toDetailedItemDto(List<Item> items) {
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

        if (lastBooking != null && nextBooking != null) {
            return ItemDtoMapper.toDetailedItemDto(item, comments, lastBooking, nextBooking);
        } else if (lastBooking == null && nextBooking == null) {
            return ItemDtoMapper.toDetailedItemDtoWithoutBookings(item, comments);
        } else if (lastBooking == null) {
            return ItemDtoMapper.toDetailedItemDtoWithoutLastBooking(item, comments, nextBooking);
        } else {
            return ItemDtoMapper.toDetailedItemDtoWithoutNextBooking(item, comments, lastBooking);
        }
    }
}
