package ru.practicum.shareit.server.dto.item.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemDetailsDto {
    Long id;
    String name;
    String description;
    Boolean available;
    BookingDto lastBooking;
    BookingDto nextBooking;
    List<CommentDto> comments;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class BookingDto {
        Long id;
        Long bookerId;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class CommentDto {
        Long id;
        String authorName;
        String text;
        LocalDateTime created;
    }
}
