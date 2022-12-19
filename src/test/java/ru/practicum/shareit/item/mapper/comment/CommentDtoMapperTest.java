package ru.practicum.shareit.item.mapper.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.in.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.out.comment.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentDtoMapperTest {
    /**
     * Method under test: {@link CommentDtoMapper#toComment(RequestCommentDto, User, Item, LocalDateTime)}
     */
    @Test
    void testToComment() {
        // test parameters
        final Long authorId      = 1L;
        final Long requesterId   = 2L;
        final Long ownerId       = 3L;
        final Long itemRequestId = 1L;
        final Long itemId        = 1L;
        // test context
        Long              requestCommentDtoId = 1L;
        User              author              = getAuthor(authorId);
        User              requester           = getRequester(requesterId);
        User              owner               = getOwner(ownerId);
        RequestCommentDto requestCommentDto   = getRequestCommentDto(requestCommentDtoId);
        ItemRequest       itemRequest         = getItemRequest(itemRequestId, requester);
        Item              item                = getItem(itemId, Boolean.TRUE, owner, itemRequest);
        LocalDateTime     time                = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);


        Comment comment = CommentDtoMapper.toComment(requestCommentDto, author, item, time);

        author      = getAuthor(authorId);
        requester   = getRequester(requesterId);
        owner       = getOwner(ownerId);
        itemRequest = getItemRequest(itemRequestId, requester);
        item        = getItem(itemId, Boolean.TRUE, owner, itemRequest);
        assertEquals(requestCommentDto.getText(), comment.getText());
        assertEquals(time,                        comment.getCreated());

        assertEquals(author.getId(),    comment.getAuthor().getId());
        assertEquals(author.getName(),  comment.getAuthor().getName());
        assertEquals(author.getEmail(), comment.getAuthor().getEmail());

        assertEquals(item.getId(),          comment.getItem().getId());
        assertEquals(item.getName(),        comment.getItem().getName());
        assertEquals(item.getDescription(), comment.getItem().getDescription());
        assertEquals(item.getIsAvailable(), comment.getItem().getIsAvailable());

        assertEquals(owner.getId(),    comment.getItem().getOwner().getId());
        assertEquals(owner.getName(),  comment.getItem().getOwner().getName());
        assertEquals(owner.getEmail(), comment.getItem().getOwner().getEmail());

        assertEquals(itemRequest.getId(),           comment.getItem().getItemRequest().getId());
        assertEquals(itemRequest.getDescription(),  comment.getItem().getItemRequest().getDescription());
        assertEquals(itemRequest.getCreationTime(), comment.getItem().getItemRequest().getCreationTime());

        assertEquals(requester.getId(),    comment.getItem().getItemRequest().getRequester().getId());
        assertEquals(requester.getName(),  comment.getItem().getItemRequest().getRequester().getName());
        assertEquals(requester.getEmail(), comment.getItem().getItemRequest().getRequester().getEmail());
    }

    /**
     * Method under test: {@link CommentDtoMapper#toCommentDto(Comment)}
     */
    @Test
    void testToCommentDto() {
        // test parameters
        final Long authorId = 1L;
        final Long ownerId = 2L;
        final Long requesterId = 3L;
        final Long itemRequestId = 1L;
        final Long itemId = 1L;
        final Long commentId = 1L;
        // test context
        User        author      = getAuthor(authorId);
        User        requester   = getRequester(requesterId);
        ItemRequest itemRequest = getItemRequest(itemRequestId, requester);
        User        owner       = getOwner(ownerId);
        Item        item        = getItem(itemId, Boolean.TRUE, owner, itemRequest);
        Comment     comment     = getComment(commentId, author, item);

        CommentDto commentDto = CommentDtoMapper.toCommentDto(comment);

        author      = getAuthor(authorId);
        itemRequest = getItemRequest(itemRequestId, requester);
        owner       = getOwner(ownerId);
        item        = getItem(itemId, Boolean.TRUE, owner, itemRequest);
        comment     = getComment(commentId, author, item);
        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getAuthor().getName(), commentDto.getAuthorName());
        assertEquals(comment.getCreated(), commentDto.getCreated());
    }

    /**
     * Method under test: {@link CommentDtoMapper#CommentDtoMapper()}
     */
    @Test
    void testConstructor() throws NoSuchMethodException {
        // test context
        var constructor = CommentDtoMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        var ex = assertThrows(InvocationTargetException.class, constructor::newInstance);

        String message = "This is a utility class and cannot be instantiated";
        assertTrue(ex.getCause() instanceof AssertionError);
        assertEquals(message, ex.getCause().getMessage());
    }

    private User getAuthor(Long authorId) {
        final String name  = String.format("Author%dName", authorId);
        final String email = String.format("user.author%d@example.com", authorId);

        User author = new User();
        author.setId(authorId);
        author.setName(name);
        author.setEmail(email);

        return author;
    }

    private User getRequester(Long requesterId) {
        final String name  = String.format("Requester%dName", requesterId);
        final String email = String.format("user.requester%d@example.com", requesterId);

        User requester = new User();
        requester.setId(requesterId);
        requester.setName(name);
        requester.setEmail(email);

        return requester;
    }

    private User getOwner(Long ownerId) {
        final String name  = String.format("Owner%dName", ownerId);
        final String email = String.format("user.owner%d@example.com", ownerId);

        User requester = new User();
        requester.setId(ownerId);
        requester.setName(name);
        requester.setEmail(email);

        return requester;
    }

    private ItemRequest getItemRequest(Long itemRequestId, User requester) {
        final String        description = String.format("item request %d description", itemRequestId);
        final LocalDateTime time        = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestId);
        itemRequest.setDescription(description);
        itemRequest.setCreationTime(time);
        itemRequest.setRequester(requester);

        return itemRequest;
    }

    private Item getItem(Long itemId, Boolean isAvailable, User owner, ItemRequest itemRequest) {
        final String name        = String.format("Item%dName", itemId);
        final String description = String.format("Item %d description", itemId);

        Item item = new Item();
        item.setId(itemId);
        item.setName(name);
        item.setDescription(description);
        item.setIsAvailable(isAvailable);
        item.setOwner(owner);
        item.setItemRequest(itemRequest);

        return item;
    }

    private Comment getComment(Long commentId, User author, Item item) {
        final String        text = String.format("Comment %d text", commentId);
        final LocalDateTime time = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setText(text);
        comment.setCreated(time);
        comment.setAuthor(author);
        comment.setItem(item);

        return comment;
    }

    private RequestCommentDto getRequestCommentDto(Long requestCommentDtoId) {
        final String text = String.format("Request comment dto %d text", requestCommentDtoId);

        RequestCommentDto commentDto = new RequestCommentDto();
        commentDto.setText(text);

        return commentDto;
    }
}
