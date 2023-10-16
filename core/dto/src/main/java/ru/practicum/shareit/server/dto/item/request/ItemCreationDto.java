package ru.practicum.shareit.server.dto.item.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.dto.validation.Groups;
import ru.practicum.shareit.server.dto.validation.annotation.NullableNotBlank;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemCreationDto {

    @NotBlank(groups = Groups.OnCreate.class)
    @NullableNotBlank(groups = Groups.OnUpdate.class)
    String name;

    @NotBlank(groups = Groups.OnCreate.class)
    @NullableNotBlank(groups = Groups.OnUpdate.class)
    String description;

    @NotNull(groups = Groups.OnCreate.class)
    Boolean available;

    Long requestId;
}
