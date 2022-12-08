package ru.practicum.shareit.booking.dto.out;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking.Status;
import ru.practicum.shareit.item.dto.out.ShortItemDto;
import ru.practicum.shareit.user.dto.out.ShortUserDto;

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
    private ShortUserDto booker;
    private ShortItemDto item;
}
