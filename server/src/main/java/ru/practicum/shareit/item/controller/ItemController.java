package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.request.ItemRequestDto;
import ru.practicum.shareit.item.dto.request.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public SimpleItemDto addItem(
            @RequestBody ItemRequestDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerUserId
    ) {
        return itemService.addItem(itemDto, ownerUserId);
    }

    @PatchMapping("/{itemId}")
    public SimpleItemDto updateItem(
            @RequestBody ItemRequestDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long currentUserId
    ) {
        return itemService.updateItem(itemDto, itemId, currentUserId);
    }

    @GetMapping("/{itemId}")
    public ItemDetailsResponseDto getItemById(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long currentUserId
    ) {
        return itemService.getItemById(itemId, currentUserId);
    }

    @GetMapping
    public List<ItemDetailsResponseDto> getItemsByOwnerUserId(
            @RequestHeader("X-Sharer-User-Id") Long ownerUserId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return itemService.getItemsByOwnerUserId(ownerUserId, from, size);
    }

    @GetMapping("/search")
    public List<SimpleItemDto> searchItemsByNameOrDescriptionIgnoreCase(
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
            @RequestHeader("X-Sharer-User-Id") Long authorUserId
    ) {
        return commentService.addComment(comment, authorUserId, itemId);
    }
}
