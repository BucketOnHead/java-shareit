package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.item.dto.in.ItemCreationRequestDto;
import ru.practicum.shareit.item.dto.in.ItemUpdateRequestDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.mapper.comment.CommentDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.service.ItemService.checkItemExistsById;
import static ru.practicum.shareit.item.service.ItemService.checkOwnerOfItemByItemIdAndUserId;
import static ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemDtoMapper itemDtoMapper;
    private final BookingDtoMapper bookingDtoMapper;
    private final CommentDtoMapper commentDtoMapper;

    @Override
    public ItemDto addItem(ItemCreationRequestDto itemDto, Long ownerId) {
        checkUserExistsById(userRepository, ownerId);
        Item item = itemDtoMapper.toItem(itemDto, ownerId);
        Item addedItem = itemRepository.save(item);
        log.debug("Item ID_{} added.", addedItem.getId());
        return itemDtoMapper.toItemDto(addedItem);
    }

    @Override
    public ItemDto updateItem(ItemUpdateRequestDto itemDto, Long itemId, Long userId) {
        checkItemExistsById(itemRepository, itemId);
        checkUserExistsById(userRepository, userId);
        checkOwnerOfItemByItemIdAndUserId(itemRepository, itemId, userId);
        Item item = itemDtoMapper.toItem(itemDto, itemId, userId);
        Item updatedItem = itemRepository.save(item);
        log.debug("Item ID_{} updated.", itemId);
        return itemDtoMapper.toItemDto(updatedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public DetailedItemDto getItemByItemId(Long itemId, Long userId) {
        checkItemExistsById(itemRepository, itemId);
        Item item = itemRepository.getReferenceById(itemId);
        log.debug("Item ID_{} returned.", item.getId());
        if (isOwner(item, userId)) {
            return itemDtoMapper.toDetailedItemDto(item, commentDtoMapper, bookingDtoMapper);
        }
        return itemDtoMapper.toDetailedItemDto(item, commentDtoMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetailedItemDto> getItemsByOwnerId(Long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        log.debug("All items have been returned, {} in total.", items.size());
        return itemDtoMapper.toDetailedItemDto(items, commentDtoMapper, bookingDtoMapper);
    }

    @Override
    @Transactional(readOnly = true)
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

        return itemDtoMapper.toItemDto(foundItems);
    }

    private static boolean isOwner(Item item, Long userId) {
        Long ownerId = item.getOwner().getId();
        return ownerId.equals(userId);
    }
}
