package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.in.RequestUserDto;
import ru.practicum.shareit.user.dto.out.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserController userController;
    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserController#addUser(RequestUserDto)}
     */
    @Test
    void testAddUser() throws Exception {
        // test parameters
        final Long userDtoId = 1L;
        // test context
        final RequestUserDto requestUserDto = getRequestUserDto(userDtoId);
        final UserDto        userDto        = getUserDto(userDtoId, requestUserDto);
        when(userService.addUser(any(RequestUserDto.class))).thenReturn(userDto);

        var requestBuilder = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestUserDto));

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    /**
     * Method under test: {@link UserController#updateUser(RequestUserDto, Long)}
     */
    @Test
    void testUpdateUser() throws Exception {
        // test parameters
        final Long userDtoId = 1L;
        // test context
        final RequestUserDto requestUserDto = getRequestUserDto(userDtoId);
        final UserDto        userDto        = getUserDto(userDtoId, requestUserDto);
        when(userService.updateUser(any(RequestUserDto.class), anyLong())).thenReturn(userDto);

        var requestBuilder = patch("/users/{userId}", userDtoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestUserDto));

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    /**
     * Method under test: {@link UserController#getUserById(Long)}
     */
    @Test
    void testGetUserById() throws Exception {
        // test parameters
        final Long userDtoId = 1L;
        // test context
        final UserDto userDto = getUserDto(userDtoId);
        when(userService.getUserById(anyLong())).thenReturn(userDto);

        var requestBuilder = get("/users/{userId}", userDtoId);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    /**
     * Method under test: {@link UserController#getAllUsers()}
     */
    @Test
    void testGetAllUsers_WithEmptyUserRepository() throws Exception {
        // test parameters
        final List<UserDto> usersDto = new ArrayList<>();
        // test context
        when(userService.getAllUsers()).thenReturn(usersDto);

        var requestBuilder = get("/users");

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(usersDto)));
    }

    /**
     * Method under test: {@link UserController#deleteUserById(Long)}
     */
    @Test
    void testDeleteUserById() throws Exception {
        // test parameters
        final Long userId = 1L;
        // test context
        doNothing().when(userService).deleteUserById(anyLong());

        var requestBuilder = delete("/users/{userId}", userId);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }

    private RequestUserDto getRequestUserDto(Long n) {
        final String         name    = String.format("RequestUserDtoName%d", n);
        final String         email   = String.format("request_user.dto%d@example.org", n);
        final RequestUserDto userDto = new RequestUserDto();

        userDto.setName(name);
        userDto.setEmail(email);

        return userDto;
    }

    private UserDto getUserDto(Long userDtoId, RequestUserDto requestUserDto) {
        final UserDto userDto = new UserDto();

        userDto.setId(userDtoId);
        userDto.setName(requestUserDto.getName());
        userDto.setEmail(requestUserDto.getEmail());

        return userDto;
    }

    private UserDto getUserDto(Long userDtoId) {
        final String  name    = String.format("UserDtoName%d", userDtoId);
        final String  email   = String.format("user.dto%d@example.com", userDtoId);
        final UserDto userDto = new UserDto();

        userDto.setId(userDtoId);
        userDto.setName(name);
        userDto.setEmail(email);

        return userDto;
    }
}
