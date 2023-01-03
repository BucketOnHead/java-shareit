package ru.practicum.shareit.booking.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class BookItemRequestDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
