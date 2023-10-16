package ru.practicum.shareit.itemrequest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.commondto.itemrequest.request.ItemRequestCreationDto;
import ru.practicum.shareit.commondto.itemrequest.response.ItemRequestDto;
import ru.practicum.shareit.commondto.validation.Groups;
import ru.practicum.shareit.commons.constants.HttpHeaderConstants;
import ru.practicum.shareit.consts.DefaultParams;
import ru.practicum.shareit.itemrequest.client.ItemRequestClient;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ItemRequestDto addItemRequest(
            @RequestBody @Validated(Groups.OnCreate.class) ItemRequestCreationDto requestDto,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return itemRequestClient.addItemRequest(requestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByRequesterId(
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return itemRequestClient.getItemRequestsByRequesterId(userId);
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestsById(
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId,
            @PathVariable Long itemRequestId
    ) {
        return itemRequestClient.getItemRequestById(itemRequestId, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getPageWithItemRequestsByRequesterId(
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId,
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size
    ) {
        return itemRequestClient.getItemRequestsByRequesterId(userId, from, size);
    }
}
