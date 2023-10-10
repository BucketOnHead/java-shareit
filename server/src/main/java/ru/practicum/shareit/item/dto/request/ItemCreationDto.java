package ru.practicum.shareit.item.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemCreationDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
