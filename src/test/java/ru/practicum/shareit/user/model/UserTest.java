package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link User}
     *   <li>{@link User#setEmail(String)}
     *   <li>{@link User#setId(Long)}
     *   <li>{@link User#setName(String)}
     *   <li>{@link User#getEmail()}
     *   <li>{@link User#getId()}
     *   <li>{@link User#getName()}
     * </ul>
     */
    @Test
    void testConstructor() {
        // test parameters
        final Long   id    = 1L;
        final String name  = "UserName";
        final String email = "user@example.org";

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);

        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }
}
