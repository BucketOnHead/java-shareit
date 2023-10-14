package ru.practicum.shareit.commondto.booking.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.commondto.booking.validation.annotation.StartBeforeEnd;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@StartBeforeEnd
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class BookingCreationDto {
    @NotNull
    Long itemId;

    @NotNull
    @FutureOrPresent
    LocalDateTime start;

    @NotNull
    @FutureOrPresent
    LocalDateTime end;
}
