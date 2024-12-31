package com.example.taskit.core.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.taskit.core.model.Project;
import com.example.taskit.core.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ProjectRepositoryTest {

    @Mock
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByMembersId() {
        Project project1 = new Project();
        project1.setId(1L);
        Project project2 = new Project();
        project2.setId(2L);

        User user = new User();
        user.setId(1L);

        Set<User> members = new HashSet<>();
        members.add(user);

        project1.setMembers(members);
        project2.setMembers(members);

        when(projectRepository.findByMembersId(user.getId())).thenReturn(List.of(project1, project2));
        List<Project> projects = projectRepository.findByMembersId(user.getId());
        assertThat(projects.size()).isEqualTo(2);
        assertThat(projects.get(0)).isEqualTo(project1);
        assertThat(projects.get(1)).isEqualTo(project2);
    }

    @Test
    void testFindByCreatedById() {
        User user = new User();
        user.setId(1L);

        Project project1 = new Project();
        project1.setId(1L);
        project1.setCreatedBy(user);

        Project project2 = new Project();
        project2.setId(2L);
        project2.setCreatedBy(user);

        when(projectRepository.findByCreatedById(user.getId())).thenReturn(List.of(project1, project2));
        List<Project> projects = projectRepository.findByCreatedById(user.getId());
        assertThat(projects.size()).isEqualTo(2);
        assertThat(projects.get(0)).isEqualTo(project1);
        assertThat(projects.get(1)).isEqualTo(project2);

    }
}
