package ru.practicum.shareit.server.dto.item.request.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.constants.OpenApiConsts;
import ru.practicum.shareit.server.dto.validation.Groups;

import javax.validation.constraints.NotEmpty;

@Schema(description = "Данные комментария (отзыва)")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class CommentCreationDto {

    @Schema(description = OpenApiConsts.Comment.TEXT, example = OpenApiConsts.Comment.TEXT_EG)
    @NotEmpty(groups = Groups.OnCreate.class)
    String text;
}
