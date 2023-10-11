package ru.practicum.shareit.booking.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private UserDto booker;
    private ItemDto item;

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class ItemDto {
        private Long id;
        private String name;
    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class UserDto {
        private Long id;
    }
}
