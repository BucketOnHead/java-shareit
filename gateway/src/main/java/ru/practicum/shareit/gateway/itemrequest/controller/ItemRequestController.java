package ru.practicum.shareit.gateway.itemrequest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.consts.DefaultParams;
import ru.practicum.shareit.server.client.itemrequest.ItemRequestClient;
import ru.practicum.shareit.server.constants.HttpHeaderConstants;
import ru.practicum.shareit.server.constants.OpenApiConsts;
import ru.practicum.shareit.server.dto.itemrequest.request.ItemRequestCreationDto;
import ru.practicum.shareit.server.dto.itemrequest.response.ItemRequestDto;
import ru.practicum.shareit.server.dto.validation.Groups;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Tag(name = "Запрос вещи", description = "API для работы с запросами вещей")
@RestController
@RequestMapping("/requests")
@Validated
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ItemRequestDto addItemRequest(
            @RequestBody @Validated(Groups.OnCreate.class) ItemRequestCreationDto requestDto,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return itemRequestClient.addItemRequest(requestDto, userId);
    }

    @Operation(
            summary = "Получение списка запросов вещей от пользователя",
            description = "Получение всех запросов пользователя вместе с данными об ответах на них\n\n" +
                    "В случае, если запросы не найден возвращает пустой список"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Запросы вещей от пользователя найдены",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ItemRequestDto.class))
            )
    )
    @GetMapping
    public List<ItemRequestDto> getItemRequestsByRequesterId(
            @Parameter(
                    name = HttpHeaderConstants.X_SHARER_USER_ID,
                    description = OpenApiConsts.Param.USER_ID,
                    example = OpenApiConsts.Param.USER_ID_EG
            )
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
