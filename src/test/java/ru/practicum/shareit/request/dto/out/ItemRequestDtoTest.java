package ru.practicum.shareit.request.dto.out;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.out.ItemRequestDto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ItemRequestDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link ItemRequestDto}
     *   <li>{@link ItemRequestDto#setCreated(LocalDateTime)}
     *   <li>{@link ItemRequestDto#setDescription(String)}
     *   <li>{@link ItemRequestDto#setId(Long)}
     *   <li>{@link ItemRequestDto#setItems(List)}
     *   <li>{@link ItemRequestDto#getCreated()}
     *   <li>{@link ItemRequestDto#getDescription()}
     *   <li>{@link ItemRequestDto#getId()}
     *   <li>{@link ItemRequestDto#getItems()}
     * </ul>
     */
    @Test
    void testConstructor() {
        // test parameters
        final Long id = 1L;
        final String description = "Item request dto description";
        final LocalDateTime time = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        final List<ItemDto> items = new ArrayList<>();

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(id);
        itemRequestDto.setDescription(description);
        itemRequestDto.setCreated(time);
        itemRequestDto.setItems(items);

        assertEquals(id, itemRequestDto.getId());
        assertEquals(description, itemRequestDto.getDescription());
        assertSame(time, itemRequestDto.getCreated());
        assertSame(items, itemRequestDto.getItems());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link ItemRequestDto.ItemDto}
     *   <li>{@link ItemDto#setId(Long)}
     *   <li>{@link ItemDto#setName(String)}
     *   <li>{@link ItemDto#setDescription(String)}
     *   <li>{@link ItemDto#setAvailable(Boolean)}
     *   <li>{@link ItemDto#setRequestId(Long)}
     *   <li>{@link ItemDto#getId()}
     *   <li>{@link ItemDto#getName()}
     *   <li>{@link ItemDto#getDescription()}
     *   <li>{@link ItemDto#getAvailable()}
     *   <li>{@link ItemDto#getRequestId()}
     * </ul>
     */
    @Test
    void testItemDto() {
        // test parameters
        final Long    id          = 1L;
        final String  name        = "ItemDtoName";
        final String  description = "Item dto description";
        final Boolean available   = Boolean.TRUE;
        final Long    requestId   = 2L;

        ItemDto itemDto = new ItemDto();
        itemDto.setId(id);
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);
        itemDto.setRequestId(requestId);

        assertEquals(id,          itemDto.getId());
        assertEquals(name,        itemDto.getName());
        assertEquals(description, itemDto.getDescription());
        assertEquals(available,   itemDto.getAvailable());
        assertEquals(requestId,   itemDto.getRequestId());
    }
}

