package ru.practicum.shareit.commondto.item.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.commondto.validation.Groups;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PUBLIC)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemCreationDto {

    @NotBlank
    @NotNull(groups = Groups.OnCreate.class)
    String name;

    @NotBlank
    @NotNull(groups = Groups.OnCreate.class)
    String description;

    @NotNull(groups = Groups.OnCreate.class)
    Boolean available;

    Long requestId;
}
