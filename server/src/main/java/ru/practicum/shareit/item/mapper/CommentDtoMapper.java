package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.commondto.item.request.comment.CommentCreationDto;
import ru.practicum.shareit.commondto.item.response.comment.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", nullValueMappingStrategy = RETURN_DEFAULT)
public interface CommentDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    Comment mapToComment(CommentCreationDto commentDto, User author, Item item);

    @Mapping(target = "authorName", source = "comment.author.name")
    CommentDto mapToCommentDto(Comment comment);
}
