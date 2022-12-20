package ru.practicum.shareit.user.dto.out;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link UserDto}
     *   <li>{@link UserDto#setEmail(String)}
     *   <li>{@link UserDto#setId(Long)}
     *   <li>{@link UserDto#setName(String)}
     *   <li>{@link UserDto#getEmail()}
     *   <li>{@link UserDto#getId()}
     *   <li>{@link UserDto#getName()}
     * </ul>
     */
    @Test
    void testConstructor() {
        // test parameters
        final Long   id    = 1L;
        final String name  = "UserDtoName";
        final String email = "user.dto@example.org";

        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(name);
        userDto.setEmail(email);

        assertEquals(id, userDto.getId());
        assertEquals(name, userDto.getName());
        assertEquals(email, userDto.getEmail());
    }
}
