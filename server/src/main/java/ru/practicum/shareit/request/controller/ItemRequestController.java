package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.HttpHeadersConstants;
import ru.practicum.shareit.request.dto.request.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.response.ItemRequestDto;
import ru.practicum.shareit.request.logger.ItemRequestControllerLoggerHelper;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(
            @RequestBody RequestItemRequestDto requestDto,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long currentUserId
    ) {
        ItemRequestControllerLoggerHelper.addItemRequest(log, requestDto, currentUserId);
        return itemRequestService.addItemRequest(requestDto, currentUserId);
    }

    @GetMapping
    public Iterable<ItemRequestDto> getItemRequestsByRequesterId(
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        ItemRequestControllerLoggerHelper.getItemRequestDtosByRequesterId(log, userId);
        return itemRequestService.getItemRequestsByRequesterId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(
            @PathVariable Long requestId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        ItemRequestControllerLoggerHelper.getItemRequestDtoById(log, requestId, userId);
        return itemRequestService.getItemRequestById(requestId, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequestPageByRequesterId(
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long requesterUserId,
            @RequestParam Integer from,
            @RequestParam Integer size
    ) {
        ItemRequestControllerLoggerHelper.getItemRequestDtoPageByRequesterId(log, requesterUserId, from, size);
        return itemRequestService.getItemRequestsByRequesterId(requesterUserId, from, size);
    }
}
