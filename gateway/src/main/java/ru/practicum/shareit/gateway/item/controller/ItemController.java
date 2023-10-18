package ru.practicum.shareit.gateway.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.client.item.ItemClient;
import ru.practicum.shareit.server.client.item.comment.CommentClient;
import ru.practicum.shareit.server.dto.item.request.ItemCreationDto;
import ru.practicum.shareit.server.dto.item.request.comment.CommentCreationDto;
import ru.practicum.shareit.server.dto.item.response.ItemDetailsDto;
import ru.practicum.shareit.server.dto.item.response.ItemDto;
import ru.practicum.shareit.server.dto.item.response.comment.CommentDto;
import ru.practicum.shareit.server.dto.validation.Groups;
import ru.practicum.shareit.server.constants.HttpHeaderConstants;
import ru.practicum.shareit.gateway.consts.DefaultParams;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private final CommentClient commentClient;

    @PostMapping
    public ItemDto addItem(
            @RequestBody @Validated(Groups.OnCreate.class) ItemCreationDto itemDto,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long ownerId
    ) {
        return itemClient.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestBody @Validated(Groups.OnUpdate.class) ItemCreationDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDetailsDto getItemById(
            @PathVariable Long itemId,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDetailsDto> getItemsByOwnerId(
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId,
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size
    ) {
        return itemClient.getItemsByOwnerId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(
            @RequestParam String text,
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size
    ) {
        return itemClient.searchItemsByNameOrDescription(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestBody @Validated(Groups.OnCreate.class) CommentCreationDto comment,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return commentClient.addComment(comment, userId, itemId);
    }
}