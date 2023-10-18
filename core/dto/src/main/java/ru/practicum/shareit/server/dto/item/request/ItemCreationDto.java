package ru.practicum.shareit.server.dto.item.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.constants.OpenApiConsts;
import ru.practicum.shareit.server.dto.validation.Groups;
import ru.practicum.shareit.server.dto.validation.annotation.NullableNotBlank;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "Информация о вещи")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemCreationDto {

    @Schema(description = OpenApiConsts.Item.NAME, example = OpenApiConsts.Item.NAME_EG)
    @NotBlank(groups = Groups.OnCreate.class)
    @NullableNotBlank(groups = Groups.OnUpdate.class)
    String name;

    @Schema(description = OpenApiConsts.Item.DESCRIPTION, example = OpenApiConsts.Item.DESCRIPTION_EG)
    @NotBlank(groups = Groups.OnCreate.class)
    @NullableNotBlank(groups = Groups.OnUpdate.class)
    String description;

    @Schema(description = OpenApiConsts.Item.IS_AVAILABLE, example = OpenApiConsts.Item.IS_AVAILABLE_EG)
    @NotNull(groups = Groups.OnCreate.class)
    Boolean available;

    @Schema(description = OpenApiConsts.Item.REQUEST_ID, example = OpenApiConsts.Item.REQUEST_ID_EG)
    Long requestId;
}
