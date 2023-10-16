package ru.practicum.shareit.commondto.booking.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class BookingDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    String status;
    UserDto booker;
    ItemDto item;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class ItemDto {
        Long id;
        String name;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class UserDto {
        Long id;
    }
}
