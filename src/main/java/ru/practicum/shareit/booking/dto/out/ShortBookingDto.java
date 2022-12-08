package ru.practicum.shareit.booking.dto.out;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ShortBookingDto {
    private Long id;
    private Long bookerId;
}
