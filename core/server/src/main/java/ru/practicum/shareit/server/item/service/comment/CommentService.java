package ru.practicum.shareit.server.item.service.comment;

import ru.practicum.shareit.server.dto.item.request.comment.CommentCreationDto;
import ru.practicum.shareit.server.dto.item.response.comment.CommentDto;

public interface CommentService {
    CommentDto addComment(CommentCreationDto commentDto, Long authorId, Long itemId);
}
