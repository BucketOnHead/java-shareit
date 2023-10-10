package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.HttpHeadersConstants;
import ru.practicum.shareit.item.dto.request.ItemRequestDto;
import ru.practicum.shareit.item.dto.request.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemResponseDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;
import ru.practicum.shareit.item.logger.ItemControllerLoggerHelper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.comment.CommentService;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public SimpleItemResponseDto addItem(
            @RequestBody ItemRequestDto itemDto,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        ItemControllerLoggerHelper.addItem(log, itemDto, userId);
        return itemService.addItem(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public SimpleCommentResponseDto addComment(
            @RequestBody CommentRequestDto commentDto,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        ItemControllerLoggerHelper.addComment(log, commentDto, itemId, userId);
        return commentService.addComment(commentDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDetailsResponseDto getItemById(
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        ItemControllerLoggerHelper.getItemById(log, itemId, userId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public Iterable<ItemDetailsResponseDto> getItemsByUserId(
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        ItemControllerLoggerHelper.getItemsByOwnerUserId(log, userId, from, size);
        return itemService.getItemsByOwnerUserId(userId, from, size);
    }

    @GetMapping("/search")
    public Iterable<SimpleItemResponseDto> getItemsByText(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        ItemControllerLoggerHelper.searchItemsByTextIgnoreCase(log, text, from, size);
        return itemService.searchItemsByNameOrDescriptionIgnoreCase(text, from, size);
    }

    @PatchMapping("/{itemId}")
    public SimpleItemResponseDto updateItem(
            @RequestBody ItemRequestDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        ItemControllerLoggerHelper.updateItem(log, itemDto, itemId, userId);
        return itemService.updateItem(itemDto, itemId, userId);
    }
}
