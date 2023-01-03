package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.request.RequestItemDto;
import ru.practicum.shareit.item.dto.request.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.response.DetailedItemDto;
import ru.practicum.shareit.item.dto.response.ItemDto;
import ru.practicum.shareit.item.dto.response.comment.CommentDto;
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
    public ItemDto addItem(
            @RequestBody RequestItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestBody RequestItemDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public DetailedItemDto getItemById(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<DetailedItemDto> getItemsByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getItemsByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return itemService.searchItemsByNameOrDescription(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestBody RequestCommentDto comment,
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return commentService.addComment(comment, userId, itemId);
    }
}
