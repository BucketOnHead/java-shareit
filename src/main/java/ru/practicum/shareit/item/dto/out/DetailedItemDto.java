package ru.practicum.shareit.item.dto.out;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.out.ShortBookingDto;
import ru.practicum.shareit.item.dto.out.comment.CommentDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DetailedItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ShortBookingDto lastBooking;
    private ShortBookingDto nextBooking;
    private List<CommentDto> comments;
}
