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
import ru.practicum.shareit.server.constants.OpenApiConsts.Param;
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

    @Operation(
            summary = "Добавление новой вещи",
            description = "Добавление новой вещи, владельцем которой будет пользовать " +
                    "с идентификатором из заголовка"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Вещь добавлена",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ItemDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Запрос составлен некорректно",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(OpenApiConsts.Response.ITEM_REQUEST_BAD_REQUEST)
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
    public ItemDto addItem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные новой вещи")
            @RequestBody @Validated(Groups.OnCreate.class) ItemCreationDto itemDto,

            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long ownerId
    ) {
        return itemClient.addItem(itemDto, ownerId);
    }

    @Operation(
            summary = "Обновление вещи",
            description = "Для обновления вещи используется тоже дто, что и для создания, " +
                    "все параметры НЕ ОБЯЗАТЕЛЬНЫ\n\n" +
                    "Обновить вещь может только ее владелец"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Вещь обновлена",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ItemDto.class)
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
            responseCode = "403",
            description = "Пользователь не является владельцем вещи",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(OpenApiConsts.Response.ITEM_FORBIDDEN)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Необходимые ресурсы не найдены",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = {
                            @ExampleObject(
                                    name = "Пользователь не найден",
                                    value = OpenApiConsts.Response.USER_NOT_FOUND
                            ),
                            @ExampleObject(
                                    name = "Вещь не найдена",
                                    value = OpenApiConsts.Response.ITEM_NOT_FOUND
                            )
                    }
            )
    )
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для обновление")
            @RequestBody @Validated(Groups.OnUpdate.class) ItemCreationDto itemDto,

            @Parameter(description = Param.ITEM_ID, example = Param.ITEM_ID_EG)
            @PathVariable Long itemId,

            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @Operation(
            summary = "Получение информации о вещи",
            description = "Получение информации о вещи вместе с комментариями\n\n" +
                    "Если пользователь является владельцем этой вещи, то к данным " +
                    "добавляется информация о предыдущем и следующем бронированиях"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Вещь найдена",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ItemDetailsDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Вещь не найдена",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(OpenApiConsts.Response.ITEM_NOT_FOUND)
            )
    )
    @GetMapping("/{itemId}")
    public ItemDetailsDto getItemById(
            @Parameter(description = Param.ITEM_ID, example = Param.ITEM_ID_EG)
            @PathVariable Long itemId,

            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
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
            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId,

            @Parameter(description = Param.FROM, example = Param.FROM_EG)
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,

            @Parameter(description = Param.SIZE, example = Param.SIZE_EG)
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size
    ) {
        return itemClient.getItemsByUserId(userId, from, size);
    }

    @Operation(
            summary = "Получение вещей по тексту",
            description = "Поиск вещей по тексту в названии или описании, не учитывающий регистр букв"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Вещи найдены",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ItemDto.class))
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
    @GetMapping("/search")
    public List<ItemDto> getItemsByText(
            @Parameter(description = "Тест для поиска вещей", example = "дрель")
            @RequestParam String text,

            @Parameter(description = Param.FROM, example = Param.FROM_EG)
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,

            @Parameter(description = Param.SIZE, example = Param.SIZE_EG)
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size
    ) {
        return itemClient.getItemsByText(text, from, size);
    }

    @Operation(
            summary = "Добавление комментария (отзыва) к вещи",
            description = "После того, как бронирование вещи завершено, " +
                    "пользователь может оставить комментарий (отзыв) к ней"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Комментарий добавлен",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CommentDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Запрос составлен некорректно",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = {
                            @ExampleObject(
                                    name = "Нельзя оставить комментарий",
                                    description = "Не найдено подтвержденного бронирования или оно ещё не завершено",
                                    value = OpenApiConsts.Response.COMMENT_BAD_REQUEST
                            ),
                            @ExampleObject(
                                    name = "Ошибка валидации",
                                    value = OpenApiConsts.Response.ITEM_REQUEST_BAD_REQUEST
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Необходимые ресурсы не найдены",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = {
                            @ExampleObject(
                                    name = "Пользователь не найден",
                                    value = OpenApiConsts.Response.USER_NOT_FOUND
                            ),
                            @ExampleObject(
                                    name = "Вещь не найдена",
                                    value = OpenApiConsts.Response.ITEM_NOT_FOUND
                            )
                    }
            )
    )
    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные комментария (отзыва)")
            @RequestBody @Validated(Groups.OnCreate.class) CommentCreationDto comment,

            @Parameter(description = Param.ITEM_ID, example = Param.ITEM_ID_EG)
            @PathVariable Long itemId,

            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return commentClient.addComment(comment, userId, itemId);
    }
}
