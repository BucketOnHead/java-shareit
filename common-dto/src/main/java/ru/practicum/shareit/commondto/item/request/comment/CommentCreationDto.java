package ru.practicum.shareit.commondto.item.request.comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;

@FieldDefaults(level = AccessLevel.PUBLIC)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class CommentCreationDto {
    @NotEmpty
    String text;
}
