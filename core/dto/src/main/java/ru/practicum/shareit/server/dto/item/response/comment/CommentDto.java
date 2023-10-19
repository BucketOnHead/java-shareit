package ru.practicum.shareit.server.dto.item.response.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.constants.OpenApiConsts;

import java.time.LocalDateTime;

@Schema(description = "Данные комментария")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class CommentDto {

    @Schema(description = OpenApiConsts.Comment.ID, example = OpenApiConsts.Comment.ID_EG)
    Long id;

    @Schema(description = OpenApiConsts.Comment.TEXT, example = OpenApiConsts.Comment.TEXT_EG)
    String text;

    @Schema(description = OpenApiConsts.Comment.AUTHOR_NAME, example = OpenApiConsts.Comment.AUTHOR_NAME_EG)
    String authorName;

    @Schema(description = OpenApiConsts.Comment.CREATED)
    LocalDateTime created;
}
