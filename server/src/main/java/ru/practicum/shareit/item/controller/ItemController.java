package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.HttpHeadersConstants;
import ru.practicum.shareit.item.dto.request.ItemCreationDto;
import ru.practicum.shareit.item.dto.request.comment.CommentCreationDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemResponseDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;
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
    public SimpleItemResponseDto addItem(
            @RequestBody ItemCreationDto itemDto,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Adding item for user with id: {}", userId);
        log.debug("Adding item for user with id: {}, {}", userId, itemDto);

        return itemService.addItem(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public SimpleCommentResponseDto addComment(
            @RequestBody CommentCreationDto commentDto,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Adding comment for item with id: {} from user with id: {}", itemId, userId);
        log.debug("Adding comment for item with id: {} from user with id: {}, {}", itemId, userId, commentDto);

        return commentService.addComment(commentDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDetailsResponseDto getItemById(
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Getting item with id: {} for user with id: {}", itemId, userId);

        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDetailsResponseDto> getItemsByUserId(
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Getting items for user with ID: {}, with pagination: (from: {}, size: {})", userId, from, size);

        return itemService.getItemsByOwnerUserId(userId, from, size);
    }

    @GetMapping("/search")
    public List<SimpleItemResponseDto> getItemsByText(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Getting items with text: {}, with pagination: (from: {}, size: {})", text, from, size);

        return itemService.searchItemsByNameOrDescriptionIgnoreCase(text, from, size);
    }

    @PatchMapping("/{itemId}")
    public SimpleItemResponseDto updateItem(
            @RequestBody ItemCreationDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Updating item with id: {} for user with id: {}", itemId, userId);
        log.info("Updating item with id: {} for user with id: {}, {}", itemId, userId, itemDto);

        return itemService.updateItem(itemDto, itemId, userId);
    }
}
