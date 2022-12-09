package ru.practicum.shareit.booking.dto.out;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking.Status;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class BookingDto {
    public static final Status DEFAULT_STATUS = Status.WAITING;

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status = DEFAULT_STATUS;
    private BookingUserDto booker;
    private BookingItemDto item;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class BookingItemDto {
        private Long id;
        private String name;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class BookingUserDto {
        private Long id;
    }
}
