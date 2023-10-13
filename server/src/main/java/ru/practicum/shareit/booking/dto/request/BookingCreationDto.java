package ru.practicum.shareit.booking.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class BookingCreationDto {
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
