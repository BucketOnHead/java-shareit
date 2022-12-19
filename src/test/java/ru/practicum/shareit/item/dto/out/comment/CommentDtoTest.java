package ru.practicum.shareit.item.dto.out.comment;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link CommentDto}
     *   <li>{@link CommentDto#setAuthorName(String)}
     *   <li>{@link CommentDto#setCreated(LocalDateTime)}
     *   <li>{@link CommentDto#setId(Long)}
     *   <li>{@link CommentDto#setText(String)}
     *   <li>{@link CommentDto#getAuthorName()}
     *   <li>{@link CommentDto#getCreated()}
     *   <li>{@link CommentDto#getId()}
     *   <li>{@link CommentDto#getText()}
     * </ul>
     */
    @Test
    void testConstructor() {
        // test parameters
        final Long          id     = 1L;
        final String        author = "CommentDtoAuthor";
        final String        text   = "Comment dto text";
        final LocalDateTime time   = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        // test context
        final CommentDto commentDto = new CommentDto();

        commentDto.setId(id);
        commentDto.setAuthorName(author);
        commentDto.setText(text);
        commentDto.setCreated(time);

        assertEquals(id, commentDto.getId());
        assertEquals(author, commentDto.getAuthorName());
        assertEquals(text, commentDto.getText());
        assertEquals(time, commentDto.getCreated());
    }
}
