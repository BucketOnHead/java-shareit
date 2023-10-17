package ru.practicum.shareit.server.itemrequest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.dto.itemrequest.request.ItemRequestCreationDto;
import ru.practicum.shareit.server.dto.itemrequest.response.ItemRequestDto;
import ru.practicum.shareit.server.constants.HttpHeaderConstants;
import ru.practicum.shareit.server.itemrequest.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(
            @RequestBody ItemRequestCreationDto requestDto,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Adding item request from user with id: {}", userId);
        log.debug("Adding item request from user with id: {}, {}", userId, requestDto);

        return itemRequestService.addItemRequest(requestDto, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(
            @PathVariable Long requestId,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Getting item request with id: {} by user with id: {}", requestId, userId);
        return itemRequestService.getItemRequestById(requestId, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByRequesterId(
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        log.info("Getting item requests for requester with id: {}", userId);
        return itemRequestService.getItemRequestsByRequesterId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequests(
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId,
            @RequestParam Integer from,
            @RequestParam Integer size
    ) {
        log.info("Getting item requests page with from: {} and size: {} for user with id: {}", from, size, userId);
        return itemRequestService.getItemRequests(userId, from, size);
    }
}
