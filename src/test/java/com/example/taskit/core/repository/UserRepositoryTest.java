package com.example.taskit.core.repository;

import com.example.taskit.core.model.Project;
import com.example.taskit.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByEmail() {
        String email = "test@gmail.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> user1 = userRepository.findByEmail(email);

        assertThat(user1.get().getEmail()).isEqualTo(email);
    }

    @Test
    void testFindByProjectsId() {
        Project project = new Project();
        project.setId(1L);

        User user = new User();
        user.setId(1L);

        Set<Project> projects = new HashSet<>();
        projects.add(project);

        user.setProjectsIn(projects);

        when(userRepository.findByProjectsId(project.getId())).thenReturn(Set.of(user));
        Set<User> users = userRepository.findByProjectsId(project.getId());

        assertThat(users.size()).isEqualTo(1);

    }
}
