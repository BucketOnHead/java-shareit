package ru.practicum.shareit.booking.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking.Status;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private UserDto booker;
    private ItemDto item;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class ItemDto {
        private Long id;
        private String name;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class UserDto {
        private Long id;
    }
}
