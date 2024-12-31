package com.example.taskit.rest.controller;

import com.example.taskit.core.model.User;
import com.example.taskit.core.service.UserService;
import com.example.taskit.rest.controllers.UserController;
import com.example.taskit.rest.dto.LoginDTO;
import com.example.taskit.rest.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetUserById() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        // When & Then
        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void testGetUserByEmail() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        // When & Then
        mockMvc.perform(get("/api/users/email-{email}", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User 1");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[1].name").value("User 2"));
    }

    @Test
    void testLogin() throws Exception {
        // Given
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userService.login(any(LoginDTO.class))).thenReturn(Optional.of(user));

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testCreateUser() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setName("New User");
        userDTO.setEmail("new@example.com");
        userDTO.setPassword("password");
        userDTO.setRole("User");

        User user = new User();
        user.setId(1L);
        user.setName(userDTO.getName());

        when(userService.createUser(any(UserDTO.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateUser() throws Exception {
        // Given
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Updated User");
        userDTO.setEmail("updated@example.com");
        userDTO.setPassword("newpassword");
        userDTO.setRole("Admin");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName(userDTO.getName());

        when(userService.updateUser(any(userId.getClass()), any(UserDTO.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/api/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Given
        Long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        // When & Then
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isOk());

        // Verify that deleteUserById is called
        verify(userService, times(1)).deleteUser(userId);
    }
}
