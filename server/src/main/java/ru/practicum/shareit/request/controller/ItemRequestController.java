package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.HttpHeadersConstants;
import ru.practicum.shareit.request.dto.request.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.response.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(
            @RequestBody RequestItemRequestDto requestDto,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        return itemRequestService.addItemRequest(requestDto, userId);
    }

    @GetMapping
    public Iterable<ItemRequestDto> getItemRequestsByRequesterId(
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        return itemRequestService.getItemRequestsByRequesterId(userId);
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestsById(
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId,
            @PathVariable Long itemRequestId
    ) {
        return itemRequestService.getItemRequestById(itemRequestId, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getPageWithItemRequestsByRequesterId(
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId,
            @RequestParam Integer from,
            @RequestParam Integer size
    ) {
        return itemRequestService.getItemRequestsByRequesterId(userId, from, size);
    }
}
