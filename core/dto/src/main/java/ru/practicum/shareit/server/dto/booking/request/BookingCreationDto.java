package ru.practicum.shareit.server.dto.booking.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.dto.booking.validation.annotation.StartBeforeEnd;
import ru.practicum.shareit.server.dto.validation.Groups;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@StartBeforeEnd(groups = Groups.OnCreate.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class BookingCreationDto {
    @NotNull(groups = Groups.OnCreate.class)
    Long itemId;

    @NotNull(groups = Groups.OnCreate.class)
    @FutureOrPresent(groups = Groups.OnCreate.class)
    LocalDateTime start;

    @NotNull(groups = Groups.OnCreate.class)
    @FutureOrPresent(groups = Groups.OnCreate.class)
    LocalDateTime end;
}