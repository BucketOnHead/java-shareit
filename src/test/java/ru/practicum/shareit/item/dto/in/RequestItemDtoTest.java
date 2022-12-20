package ru.practicum.shareit.item.dto.in;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestItemDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link RequestItemDto}
     *   <li>{@link RequestItemDto#setAvailable(Boolean)}
     *   <li>{@link RequestItemDto#setDescription(String)}
     *   <li>{@link RequestItemDto#setName(String)}
     *   <li>{@link RequestItemDto#setRequestId(Long)}
     *   <li>{@link RequestItemDto#getAvailable()}
     *   <li>{@link RequestItemDto#getDescription()}
     *   <li>{@link RequestItemDto#getName()}
     *   <li>{@link RequestItemDto#getRequestId()}
     * </ul>
     */
    @Test
    void testConstructor() {
        // test parameters
        final Long    requestItemDtoId = 1L;
        final String  name             = "RequestItemDtoName";
        final String  description      = "Request item dto description";
        final Boolean available        = Boolean.TRUE;
        // test context
        final RequestItemDto requestItemDto = new RequestItemDto();

        requestItemDto.setRequestId(requestItemDtoId);
        requestItemDto.setName(name);
        requestItemDto.setDescription(description);
        requestItemDto.setAvailable(available);

        assertEquals(requestItemDtoId, requestItemDto.getRequestId());
        assertEquals(name,             requestItemDto.getName());
        assertEquals(description,      requestItemDto.getDescription());
        assertEquals(available,        requestItemDto.getAvailable());
    }
}

