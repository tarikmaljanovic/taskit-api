package com.example.taskit.core.service;

import com.example.taskit.core.model.User;
import com.example.taskit.core.repository.UserRepository;
import com.example.taskit.rest.dto.UserDTO;
import com.example.taskit.rest.dto.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).isEqualTo("Test User");
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test User");
    }

    @Test
    void testGetUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("test@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testLoginSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.login(loginDTO);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testLoginFailure() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("wrongpassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.login(loginDTO);

        assertThat(result).isNotPresent();
    }

    @Test
    void testCreateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("New User");
        userDTO.setEmail("newuser@example.com");
        userDTO.setPassword("password123");
        userDTO.setRole("USER");

        User newUser = new User();
        newUser.setName(userDTO.getName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(userDTO.getPassword());
        newUser.setRole(userDTO.getRole());

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User createdUser = userService.createUser(userDTO);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getName()).isEqualTo("New User");
        assertThat(createdUser.getEmail()).isEqualTo("newuser@example.com");
    }

    @Test
    void testUpdateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Updated User");
        userDTO.setEmail("updateduser@example.com");
        userDTO.setPassword("newpassword123");
        userDTO.setRole("ADMIN");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Old User");
        existingUser.setEmail("olduser@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.updateUser(1L, userDTO);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("Updated User");
        assertThat(updatedUser.getEmail()).isEqualTo("updateduser@example.com");
    }

    @Test
    void testUpdateUserNotFound() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Updated User");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            userService.updateUser(1L, userDTO);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("User not found");
        }
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
