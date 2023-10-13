package ru.practicum.shareit.item.dto.request.comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class CommentCreationDto {
    String text;
}
