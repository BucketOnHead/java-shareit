package ru.practicum.shareit.gateway.itemrequest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.consts.DefaultParams;
import ru.practicum.shareit.gateway.itemrequest.client.ItemRequestClient;
import ru.practicum.shareit.server.dto.itemrequest.request.ItemRequestCreationDto;
import ru.practicum.shareit.server.dto.itemrequest.response.ItemRequestDto;
import ru.practicum.shareit.server.dto.validation.Groups;
import ru.practicum.shareit.server.constants.HttpHeaderConstants;

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
