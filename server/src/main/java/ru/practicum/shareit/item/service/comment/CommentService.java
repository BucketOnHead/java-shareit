package ru.practicum.shareit.item.service.comment;

import ru.practicum.shareit.item.dto.request.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;

public interface CommentService {
    SimpleCommentResponseDto addComment(CommentRequestDto comment, Long itemId, Long authorUserId);
}
