package ru.practicum.shareit.gateway.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

    @PostMapping
    public BookingDto addBooking(
            @RequestBody @Validated(Groups.OnCreate.class) BookingCreationDto bookingDto,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam Boolean approved,
            @RequestHeader(HttpHeaderConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.updateBookingStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @PathVariable Long bookingId,
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
