package ru.practicum.shareit.item.dto.request.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CommentCreationDto {
    private String text;
}
