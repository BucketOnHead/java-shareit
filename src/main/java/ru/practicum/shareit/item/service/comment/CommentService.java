package ru.practicum.shareit.item.service.comment;

import ru.practicum.shareit.item.dto.in.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.out.comment.CommentDto;

public interface CommentService {
    CommentDto addComment(RequestCommentDto comment, Long itemId, Long userId);
}
