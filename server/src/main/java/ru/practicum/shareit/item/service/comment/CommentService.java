package ru.practicum.shareit.item.service.comment;

import ru.practicum.shareit.item.dto.request.comment.CommentCreationDto;
import ru.practicum.shareit.item.dto.response.comment.CommentDto;

public interface CommentService {
    CommentDto addComment(CommentCreationDto comment, Long authorId, Long itemId);
}
