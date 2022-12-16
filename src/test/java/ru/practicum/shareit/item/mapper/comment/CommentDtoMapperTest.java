package ru.practicum.shareit.item.mapper.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.in.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.out.comment.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CommentDtoMapperTest {
    /**
     * Method under test: {@link CommentDtoMapper#toComment(RequestCommentDto, User, Item, LocalDateTime)}
     */
    @Test
    void testToComment() {
        RequestCommentDto requestCommentDto = new RequestCommentDto();
        requestCommentDto.setText("Text");

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);
        Comment actualToCommentResult = CommentDtoMapper.toComment(requestCommentDto, user, item,
                LocalDateTime.of(1, 1, 1, 1, 1));
        assertSame(user, actualToCommentResult.getAuthor());
        assertEquals("Text", actualToCommentResult.getText());
        assertSame(item, actualToCommentResult.getItem());
        assertEquals("01:01", actualToCommentResult.getCreated().toLocalTime().toString());
    }

    /**
     * Method under test: {@link CommentDtoMapper#toCommentDto(Comment)}
     */
    @Test
    void testToCommentDto() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item);
        comment.setText("Text");
        CommentDto actualToCommentDtoResult = CommentDtoMapper.toCommentDto(comment);
        assertEquals("Name", actualToCommentDtoResult.getAuthorName());
        assertEquals("Text", actualToCommentDtoResult.getText());
        assertEquals(123L, actualToCommentDtoResult.getId().longValue());
        assertEquals("0001-01-01", actualToCommentDtoResult.getCreated().toLocalDate().toString());
    }
}

