package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.request.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.response.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validation.group.CreationGroup;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(
            @Validated(CreationGroup.class) @RequestBody RequestItemRequestDto requestDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.addItemRequest(requestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByRequesterId(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequestsByRequesterId(userId);
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestsById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemRequestId) {
        return itemRequestService.getItemRequestById(itemRequestId, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getPageWithItemRequestsByRequesterId(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam Integer from,
            @RequestParam Integer size) {
        return itemRequestService.getItemRequestsByRequesterId(userId, from, size);
    }
}
