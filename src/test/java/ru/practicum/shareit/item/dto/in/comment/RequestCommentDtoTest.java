package ru.practicum.shareit.item.dto.in.comment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestCommentDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link RequestCommentDto}
     *   <li>{@link RequestCommentDto#setText(String)}
     *   <li>{@link RequestCommentDto#getText()}
     * </ul>
     */
    @Test
    void testConstructor() {
        // test parameters
        final String text = "Text";
        // test context
        final RequestCommentDto requestCommentDto = new RequestCommentDto();

        requestCommentDto.setText(text);

        assertEquals(text, requestCommentDto.getText());
    }
}

