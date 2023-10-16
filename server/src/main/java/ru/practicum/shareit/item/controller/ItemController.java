package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.commondto.item.request.ItemCreationDto;
import ru.practicum.shareit.commondto.item.request.comment.CommentCreationDto;
import ru.practicum.shareit.commondto.item.response.ItemDetailsDto;
import ru.practicum.shareit.commondto.item.response.ItemDto;
import ru.practicum.shareit.commondto.item.response.comment.CommentDto;
import ru.practicum.shareit.commons.constants.HttpHeaderConstants;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto addItem(
            @RequestBody ItemCreationDto itemDto,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Adding item for user with id: {}", userId);
        log.debug("Adding item for user with id: {}, {}", userId, itemDto);

        return itemService.addItem(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestBody CommentCreationDto commentDto,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Adding comment for item with id: {} from user with id: {}", itemId, userId);
        log.debug("Adding comment for item with id: {} from user with id: {}, {}", itemId, userId, commentDto);

        return commentService.addComment(commentDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDetailsDto getItemById(
            @PathVariable Long itemId,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Getting item with id: {} for user with id: {}", itemId, userId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDetailsDto> getItemsByUserId(
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Getting items for user with ID: {}, with pagination: (from: {}, size: {})", userId, from, size);
        return itemService.getItemsByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByText(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Getting items with text: {}, with pagination: (from: {}, size: {})", text, from, size);
        return itemService.getItemsByText(text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestBody ItemCreationDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Updating item with id: {} for user with id: {}", itemId, userId);
        log.info("Updating item with id: {} for user with id: {}, {}", itemId, userId, itemDto);

        return itemService.updateItem(itemDto, itemId, userId);
    }
}
