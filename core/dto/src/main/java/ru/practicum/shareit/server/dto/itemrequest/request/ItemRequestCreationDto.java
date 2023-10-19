package ru.practicum.shareit.server.dto.itemrequest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.constants.OpenApiConsts;
import ru.practicum.shareit.server.dto.validation.Groups;

import javax.validation.constraints.NotBlank;

@Schema(description = "Данные запроса вещи")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemRequestCreationDto {

    @Schema(description = OpenApiConsts.ItemRequest.DESCRIPTION, example = OpenApiConsts.ItemRequest.DESCRIPTION_EG)
    @NotBlank(groups = Groups.OnCreate.class)
    String description;
}
