package ru.practicum.shareit.user.dto.in;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestUserDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link RequestUserDto}
     *   <li>{@link RequestUserDto#setEmail(String)}
     *   <li>{@link RequestUserDto#setName(String)}
     *   <li>{@link RequestUserDto#getEmail()}
     *   <li>{@link RequestUserDto#getName()}
     * </ul>
     */
    @Test
    void testConstructor() {
        // test parameters
        final String name  = "RequestUserDtoName";
        final String email = "request_user.dto@example.org";

        RequestUserDto userDto = new RequestUserDto();
        userDto.setName(name);
        userDto.setEmail(email);

        assertEquals(name, userDto.getName());
        assertEquals(email, userDto.getEmail());
    }
}
