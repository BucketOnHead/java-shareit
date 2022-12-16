package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.in.RequestUserDto;
import ru.practicum.shareit.user.dto.out.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDtoMapperTest {
    /**
     * Method under test: {@link UserDtoMapper#toUser(RequestUserDto)}
     */
    @Test
    void testToUser() {
        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setEmail("jane.doe@example.org");
        requestUserDto.setName("Name");
        User actualToUserResult = UserDtoMapper.toUser(requestUserDto);
        assertEquals("jane.doe@example.org", actualToUserResult.getEmail());
        assertEquals("Name", actualToUserResult.getName());
    }

    /**
     * Method under test: {@link UserDtoMapper#toUserDto(Collection)}
     */
    @Test
    void testToUserDto() {
        assertTrue(UserDtoMapper.toUserDto(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link UserDtoMapper#toUserDto(Collection)}
     */
    @Test
    void testToUserDto2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        List<UserDto> actualToUserDtoResult = UserDtoMapper.toUserDto(userList);
        assertEquals(1, actualToUserDtoResult.size());
        UserDto getResult = actualToUserDtoResult.get(0);
        assertEquals("jane.doe@example.org", getResult.getEmail());
        assertEquals("Name", getResult.getName());
        assertEquals(123L, getResult.getId().longValue());
    }

    /**
     * Method under test: {@link UserDtoMapper#toUserDto(Collection)}
     */
    @Test
    void testToUserDto3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user);
        List<UserDto> actualToUserDtoResult = UserDtoMapper.toUserDto(userList);
        assertEquals(2, actualToUserDtoResult.size());
        UserDto getResult = actualToUserDtoResult.get(0);
        assertEquals("Name", getResult.getName());
        UserDto getResult1 = actualToUserDtoResult.get(1);
        assertEquals("Name", getResult1.getName());
        assertEquals(123L, getResult1.getId().longValue());
        assertEquals("jane.doe@example.org", getResult1.getEmail());
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("jane.doe@example.org", getResult.getEmail());
    }

    /**
     * Method under test: {@link UserDtoMapper#toUserDto(User)}
     */
    @Test
    void testToUserDto4() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");
        UserDto actualToUserDtoResult = UserDtoMapper.toUserDto(user);
        assertEquals("jane.doe@example.org", actualToUserDtoResult.getEmail());
        assertEquals("Name", actualToUserDtoResult.getName());
        assertEquals(123L, actualToUserDtoResult.getId().longValue());
    }
}

