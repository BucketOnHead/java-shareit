package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.in.RequestItemDto;
import ru.practicum.shareit.item.dto.in.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.ItemDto;
import ru.practicum.shareit.item.dto.out.comment.CommentDto;
import ru.practicum.shareit.item.model.Item.CreationInfo;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.comment.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto addItem(
            @Validated(CreationInfo.class) @RequestBody RequestItemDto itemDto,
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
        return itemService.getItemByItemId(itemId, userId);
    }

    @GetMapping
    public List<DetailedItemDto> getItemsByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.getItemsByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(
            @RequestParam String text) {
        return itemService.searchItemsByNameOrDescription(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestBody @Valid RequestCommentDto comment,
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return commentService.addComment(comment, userId, itemId);
    }
}
