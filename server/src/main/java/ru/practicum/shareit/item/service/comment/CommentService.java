package ru.practicum.shareit.item.service.comment;

import ru.practicum.shareit.item.dto.request.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.response.comment.CommentDto;

public interface CommentService {
    CommentDto addComment(RequestCommentDto comment, Long itemId, Long userId);
}
