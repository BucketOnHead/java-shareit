package ru.practicum.shareit.item.dto.out;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link ItemDto}
     *   <li>{@link ItemDto#setAvailable(Boolean)}
     *   <li>{@link ItemDto#setDescription(String)}
     *   <li>{@link ItemDto#setId(Long)}
     *   <li>{@link ItemDto#setName(String)}
     *   <li>{@link ItemDto#setRequestId(Long)}
     *   <li>{@link ItemDto#getAvailable()}
     *   <li>{@link ItemDto#getDescription()}
     *   <li>{@link ItemDto#getId()}
     *   <li>{@link ItemDto#getName()}
     *   <li>{@link ItemDto#getRequestId()}
     * </ul>
     */
    @Test
    void testConstructor() {
        // test parameters
        final Long    id          = 1L;
        final String  name        = "ItemDtoName";
        final String  description = "Item dto description";
        final Boolean available   = Boolean.FALSE;
        final Long    requesterId = 10L;
        // test context
        final ItemDto itemDto = new ItemDto();

        itemDto.setId(id);
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);
        itemDto.setRequestId(requesterId);

        assertEquals(id,          itemDto.getId());
        assertEquals(name,        itemDto.getName());
        assertEquals(description, itemDto.getDescription());
        assertEquals(available,   itemDto.getAvailable());
        assertEquals(requesterId, itemDto.getRequestId());
    }
}
