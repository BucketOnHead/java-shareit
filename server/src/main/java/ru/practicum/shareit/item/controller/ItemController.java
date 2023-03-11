package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.HttpHeadersConstants;
import ru.practicum.shareit.item.dto.request.ItemRequestDto;
import ru.practicum.shareit.item.dto.request.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemResponseDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.comment.CommentService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public SimpleItemResponseDto addItem(
            @RequestBody ItemRequestDto itemDto,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long ownerUserId
    ) {
        return itemService.addItem(itemDto, ownerUserId);
    }

    @PatchMapping("/{itemId}")
    public SimpleItemResponseDto updateItem(
            @RequestBody ItemRequestDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long currentUserId
    ) {
        return itemService.updateItem(itemDto, itemId, currentUserId);
    }

    @GetMapping("/{itemId}")
    public ItemDetailsResponseDto getItemById(
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long currentUserId
    ) {
        return itemService.getItemById(itemId, currentUserId);
    }

    @GetMapping
    public Iterable<ItemDetailsResponseDto> getItemsByOwnerUserId(
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long ownerUserId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return itemService.getItemsByOwnerUserId(ownerUserId, from, size);
    }

    @GetMapping("/search")
    public Iterable<SimpleItemResponseDto> searchItemsByNameOrDescriptionIgnoreCase(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return itemService.searchItemsByNameOrDescriptionIgnoreCase(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public SimpleCommentResponseDto addComment(
            @RequestBody CommentRequestDto comment,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long authorUserId
    ) {
        return commentService.addComment(comment, authorUserId, itemId);
    }
}
