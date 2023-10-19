package ru.practicum.shareit.gateway.booking.controller;

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
import ru.practicum.shareit.server.client.booking.BookingClient;
import ru.practicum.shareit.gateway.booking.validation.annotation.BookingStateEnum;
import ru.practicum.shareit.server.constants.OpenApiConsts;
import ru.practicum.shareit.server.constants.OpenApiConsts.Param;
import ru.practicum.shareit.server.constants.booking.BookingState;
import ru.practicum.shareit.server.dto.booking.request.BookingCreationDto;
import ru.practicum.shareit.server.dto.booking.response.BookingDto;
import ru.practicum.shareit.server.dto.error.ApiError;
import ru.practicum.shareit.server.dto.validation.Groups;
import ru.practicum.shareit.server.constants.HttpHeaderConstants;
import ru.practicum.shareit.gateway.consts.DefaultParams;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Tag(name = "Бронирование", description = "API для работы с бронированием")
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @Operation(
            summary = "Добавление бронирования",
            description = "Добавляет новое бронирование, при условии, что вещь доступна.\n\n" +
                    "Запрещается бронировать свою же вещь"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Бронирование добавлено",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookingDto.class)
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
                                    name = "Вещь не готова к бронированию",
                                    value = OpenApiConsts.Response.BOOK_ITEM_NOT_AVAILABLE
                            ),
                            @ExampleObject(
                                    name = "Некорректный запрос",
                                    value = OpenApiConsts.Response.BOOKING_BAD_REQUEST
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Необходимые ресурсы не найдены или нарушена логика",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = {
                            @ExampleObject(
                                    name = "Пользователь бронирует свою же вещь",
                                    value = OpenApiConsts.Response.BOOKING_SELF_BOOK_NOT_FOUND
                            ),
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
    @PostMapping
    public BookingDto addBooking(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Информация о бронировании")
            @RequestBody @Validated(Groups.OnCreate.class) BookingCreationDto bookingDto,

            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.addBooking(bookingDto, userId);
    }

    @Operation(
            summary = "Подтверждение или отклонение запроса на бронирование",
            description = "Может быть выполнено только владельцем вещи. " +
                    "Затем статус бронирования становится либо APPROVED, либо REJECTED"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Статус бронирования обновлен",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookingDto.class)
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
                                    name = "Бронирование не ожидает обновление статуса",
                                    value = OpenApiConsts.Response.BOOKING_NOT_WAITING
                            ),
                            @ExampleObject(
                                    name = "Ошибка в запросе",
                                    value = OpenApiConsts.Response.BOOKING_AVAILABLE_BAD_REQUEST
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
                                    name = "Бронирование не найдено",
                                    value = OpenApiConsts.Response.BOOKING_NOT_FOUND
                            ),
                            @ExampleObject(
                                    name = "Доступ не найден",
                                    value = OpenApiConsts.Response.BOOKING_ACCESS_NOT_FOUND
                            ),
                            @ExampleObject(
                                    name = "Пользователь не найден",
                                    value = OpenApiConsts.Response.USER_NOT_FOUND
                            )
                    }
            )
    )
    @PatchMapping("/{bookingId}")
    public BookingDto approveOrRejectBooking(
            @Parameter(description = Param.BOOKING_ID, example = Param.BOOKING_ID_EG)
            @PathVariable Long bookingId,

            @Parameter(description = "Подтверждение или отклонение", example = "true")
            @RequestParam Boolean approved,

            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.approveOrRejectBooking(bookingId, approved, userId);
    }

    @Operation(
            summary = "Получение бронирования по идентификатору",
            description = "Получить информацию может только инициатор бронирования " +
                    "или владелец вещи, к которой относится это бронирование"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Бронирование найдено",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookingDto.class)
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
                                    name = "Бронирование не найдено",
                                    value = OpenApiConsts.Response.BOOKING_NOT_FOUND
                            ),
                            @ExampleObject(
                                    name = "Доступ не найден",
                                    value = OpenApiConsts.Response.BOOKING_ACCESS_NOT_FOUND
                            ),
                            @ExampleObject(
                                    name = "Пользователь не найден",
                                    value = OpenApiConsts.Response.USER_NOT_FOUND
                            )
                    }
            )
    )
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(
            @Parameter(description = Param.BOOKING_ID, example = Param.BOOKING_ID_EG)
            @PathVariable Long bookingId,

            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.getBookingById(bookingId, userId);
    }

    @Operation(summary = "Получение всех бронирований от пользователя")
    @ApiResponse(
            responseCode = "200",
            description = "Бронирования найдены",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = BookingDto.class))
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookingDto.class),
                    examples = @ExampleObject(OpenApiConsts.Response.USER_NOT_FOUND)
            )
    )
    @GetMapping
    public List<BookingDto> getBookingsByBookerId(
            @Parameter(description = Param.STATE, schema = @Schema(
                    implementation = BookingState.class,
                    defaultValue = "ALL"))
            @RequestParam(defaultValue = DefaultParams.STATE) @BookingStateEnum String state,

            @Parameter(description = Param.FROM, example = Param.FROM_EG)
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,

            @Parameter(description = Param.SIZE, example = Param.SIZE_EG)
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size,

            @Parameter(description = Param.USER_ID, example = Param.USER_ID_EG)
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.getBookingsByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerId(
            @RequestParam(defaultValue = DefaultParams.STATE) @BookingStateEnum String state,
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.getBookingsByOwnerId(userId, state, from, size);
    }
}
