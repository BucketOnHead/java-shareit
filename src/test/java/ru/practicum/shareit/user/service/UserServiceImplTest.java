package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Disabled;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test: {@link UserServiceImpl#checkUserExistsById(UserRepository, Long)}
     */
    @Test
    void testCheckUserExistsById() {
        UserRepository userRepository1 = mock(UserRepository.class);
        when(userRepository1.existsById((Long) any())).thenReturn(true);
        UserServiceImpl.checkUserExistsById(userRepository1, 123L);
        verify(userRepository1).existsById((Long) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#checkUserExistsById(UserRepository, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCheckUserExistsById2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.user.exception.UserNotFoundException: USER[ID_123] not found
        //       at ru.practicum.shareit.user.exception.UserNotFoundException.getFromUserId(UserNotFoundException.java:18)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById(UserServiceImpl.java:26)
        //   See https://diff.blue/R013 to resolve this issue.

        UserRepository userRepository1 = mock(UserRepository.class);
        when(userRepository1.existsById((Long) any())).thenReturn(false);
        UserServiceImpl.checkUserExistsById(userRepository1, 123L);
    }

    /**
     * Method under test: {@link UserServiceImpl#checkUserExistsById(UserRepository, Long)}
     */
    @Test
    void testCheckUserExistsById3() {
        UserRepository userRepository1 = mock(UserRepository.class);
        when(userRepository1.existsById((Long) any())).thenThrow(new UserNotFoundException("An error occurred"));
        assertThrows(UserNotFoundException.class, () -> UserServiceImpl.checkUserExistsById(userRepository1, 123L));
        verify(userRepository1).existsById((Long) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addUser(RequestUserDto)}
     */
    @Test
    void testAddUser() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");
        when(userRepository.save((User) any())).thenReturn(user);

        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setEmail("jane.doe@example.org");
        requestUserDto.setName("Name");
        UserDto actualAddUserResult = userServiceImpl.addUser(requestUserDto);
        assertEquals("jane.doe@example.org", actualAddUserResult.getEmail());
        assertEquals("Name", actualAddUserResult.getName());
        assertEquals(123L, actualAddUserResult.getId().longValue());
        verify(userRepository).save((User) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addUser(RequestUserDto)}
     */
    @Test
    void testAddUser2() {
        when(userRepository.save((User) any())).thenThrow(new UserNotFoundException("An error occurred"));

        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setEmail("jane.doe@example.org");
        requestUserDto.setName("Name");
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.addUser(requestUserDto));
        verify(userRepository).save((User) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#updateUser(RequestUserDto, Long)}
     */
    @Test
    void testUpdateUser() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");
        when(userRepository.save((User) any())).thenReturn(user1);
        when(userRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userRepository.existsById((Long) any())).thenReturn(true);

        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setEmail("jane.doe@example.org");
        requestUserDto.setName("Name");
        UserDto actualUpdateUserResult = userServiceImpl.updateUser(requestUserDto, 123L);
        assertEquals("jane.doe@example.org", actualUpdateUserResult.getEmail());
        assertEquals("Name", actualUpdateUserResult.getName());
        assertEquals(123L, actualUpdateUserResult.getId().longValue());
        verify(userRepository).existsById((Long) any());
        verify(userRepository).getReferenceById((Long) any());
        verify(userRepository).save((User) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#updateUser(RequestUserDto, Long)}
     */
    @Test
    void testUpdateUser2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");
        when(userRepository.save((User) any())).thenThrow(new UserNotFoundException("An error occurred"));
        when(userRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userRepository.existsById((Long) any())).thenReturn(true);

        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setEmail("jane.doe@example.org");
        requestUserDto.setName("Name");
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.updateUser(requestUserDto, 123L));
        verify(userRepository).existsById((Long) any());
        verify(userRepository).getReferenceById((Long) any());
        verify(userRepository).save((User) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#updateUser(RequestUserDto, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateUser3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.user.exception.UserNotFoundException: USER[ID_123] not found
        //       at ru.practicum.shareit.user.exception.UserNotFoundException.getFromUserId(UserNotFoundException.java:18)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById(UserServiceImpl.java:26)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.updateUser(UserServiceImpl.java:42)
        //   See https://diff.blue/R013 to resolve this issue.

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");
        when(userRepository.save((User) any())).thenReturn(user1);
        when(userRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userRepository.existsById((Long) any())).thenReturn(false);

        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setEmail("jane.doe@example.org");
        requestUserDto.setName("Name");
        userServiceImpl.updateUser(requestUserDto, 123L);
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserById(Long)}
     */
    @Test
    void testGetUserById() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");
        when(userRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        UserDto actualUserById = userServiceImpl.getUserById(123L);
        assertEquals("jane.doe@example.org", actualUserById.getEmail());
        assertEquals("Name", actualUserById.getName());
        assertEquals(123L, actualUserById.getId().longValue());
        verify(userRepository).existsById((Long) any());
        verify(userRepository).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserById(Long)}
     */
    @Test
    void testGetUserById2() {
        when(userRepository.getReferenceById((Long) any())).thenThrow(new UserNotFoundException("An error occurred"));
        when(userRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserById(123L));
        verify(userRepository).existsById((Long) any());
        verify(userRepository).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserById(Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetUserById3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.user.exception.UserNotFoundException: USER[ID_123] not found
        //       at ru.practicum.shareit.user.exception.UserNotFoundException.getFromUserId(UserNotFoundException.java:18)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById(UserServiceImpl.java:26)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.getUserById(UserServiceImpl.java:51)
        //   See https://diff.blue/R013 to resolve this issue.

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");
        when(userRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userRepository.existsById((Long) any())).thenReturn(false);
        userServiceImpl.getUserById(123L);
    }

    /**
     * Method under test: {@link UserServiceImpl#getAllUsers()}
     */
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(userServiceImpl.getAllUsers().isEmpty());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserServiceImpl#getAllUsers()}
     */
    @Test
    void testGetAllUsers2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("All USER<DTO> returned, {} in total.");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        List<UserDto> actualAllUsers = userServiceImpl.getAllUsers();
        assertEquals(1, actualAllUsers.size());
        UserDto getResult = actualAllUsers.get(0);
        assertEquals("jane.doe@example.org", getResult.getEmail());
        assertEquals("All USER<DTO> returned, {} in total.", getResult.getName());
        assertEquals(123L, getResult.getId().longValue());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserServiceImpl#getAllUsers()}
     */
    @Test
    void testGetAllUsers3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("All USER<DTO> returned, {} in total.");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("All USER<DTO> returned, {} in total.");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        List<UserDto> actualAllUsers = userServiceImpl.getAllUsers();
        assertEquals(2, actualAllUsers.size());
        UserDto getResult = actualAllUsers.get(0);
        assertEquals("All USER<DTO> returned, {} in total.", getResult.getName());
        UserDto getResult1 = actualAllUsers.get(1);
        assertEquals("All USER<DTO> returned, {} in total.", getResult1.getName());
        assertEquals(123L, getResult1.getId().longValue());
        assertEquals("jane.doe@example.org", getResult1.getEmail());
        assertEquals(123L, getResult.getId().longValue());
        assertEquals("jane.doe@example.org", getResult.getEmail());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserServiceImpl#getAllUsers()}
     */
    @Test
    void testGetAllUsers4() {
        when(userRepository.findAll()).thenThrow(new UserNotFoundException("An error occurred"));
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getAllUsers());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUserById(Long)}
     */
    @Test
    void testDeleteUserById() {
        doNothing().when(userRepository).deleteById((Long) any());
        when(userRepository.existsById((Long) any())).thenReturn(true);
        userServiceImpl.deleteUserById(123L);
        verify(userRepository).existsById((Long) any());
        verify(userRepository).deleteById((Long) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUserById(Long)}
     */
    @Test
    void testDeleteUserById2() {
        doThrow(new UserNotFoundException("An error occurred")).when(userRepository).deleteById((Long) any());
        when(userRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.deleteUserById(123L));
        verify(userRepository).existsById((Long) any());
        verify(userRepository).deleteById((Long) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUserById(Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testDeleteUserById3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.user.exception.UserNotFoundException: USER[ID_123] not found
        //       at ru.practicum.shareit.user.exception.UserNotFoundException.getFromUserId(UserNotFoundException.java:18)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById(UserServiceImpl.java:26)
        //       at ru.practicum.shareit.user.service.UserServiceImpl.deleteUserById(UserServiceImpl.java:69)
        //   See https://diff.blue/R013 to resolve this issue.

        doNothing().when(userRepository).deleteById((Long) any());
        when(userRepository.existsById((Long) any())).thenReturn(false);
        userServiceImpl.deleteUserById(123L);
    }
}

