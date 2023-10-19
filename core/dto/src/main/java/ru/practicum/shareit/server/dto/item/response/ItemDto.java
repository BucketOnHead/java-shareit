package ru.practicum.shareit.server.dto.item.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.constants.OpenApiConsts;

@Schema(description = "Данные вещи")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDto {

    @Schema(description = OpenApiConsts.Item.ID, example = OpenApiConsts.Item.ID_EG)
    Long id;

    @Schema(description = OpenApiConsts.Item.NAME, example = OpenApiConsts.Item.NAME_EG)
    String name;

    @Schema(description = OpenApiConsts.Item.DESCRIPTION, example = OpenApiConsts.Item.DESCRIPTION_EG)
    String description;

    @Schema(description = OpenApiConsts.Item.IS_AVAILABLE, example = OpenApiConsts.Item.IS_AVAILABLE_EG)
    Boolean available;

    @Schema(description = OpenApiConsts.Item.REQUEST_ID, example = OpenApiConsts.Item.REQUEST_ID_EG)
    Long requestId;
}
