package ru.practicum.shareit.item.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
}
