package ru.practicum.shareit.gateway.item.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.client.item.ItemClient;
import ru.practicum.shareit.server.client.item.comment.CommentClient;
import ru.practicum.shareit.server.constants.OpenApiConsts;
import ru.practicum.shareit.server.dto.error.ApiError;
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

@Tag(name = "Вещи", description = "API для работы с вещами")
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

    @Operation(
            summary = "Получение информации о вещах пользователя",
            description = "Получение всех вещей пользователя вместе с " +
                    "информацией о последнем и следующем бронированиях"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Вещи пользователя найдены",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ItemDetailsDto.class))
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Запрос составлен некорректно",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(OpenApiConsts.Response.GET_PAGINATION_BAD_REQUEST)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(OpenApiConsts.Response.USER_NOT_FOUND)
            )
    )
    @GetMapping
    public List<ItemDetailsDto> getItemsByUserId(
            @Parameter(description = OpenApiConsts.Param.USER_ID, example = OpenApiConsts.Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId,

            @Parameter(description = OpenApiConsts.Param.FROM, example = OpenApiConsts.Param.FROM_EG)
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,

            @Parameter(description = OpenApiConsts.Param.SIZE, example = OpenApiConsts.Param.SIZE_EG)
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size
    ) {
        return itemClient.getItemsByUserId(userId, from, size);
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
