package ru.practicum.shareit.request.dto.in;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestItemRequestDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link RequestItemRequestDto}
     *   <li>{@link RequestItemRequestDto#setDescription(String)}
     *   <li>{@link RequestItemRequestDto#getDescription()}
     * </ul>
     */
    @Test
    void testConstructor() {
        // test parameters
        final String description = "Request item request description";

        RequestItemRequestDto itemRequestDto = new RequestItemRequestDto();
        itemRequestDto.setDescription(description);

        assertEquals(description, itemRequestDto.getDescription());
    }
}

