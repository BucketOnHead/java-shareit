package ru.practicum.shareit.item.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemCreationDto {
    String name;
    String description;
    Boolean available;
    Long requestId;
}
