package ru.practicum.shareit.server.dto.itemrequest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.constants.OpenApiConsts;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Запрос вещи")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemRequestDto {

    @Schema(description = OpenApiConsts.ItemRequest.ID, example = OpenApiConsts.ItemRequest.ID_EG)
    Long id;

    @Schema(description = OpenApiConsts.ItemRequest.DESCRIPTION, example = OpenApiConsts.ItemRequest.DESCRIPTION_EG)
    String description;

    @Schema(description = OpenApiConsts.ItemRequest.CREATED)
    LocalDateTime created;

    @Schema(description = OpenApiConsts.ItemRequest.ITEMS)
    List<ItemDto> items;

    @Schema(description = "Вещь, добавленная по запросу")
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class ItemDto {

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
}
