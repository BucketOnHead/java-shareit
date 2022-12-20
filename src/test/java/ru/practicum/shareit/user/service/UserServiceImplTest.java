package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.user.dto.in.RequestUserDto;
import ru.practicum.shareit.user.dto.out.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserServiceImplTest {
    private final UserServiceImpl userServiceImpl;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link UserServiceImpl#checkUserExistsById(UserRepository, Long)}
     */
    @Test
    void testCheckUserExistsById() {
        // test parameters
        final Long userId = 1L;
        // test context
        when(userRepository.existsById(anyLong())).thenReturn(true);

        UserServiceImpl.checkUserExistsById(userRepository, userId);

        verify(userRepository).existsById(userId);
    }

    /**
     * Method under test: {@link UserServiceImpl#checkUserExistsById(UserRepository, Long)}
     */
    @Test
    void testCheckUserExistsById_NotFound() {
        // test parameters
        final Long userId = 1L;
        // test context
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                UserServiceImpl.checkUserExistsById(userRepository, userId));

        verify(userRepository).existsById(userId);
    }

    /**
     * Method under test: {@link UserServiceImpl#addUser(RequestUserDto)}
     */
    @Test
    void testAddUser() {
        // test parameters
        final Long userId           = 1L;
        final Long requestUserDtoId = 1L;
        // test context
        final User           user           = getUser(userId);
        final RequestUserDto requestUserDto = getRequestUserDto(requestUserDtoId);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDto = userServiceImpl.addUser(requestUserDto);

        assertEquals(user.getId(),    userDto.getId());
        assertEquals(user.getName(),  userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());

        verify(userRepository).save(any(User.class));
    }

    /**
     * Method under test: {@link UserServiceImpl#updateUser(RequestUserDto, Long)}
     */
    @Test
    void testUpdateUser() {
        // test parameters
        final Long userId           = 1L;
        final Long user2Id          = 2L;
        final Long requestUserDtoId = 1L;
        // test context
        final User           user           = getUser(userId);
        final User           user2          = getUser(user2Id);
        final RequestUserDto requestUserDto = getRequestUserDto(requestUserDtoId);
        when(userRepository.save(any(User.class))).thenReturn(user2);
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);
        when(userRepository.existsById(anyLong())).thenReturn(true);

        UserDto userDto = userServiceImpl.updateUser(requestUserDto, userId);

        assertEquals(user2.getId(),    userDto.getId());
        assertEquals(user2.getName(),  userDto.getName());
        assertEquals(user2.getEmail(), userDto.getEmail());
        verify(userRepository).existsById(userId);
        verify(userRepository).getReferenceById(userId);
        verify(userRepository).save(any(User.class));
    }

    /**
     * Method under test: {@link UserServiceImpl#updateUser(RequestUserDto, Long)}
     */
    @Test
    void testUpdateUser_NotFound() {
        // test parameters
        final Long userId           = 1L;
        final Long requestUserDtoId = 1L;
        // test context
        final RequestUserDto requestUserDto = getRequestUserDto(requestUserDtoId);
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                userServiceImpl.updateUser(requestUserDto, userId));

        verify(userRepository).existsById(userId);
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserById(Long)}
     */
    @Test
    void testGetUserById() {
        // test parameters
        final Long userId = 1L;
        // test context
        final User user = getUser(userId);
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);
        when(userRepository.existsById(anyLong())).thenReturn(true);

        UserDto userDto = userServiceImpl.getUserById(userId);

        assertEquals(userId,          userDto.getId());
        assertEquals(user.getName(),  userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
        verify(userRepository).existsById(userId);
        verify(userRepository).getReferenceById(userId);
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserById(Long)}
     */
    @Test
    void testGetUserById_NotFound() {
        // test parameters
        final Long userId = 1L;
        // test context
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                userServiceImpl.getUserById(userId));

        verify(userRepository).existsById(userId);
    }

    /**
     * Method under test: {@link UserServiceImpl#getAllUsers()}
     */
    @Test
    void testGetAllUsers_WithEmptyUserRepository() {
        // test context
        final List<User> users = getUsers();
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> allUsers = userServiceImpl.getAllUsers();

        assertTrue(allUsers.isEmpty());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserServiceImpl#getAllUsers()}
     */
    @Test
    void testGetAllUsers_WithUserRepository_WithOneUser() {
        // test parameters
        final Long userId = 1L;
        // test context
        final User       user  = getUser(userId);
        final List<User> users = getUsers(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> usersDto = userServiceImpl.getAllUsers();

        assertEquals(1,      usersDto.size());
        assertEquals(user.getId(),    usersDto.get(0).getId());
        assertEquals(user.getName(),  usersDto.get(0).getName());
        assertEquals(user.getEmail(), usersDto.get(0).getEmail());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserServiceImpl#getAllUsers()}
     */
    @Test
    void testGetAllUsers_WithUserRepository_WithTwoUsers() {
        // test parameters
        final Long userId  = 1L;
        final Long user2Id = 2L;
        // test context
        final User       user  = getUser(userId);
        final User       user2 = getUser(user2Id);
        final List<User> users = getUsers(user, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> usersDto = userServiceImpl.getAllUsers();

        assertEquals(2,       usersDto.size());
        assertEquals(user.getId(),     usersDto.get(0).getId());
        assertEquals(user.getName(),   usersDto.get(0).getName());
        assertEquals(user.getEmail(),  usersDto.get(0).getEmail());
        assertEquals(user2.getId(),    usersDto.get(1).getId());
        assertEquals(user2.getName(),  usersDto.get(1).getName());
        assertEquals(user2.getEmail(), usersDto.get(1).getEmail());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUserById(Long)}
     */
    @Test
    void testDeleteUserById() {
        // test parameters
        final Long userId = 1L;
        // test context
        doNothing().when(userRepository).deleteById(anyLong());
        when(userRepository.existsById(anyLong())).thenReturn(true);

        userServiceImpl.deleteUserById(userId);

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUserById(Long)}
     */
    @Test
    void testDeleteUserById_NotFound() {
        // test parameters
        final Long userId = 1L;
        // test context
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
            userServiceImpl.deleteUserById(userId));

        verify(userRepository).existsById(userId);
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

    private RequestUserDto getRequestUserDto(Long requestUserDtoId) {
        final String         name    = String.format("RequestUserDto%d", requestUserDtoId);
        final String         email   = String.format("request_user.dto%d@example.com", requestUserDtoId);
        final RequestUserDto userDto = new RequestUserDto();

        userDto.setName(name);
        userDto.setEmail(email);

        return userDto;
    }

    private List<User> getUsers(User... users) {
        return Arrays.asList(users);
    }
}
