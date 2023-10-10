package ru.practicum.shareit.item.service.comment;

import ru.practicum.shareit.item.dto.request.comment.CommentCreationDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;

public interface CommentService {
    SimpleCommentResponseDto addComment(CommentCreationDto comment, Long itemId, Long authorUserId);
}
