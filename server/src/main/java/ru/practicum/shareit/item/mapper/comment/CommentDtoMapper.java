package ru.practicum.shareit.item.mapper.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.request.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Utility class for mapping comments.
 */
@UtilityClass
public final class CommentDtoMapper {

    /**
     * Method for converting CommentRequestDto to Comment.
     *
     * @param commentDto the CommentRequestDto object containing information about the comment
     * @param author     the author of the comment
     * @param item       the Item object to which the comment is written
     * @param time       the time of the comment creation
     * @return the Comment object
     */
    public static Comment toComment(
            CommentRequestDto commentDto,
            User author,
            Item item,
            LocalDateTime time
    ) {
        Comment comment = new Comment();

        comment.setText(commentDto.getText());
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(time);

        return comment;
    }

    /**
     * Method for converting Comment to SimpleCommentResponseDto.
     *
     * @param comment the Comment object to be converted
     * @return the {@link SimpleCommentResponseDto} object
     */
    public static SimpleCommentResponseDto toSimpleCommentResponseDto(Comment comment) {
        SimpleCommentResponseDto commentDto = new SimpleCommentResponseDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }
}
