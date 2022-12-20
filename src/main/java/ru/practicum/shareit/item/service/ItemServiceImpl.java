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
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.item.dto.in.RequestItemDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.request.model.ItemRequest;
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
        checkItemRequestExistsById(itemDto.getRequestId());

        Item item = getItem(itemDto, ownerId);
        Item savedItem = itemRepository.save(item);
        log.debug("ITEM[ID_{}] added.", savedItem.getId());

        if (itemDto.getRequestId() == null) {
            return ItemDtoMapper.toItemDtoWithoutItemRequestId(savedItem);
        } else {
            return ItemDtoMapper.toItemDto(savedItem, itemDto.getRequestId());
        }
    }

    @Override
    @Transactional
    public ItemDto updateItem(RequestItemDto itemDto, Long itemId, Long userId) {
        checkItemExistsById(itemRepository, itemId);
        checkUserExistsById(userRepository, userId);
        checkOwnerOfItemByItemIdAndUserId(itemRepository, itemId, userId);

        Item updatedItem = getUpdatedItem(itemDto, itemId, userId);
        Item savedItem = itemRepository.save(updatedItem);
        log.debug("ITEM[ID_{}] updated.", savedItem.getId());

        if (updatedItem.getItemRequest() == null) {
            return ItemDtoMapper.toItemDtoWithoutItemRequestId(updatedItem);
        } else {
            return ItemDtoMapper.toItemDto(savedItem, updatedItem.getItemRequest().getId());
        }
    }

    @Override
    public DetailedItemDto getItemById(Long itemId, Long userId) {
        checkItemExistsById(itemRepository, itemId);

        Item item = itemRepository.getReferenceById(itemId);

        DetailedItemDto itemDto;
        if (isOwner(item, userId)) {
            itemDto = getDetailedItemDto(item);
        } else {
            itemDto = getDetailedItemDtoWithoutBookings(item);
        }
        log.debug("ITEM[ID_{}]<DTO> returned.", itemDto.getId());
        return itemDto;
    }

    @Override
    public List<DetailedItemDto> getItemsByOwnerId(Long ownerId, Integer from, Integer size) {
        checkUserExistsById(userRepository, ownerId);

        List<DetailedItemDto> itemsByOwnerId;
        boolean withPagination = checkPaginationParameters(from, size);
        if (withPagination) {
            itemsByOwnerId = getItemsByOwnerIdWithPagination(ownerId, from, size);
        } else {
            itemsByOwnerId = getItemsByOwnerIdWithoutPagination(ownerId);
        }
        log.debug("All ITEM<DTO> returned, {} in total.", itemsByOwnerId.size());
        return itemsByOwnerId;
    }

    @Override
    public List<ItemDto> searchItemsByNameOrDescription(String text,
                                                        Integer from, Integer size) {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        }

        List<ItemDto> foundItemsDto;
        boolean withPagination = checkPaginationParameters(from, size);
        if (withPagination) {
            foundItemsDto = getItemsByNameOrDescriptionWithPagination(text, from, size);
        } else {
            foundItemsDto = getItemsByNameOrDescriptionWithoutPagination(text);
        }
        log.debug("All ITEM<DTO> containing '{}' returned, {} in total.", text, foundItemsDto.size());
        return foundItemsDto;
    }

    private List<ItemDto> getItemsByNameOrDescriptionWithPagination(String text,
                                                                    Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size);
        List<Item> foundItems = findItemsByText(text, page);
        return ItemDtoMapper.toItemDto(foundItems);
    }

    private List<ItemDto> getItemsByNameOrDescriptionWithoutPagination(String text) {
        List<Item> foundItems = findItemsByText(text);
        return ItemDtoMapper.toItemDto(foundItems);
    }

    /**
     * The method checks the parameters according
     * to the all-or-nothing principle, and also returns
     * a boolean value indicating the use of pagination.
     *
     * @param from Index of the starting element,
     * @param size Number of items to display.
     * @return Boolean value indicating the use of pagination.
     */
    private static boolean checkPaginationParameters(Integer from, Integer size) {
        boolean withPagination = (from != null) && (size != null);
        boolean withoutPagination = (from == null) && (size == null);

        if (!withPagination && !withoutPagination) {
            throw new IncorrectDataException(String.format("" +
                    "Pagination parameters must be specified in full or not specified at all, " +
                    "but it was: from = '%d' and size = '%d'", from, size));
        }

        return withPagination;
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

    private Item getItem(RequestItemDto itemDto, Long ownerId) {
        User owner = userRepository.getReferenceById(ownerId);
        if (itemDto.getRequestId() == null) {
            return ItemDtoMapper.toItemWithoutItemRequest(itemDto, owner);
        } else {
            ItemRequest itemRequest = itemRequestRepository.getReferenceById(itemDto.getRequestId());
            return ItemDtoMapper.toItem(itemDto, owner, itemRequest);
        }
    }

    private Item getUpdatedItem(RequestItemDto itemDto, Long itemId, Long ownerId) {
        Item item = itemRepository.getReferenceById(itemId);

        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setIsAvailable);

        User owner = userRepository.getReferenceById(ownerId);
        item.setOwner(owner);

        return item;
    }

    private List<Item> findItemsByText(String text) {
        return itemRepository.findAllByIsAvailableIsTrue()
                .stream()
                .filter(item -> itemContainsTextInNameOrDescription(item, text))
                .collect(Collectors.toList());
    }

    private List<Item> findItemsByText(String text, Pageable page) {
        return itemRepository.findAllByIsAvailableIsTrue(page).toList()
                .stream()
                .filter(item -> itemContainsTextInNameOrDescription(item, text))
                .collect(Collectors.toList());
    }

    private DetailedItemDto getDetailedItemDtoWithoutBookings(Item item) {
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());
        return ItemDtoMapper.toDetailedItemDtoWithoutBookings(item, comments);
    }

    private List<DetailedItemDto> getItemsByOwnerIdWithPagination(Long ownerId,
                                                                  Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size);
        Page<Item> items = itemRepository.findAllByOwnerId(ownerId, page);
        return toDetailedItemDto(items.toList());
    }

    private List<DetailedItemDto> getItemsByOwnerIdWithoutPagination(Long ownerId) {
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        return toDetailedItemDto(items);
    }

    private List<DetailedItemDto> toDetailedItemDto(List<Item> items) {
        return items.stream()
                .map(this::getDetailedItemDto)
                .collect(Collectors.toList());
    }

    private DetailedItemDto getDetailedItemDto(Item item) {
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
