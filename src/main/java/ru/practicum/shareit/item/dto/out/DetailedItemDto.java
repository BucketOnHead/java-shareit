package ru.practicum.shareit.item.dto.out;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DetailedItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemBookingDto lastBooking;
    private ItemBookingDto nextBooking;
    private List<ItemCommentDto> comments;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class ItemBookingDto {
        private Long id;
        private Long bookerId;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class ItemCommentDto {
        private Long id;
        private String text;
        private String authorName;
        private LocalDateTime created;
    }
}
