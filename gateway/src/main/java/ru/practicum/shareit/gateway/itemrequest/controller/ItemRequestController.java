package ru.practicum.shareit.gateway.itemrequest.controller;

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
import ru.practicum.shareit.gateway.consts.DefaultParams;
import ru.practicum.shareit.server.client.itemrequest.ItemRequestClient;
import ru.practicum.shareit.server.constants.HttpHeaderConstants;
import ru.practicum.shareit.server.constants.OpenApiConsts;
import ru.practicum.shareit.server.constants.OpenApiConsts.Param;
import ru.practicum.shareit.server.dto.error.ApiError;
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

    @Operation(
            summary = "Добавление запроса вещи от пользователя",
            description = "Основная часть запроса - описание, в " +
                    "котором пользователь описывает, какая вещь ему нужна"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Запрос на вещь добавлен",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ItemRequestDto.class),
                    examples = @ExampleObject(OpenApiConsts.Response.POST_ITEM_REQUEST_OK)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Запрос составлен некорректно",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(OpenApiConsts.Response.POST_ITEM_REQUEST_BAD_REQUEST)
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
    @PostMapping
    public ItemRequestDto addItemRequest(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные запроса вещи")
            @RequestBody @Validated(Groups.OnCreate.class) ItemRequestCreationDto requestDto,

            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
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
            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return itemRequestClient.getItemRequestsByRequesterId(userId);
    }

    @Operation(
            summary = "Получение данных запроса вещи по идентификатору",
            description = "Получение запроса вещи по его идентификатору вместе с данными об ответах"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Запрос вещи найден",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ItemRequestDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Запрос/пользователь не найден",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = {
                            @ExampleObject(
                                    name = "Запрос не найден",
                                    value = OpenApiConsts.Response.ITEM_REQUEST_NOT_FOUND
                            ),
                            @ExampleObject(
                                    name = "Пользователь не найден",
                                    value = OpenApiConsts.Response.USER_NOT_FOUND
                            )
                    }
            )
    )
    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestsById(
            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId,

            @Parameter(description = Param.ITEM_REQUEST_ID, example = Param.ITEM_REQUEST_ID_EG)
            @PathVariable Long itemRequestId
    ) {
        return itemRequestClient.getItemRequestById(itemRequestId, userId);
    }

    @Operation(
            summary = "Получение списка запросов вещей",
            description = "Получение списка запросов вещей, созданных другими пользователями\n\n" +
                    "Запросы сортируются от более новых к более старым"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Запросы вещей найдены",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ItemRequestDto.class))
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
    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequests(
            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId,

            @Parameter(description = Param.FROM, example = Param.FROM_EG)
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,

            @Parameter(description = Param.SIZE, example = Param.SIZE_EG)
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size
    ) {
        return itemRequestClient.getItemRequests(userId, from, size);
    }
}
