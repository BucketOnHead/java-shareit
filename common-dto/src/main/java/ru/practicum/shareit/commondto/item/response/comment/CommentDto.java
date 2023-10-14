package ru.practicum.shareit.commondto.item.response.comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class CommentDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
