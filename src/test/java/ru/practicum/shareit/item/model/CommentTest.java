package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CommentTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link Comment}
     *   <li>{@link Comment#setAuthor(User)}
     *   <li>{@link Comment#setCreated(LocalDateTime)}
     *   <li>{@link Comment#setId(Long)}
     *   <li>{@link Comment#setItem(Item)}
     *   <li>{@link Comment#setText(String)}
     *   <li>{@link Comment#getAuthor()}
     *   <li>{@link Comment#getCreated()}
     *   <li>{@link Comment#getId()}
     *   <li>{@link Comment#getItem()}
     *   <li>{@link Comment#getText()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Comment actualComment = new Comment();
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");
        actualComment.setAuthor(user);
        LocalDateTime ofResult = LocalDateTime.of(1, 1, 1, 1, 1);
        actualComment.setCreated(ofResult);
        actualComment.setId(123L);
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
        actualComment.setItem(item);
        actualComment.setText("Text");
        assertSame(user, actualComment.getAuthor());
        assertSame(ofResult, actualComment.getCreated());
        assertEquals(123L, actualComment.getId().longValue());
        assertSame(item, actualComment.getItem());
        assertEquals("Text", actualComment.getText());
    }
}
