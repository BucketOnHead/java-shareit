package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link Item}
     *   <li>{@link Item#setDescription(String)}
     *   <li>{@link Item#setId(Long)}
     *   <li>{@link Item#setIsAvailable(Boolean)}
     *   <li>{@link Item#setItemRequest(ItemRequest)}
     *   <li>{@link Item#setName(String)}
     *   <li>{@link Item#setOwner(User)}
     *   <li>{@link Item#getDescription()}
     *   <li>{@link Item#getId()}
     *   <li>{@link Item#getIsAvailable()}
     *   <li>{@link Item#getItemRequest()}
     *   <li>{@link Item#getName()}
     *   <li>{@link Item#getOwner()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Item actualItem = new Item();
        actualItem.setDescription("The characteristics of someone or something");
        actualItem.setId(123L);
        actualItem.setIsAvailable(true);
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user);
        actualItem.setItemRequest(itemRequest);
        actualItem.setName("Name");
        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");
        actualItem.setOwner(user1);
        assertEquals("The characteristics of someone or something", actualItem.getDescription());
        assertEquals(123L, actualItem.getId().longValue());
        assertTrue(actualItem.getIsAvailable());
        assertSame(itemRequest, actualItem.getItemRequest());
        assertEquals("Name", actualItem.getName());
        assertSame(user1, actualItem.getOwner());
    }
}
