package ru.practicum.shareit.server.dto.booking.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.constants.OpenApiConsts;
import ru.practicum.shareit.server.constants.booking.BookingStatus;

import java.time.LocalDateTime;

@Schema(description = "Информация о бронировании")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class BookingDto {

    @Schema(description = OpenApiConsts.Booking.ID, example = OpenApiConsts.Booking.ID_EG)
    Long id;

    @Schema(description = OpenApiConsts.Booking.START)
    LocalDateTime start;

    @Schema(description = OpenApiConsts.Booking.END)
    LocalDateTime end;

    @Schema(implementation = BookingStatus.class,
            description = OpenApiConsts.Booking.STATUS,
            example = OpenApiConsts.Booking.STATUS_EG)
    String status;

    @Schema(description = OpenApiConsts.Booking.BOOKER)
    UserDto booker;

    @Schema(description = OpenApiConsts.Booking.ITEM)
    ItemDto item;

    @Schema(description = "Данные о веще")
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class ItemDto {

        @Schema(description = OpenApiConsts.Item.ID, example = OpenApiConsts.Item.ID_EG)
        Long id;

        @Schema(description = OpenApiConsts.Item.NAME, example = OpenApiConsts.Item.NAME_EG)
        String name;
    }

    @Schema(description = "Данные о пользователе")
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class UserDto {

        @Schema(description = OpenApiConsts.User.ID)
        Long id;
    }
}
