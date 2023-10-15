package ru.practicum.shareit.item.service.comment;

import ru.practicum.shareit.commondto.item.request.comment.CommentCreationDto;
import ru.practicum.shareit.commondto.item.response.comment.CommentDto;

public interface CommentService {
    CommentDto addComment(CommentCreationDto commentDto, Long authorId, Long itemId);
}
