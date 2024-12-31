package com.example.taskit.core.repository;

import com.example.taskit.core.model.Project;
import com.example.taskit.core.model.Task;
import com.example.taskit.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class TaskRepositoryTest {

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByAssignedToId() {
        User user = new User();
        user.setId(1L);

        Task task1 = new Task();
        task1.setId(1L);
        task1.setAssignedTo(user);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setAssignedTo(user);

        when(taskRepository.findByAssignedToId(user.getId())).thenReturn(List.of(task1, task2));
        List<Task> tasks = taskRepository.findByAssignedToId(user.getId());
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getAssignedTo()).isEqualTo(user);
        assertThat(tasks.get(1).getAssignedTo()).isEqualTo(user);

    }

    @Test
    void testFindByProjectId() {
        Project project = new Project();
        project.setId(1L);

        Task task1 = new Task();
        task1.setId(1L);
        task1.setProject(project);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setProject(project);

        when(taskRepository.findByProjectId(project.getId())).thenReturn(List.of(task1, task2));
        List<Task> tasks = taskRepository.findByProjectId(project.getId());
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getProject()).isEqualTo(project);
        assertThat(tasks.get(1).getProject()).isEqualTo(project);
    }
}
