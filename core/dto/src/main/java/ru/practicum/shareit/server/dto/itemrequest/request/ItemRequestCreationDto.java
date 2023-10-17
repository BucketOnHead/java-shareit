package ru.practicum.shareit.server.dto.itemrequest.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.dto.validation.Groups;

import javax.validation.constraints.NotBlank;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemRequestCreationDto {

    @NotBlank(groups = Groups.OnCreate.class)
    String description;
}
