package ru.practicum.shareit.item.mapper.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.request.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Утилитарный класс для маппинга комментариев.
 */
@UtilityClass
public final class CommentDtoMapper {

    /**
     * Метод для преобразования CommentRequestDto в Comment.
     *
     * @param commentDto объект типа CommentRequestDto, содержащий информацию о комментарии
     * @param author     автор комментария
     * @param item       объект типа Item, к которому написан комментарий
     * @param time       время создания комментария
     * @return объект типа {@link Comment}
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
     * Метод для преобразования Comment в SimpleCommentResponseDto.
     *
     * @param comment объект типа Comment, который нужно преобразовать
     * @return объект типа {@link SimpleCommentResponseDto}
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
