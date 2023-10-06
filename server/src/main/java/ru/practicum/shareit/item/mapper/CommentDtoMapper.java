package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.request.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface CommentDtoMapper {

    @Mapping(target = "id", ignore = true)
    Comment mapToComment(CommentRequestDto commentDto, User author, Item item);

    @Mapping(target = "authorName", source = "comment.author.name")
    SimpleCommentResponseDto mapToSimpleCommentResponseDto(Comment comment);
}
