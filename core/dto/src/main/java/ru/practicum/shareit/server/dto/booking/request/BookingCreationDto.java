package ru.practicum.shareit.server.dto.booking.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.constants.OpenApiConsts;
import ru.practicum.shareit.server.dto.booking.validation.annotation.StartBeforeEnd;
import ru.practicum.shareit.server.dto.validation.Groups;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Данные для обновления/добавления бронирования")
@StartBeforeEnd(groups = Groups.OnCreate.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class BookingCreationDto {

    @Schema(description = OpenApiConsts.Param.ITEM_ID, example = OpenApiConsts.Param.ITEM_ID_EG)
    @NotNull(groups = Groups.OnCreate.class)
    Long itemId;

    @Schema(description = OpenApiConsts.Booking.START)
    @NotNull(groups = Groups.OnCreate.class)
    @FutureOrPresent(groups = Groups.OnCreate.class)
    LocalDateTime start;

    @Schema(description = OpenApiConsts.Booking.END)
    @NotNull(groups = Groups.OnCreate.class)
    @FutureOrPresent(groups = Groups.OnCreate.class)
    LocalDateTime end;
}
