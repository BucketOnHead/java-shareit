package ru.practicum.shareit.server.dto.item.request.comment;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.dto.validation.Groups;

import javax.validation.constraints.NotEmpty;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class CommentCreationDto {
    @NotEmpty(groups = Groups.OnCreate.class)
    String text;
}
