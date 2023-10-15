package ru.practicum.shareit.commondto.item.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.commondto.validation.Groups;
import ru.practicum.shareit.commondto.validation.annotation.NullableNotBlank;

import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemCreationDto {

    @NullableNotBlank(groups = Groups.OnCreate.class, nullable = false)
    @NullableNotBlank(groups = Groups.OnUpdate.class)
    String name;

    @NullableNotBlank(groups = Groups.OnCreate.class, nullable = false)
    @NullableNotBlank(groups = Groups.OnUpdate.class)
    String description;

    @NotNull(groups = Groups.OnCreate.class)
    Boolean available;

    Long requestId;
}
