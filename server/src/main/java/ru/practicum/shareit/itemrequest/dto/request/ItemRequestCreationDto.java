package ru.practicum.shareit.itemrequest.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemRequestCreationDto {
    private String description;
}
