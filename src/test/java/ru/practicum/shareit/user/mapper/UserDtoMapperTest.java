package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.in.RequestUserDto;
import ru.practicum.shareit.user.dto.out.UserDto;
import ru.practicum.shareit.user.model.User;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoMapperTest {
    /**
     * Method under test: {@link UserDtoMapper#toUser(RequestUserDto)}
     */
    @Test
    void testToUser() {
        // test parameters
        final Long requestUserDtoId = 1L;
        // test context
        RequestUserDto userDto = getRequestUserDto(requestUserDtoId);

        User user = UserDtoMapper.toUser(userDto);

        userDto = getRequestUserDto(requestUserDtoId);
        assertEquals(userDto.getName(),  user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    /**
     * Method under test: {@link UserDtoMapper#toUserDto(Collection)}
     */
    @Test
    void testToUserDto_WithEmptyCollection() {
        // test context
        List<User> users = getUsers();

        List<UserDto> usersDto = UserDtoMapper.toUserDto(users);

        assertNotNull(usersDto);
        assertTrue(usersDto.isEmpty());
    }

    /**
     * Method under test: {@link UserDtoMapper#toUserDto(Collection)}
     */
    @Test
    void testToUserDto_WithCollection_WithOneUser() {
        // test parameters
        final Long userId = 1L;
        // test context
        User       user  = getUser(userId);
        List<User> users = getUsers(user);

        List<UserDto> usersDto = UserDtoMapper.toUserDto(users);

        user = getUser(userId);
        assertNotNull(usersDto);
        assertEquals(1,      usersDto.size());
        assertEquals(user.getId(),    usersDto.get(0).getId());
        assertEquals(user.getName(),  usersDto.get(0).getName());
        assertEquals(user.getEmail(), usersDto.get(0).getEmail());
    }

    /**
     * Method under test: {@link UserDtoMapper#toUserDto(Collection)}
     */
    @Test
    void testToUser_WithCollection_WithTwoUser() {
        // test parameters
        final Long userId = 1L;
        final Long user2Id = 2L;
        // test context
        User       user  = getUser(userId);
        User       user2 = getUser(user2Id);
        List<User> users = getUsers(user, user2);

        List<UserDto> usersDto = UserDtoMapper.toUserDto(users);

        user  = getUser(userId);
        user2 = getUser(user2Id);
        assertNotNull(usersDto);
        assertEquals(2, usersDto.size());
        assertEquals(user.getId(),     usersDto.get(0).getId());
        assertEquals(user.getName(),   usersDto.get(0).getName());
        assertEquals(user.getEmail(),  usersDto.get(0).getEmail());
        assertEquals(user2.getId(),    usersDto.get(1).getId());
        assertEquals(user2.getName(),  usersDto.get(1).getName());
        assertEquals(user2.getEmail(), usersDto.get(1).getEmail());
    }

    /**
     * Method under test: {@link UserDtoMapper#toUserDto(User)}
     */
    @Test
    void testToUserDto() {
        // test parameters
        final Long userId = 1L;
        // test context
        User user = getUser(userId);

        UserDto userDto = UserDtoMapper.toUserDto(user);

        user = getUser(userId);
        assertNotNull(userDto);
        assertEquals(user.getId(),    userDto.getId());
        assertEquals(user.getName(),  userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    /**
     * Method under test: {@link UserDtoMapper#UserDtoMapper()}
     */
    @Test
    void testConstructor() throws NoSuchMethodException {
        // test context
        var constructor = UserDtoMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        var ex = assertThrows(InvocationTargetException.class, constructor::newInstance);

        String message = "This is a utility class and cannot be instantiated";
        assertTrue(ex.getCause() instanceof AssertionError);
        assertEquals(message, ex.getCause().getMessage());
    }

    private User getUser(Long userId) {
        final String name  = String.format("UserName%d", userId);
        final String email = String.format("user%d@example.org", userId);
        final User   user  = new User();

        user.setId(userId);
        user.setName(name);
        user.setEmail(email);

        return user;
    }

    private RequestUserDto getRequestUserDto(Long n) {
        final String         name    = String.format("RequestUserDto%d", n);
        final String         email   = String.format("requestuser.dto%d@example.com", n);
        final RequestUserDto userDto = new RequestUserDto();

        userDto.setName(name);
        userDto.setEmail(email);

        return userDto;
    }

    private List<User> getUsers(User... users) {
        return Arrays.asList(users);
    }
}
