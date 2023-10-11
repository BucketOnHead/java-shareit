package ru.practicum.shareit.booking.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class BookingCreationDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
