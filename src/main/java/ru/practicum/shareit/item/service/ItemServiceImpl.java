package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemDtoMapper itemDtoMapper;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        checkUserExistsById(ownerId);
        Item item = itemDtoMapper.toItem(itemDto, ownerId);
        Item addedItem = itemRepository.addItem(item);
        ItemDto addedItemDto = itemDtoMapper.toItemDto(addedItem);
        log.debug("Item with ID_{} added.", addedItem.getId());
        return addedItemDto;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        checkItemExistsById(itemId);
        checkUserExistsById(ownerId);
        Item item = itemRepository.getItemById(itemId);
        checkOwnerItem(item, ownerId);
        fillItemFromItemDto(item, itemDto);
        Item updatedItem = itemRepository.updateItem(item);
        ItemDto updatedItemDto = itemDtoMapper.toItemDto(updatedItem);
        log.debug("Item with ID_{} updated.", itemId);
        return updatedItemDto;
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        checkItemExistsById(itemId);
        Item item = itemRepository.getItemById(itemId);
        ItemDto itemDto = itemDtoMapper.toItemDto(item);
        log.debug("Item with ID_{} returned.", itemId);
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemsByOwnerId(Long ownerId) {
        List<ItemDto> items = itemRepository.getAllItems()
                .stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .map(itemDtoMapper::toItemDto)
                .collect(Collectors.toList());
        log.debug("Returned a list of all items.");
        return items;
    }

    @Override
    public List<ItemDto> searchItemsByNameOrDescription(String text, Long ownerId) {
        if (StringUtils.isEmpty(text)) {
            return new ArrayList<>();
        }

        List<ItemDto> foundItems = itemRepository.getAllItems()
                .stream()
                .filter(Item::getIsAvailable)
                .filter(itemDto -> StringUtils.containsIgnoreCase(itemDto.getName(), text)
                        || StringUtils.containsIgnoreCase(itemDto.getDescription(), text))
                .map(itemDtoMapper::toItemDto)
                .collect(Collectors.toList());
        log.debug("{} things were found for the query '{}'.", foundItems.size(), text);
        return foundItems;
    }

    private static void fillItemFromItemDto(Item item, ItemDto itemDto) {
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setIsAvailable(itemDto.getAvailable());
        }
    }

    private void checkItemExistsById(Long itemId) {
        log.debug("Search item with ID_{}.", itemId);
        if (!itemRepository.containsById(itemId)) {
            log.trace("Item with ID_{} not found ❌.", itemId);
            throw new ItemNotFoundException(itemId);
        }
        log.trace("Item with ID_{} has been found ✔.", itemId);
    }

    private void checkUserExistsById(Long userId) {
        log.debug("Search user with ID_{}.", userId);
        if (!userRepository.containsById(userId)) {
            log.trace("User with ID_{} not found ❌.", userId);
            throw new UserNotFoundException(userId);
        }
        log.trace("User with ID_{} has been found ✔.", userId);
    }

    private void checkOwnerItem(Item item, Long ownerId) {
        log.debug("Checking owner with ID_{} of the item with ID_{}.", ownerId, item.getId());
        if (!item.getOwner().getId().equals(ownerId)) {
            log.trace("User with ID_{} is not the owner of the item with ID_{} ❌.", ownerId, item.getId());
            throw new IncorrectParameterException(
                    String.format("User with ID_%d is not the owner of the item with ID_%d",
                            ownerId, item.getId()));
        }
        log.trace("User with ID_{} is the owner of the item with ID_{} ✔.", ownerId, item.getId());
    }
}
