package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.in.RequestUserDto;
import ru.practicum.shareit.user.dto.out.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserController#updateUser(RequestUserDto, Long)}
     */
    @Test
    void testUpdateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("jane.doe@example.org");
        userDto.setId(123L);
        userDto.setName("Name");
        when(userService.updateUser((RequestUserDto) any(), (Long) any())).thenReturn(userDto);

        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setEmail("jane.doe@example.org");
        requestUserDto.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(requestUserDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/users/{userId}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"name\":\"Name\",\"email\":\"jane.doe@example.org\"}"));
    }

    /**
     * Method under test: {@link UserController#getUserById(Long)}
     */
    @Test
    void testGetUserById() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("jane.doe@example.org");
        userDto.setId(123L);
        userDto.setName("Name");
        when(userService.getUserById((Long) any())).thenReturn(userDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{userId}", 123L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"name\":\"Name\",\"email\":\"jane.doe@example.org\"}"));
    }

    /**
     * Method under test: {@link UserController#getAllUsers()}
     */
    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserController#getAllUsers()}
     */
    @Test
    void testGetAllUsers2() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/users");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserController#addUser(RequestUserDto)}
     */
    @Test
    void testAddUser() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setEmail("jane.doe@example.org");
        requestUserDto.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(requestUserDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserController#deleteUserById(Long)}
     */
    @Test
    void testDeleteUserById() throws Exception {
        doNothing().when(userService).deleteUserById((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/{userId}", 123L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserController#deleteUserById(Long)}
     */
    @Test
    void testDeleteUserById2() throws Exception {
        doNothing().when(userService).deleteUserById((Long) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/users/{userId}", 123L);
        deleteResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

