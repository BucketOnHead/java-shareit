package ru.practicum.shareit.item.dto.in.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@NoArgsConstructor
public class RequestCommentDto {
    @NotEmpty
    private String text;
}
