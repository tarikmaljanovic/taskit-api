package com.example.taskit.core.service;

import com.example.taskit.core.api.prioritygenerator.PriorityGeneration;
import com.example.taskit.core.api.mailsender.MailSender;
import com.example.taskit.core.model.Notification;
import com.example.taskit.core.model.Project;
import com.example.taskit.core.model.Task;
import com.example.taskit.core.model.User;
import com.example.taskit.core.repository.NotificationRepository;
import com.example.taskit.core.repository.TaskRepository;
import com.example.taskit.core.repository.UserRepository;
import com.example.taskit.core.repository.ProjectRepository;
import com.example.taskit.rest.dto.TaskDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private PriorityGeneration priorityGeneration;

    @Mock
    private MailSender mailSender;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<Task> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Test Task");
    }

    @Test
    void testFindTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.findTaskById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Task");
    }

    @Test
    void testCreateTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("New Task");
        taskDTO.setDescription("Task Description");
        taskDTO.setPriority("High");
        taskDTO.setStatus("Pending");
        taskDTO.setDueDate("2024-12-31");
        taskDTO.setAssignedTo(1L);
        taskDTO.setProject(1L);

        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setId(1L);

        Task newTask = new Task();
        newTask.setTitle(taskDTO.getTitle());
        newTask.setDescription(taskDTO.getDescription());
        newTask.setPriority(taskDTO.getPriority());
        newTask.setStatus(taskDTO.getStatus());
        newTask.setDueDate(Date.from(LocalDate.parse(taskDTO.getDueDate()).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        newTask.setAssignedTo(user);
        newTask.setProject(project);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        // Use ArgumentCaptor to capture the Task object passed to mailSender.sendNotificationEmail()
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        // When
        Task createdTask = taskService.createTask(taskDTO);

        // Then
        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getTitle()).isEqualTo("New Task");

        // Verify that sendNotificationEmail() is called with the correct task object
        verify(mailSender, times(1)).sendNotificationEmail(eq(user), taskCaptor.capture());

        // Assert that the captured Task has the expected properties
        Task capturedTask = taskCaptor.getValue();
        assertThat(capturedTask).isNotNull();
        assertThat(capturedTask.getTitle()).isEqualTo("New Task");
        assertThat(capturedTask.getDescription()).isEqualTo("Task Description");
        assertThat(capturedTask.getPriority()).isEqualTo("High");
        assertThat(capturedTask.getStatus()).isEqualTo("Pending");

        // Verify the task was saved
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    @Test
    void testUpdateTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setTitle("Updated Task");
        taskDTO.setDescription("Updated Description");
        taskDTO.setPriority("Medium");
        taskDTO.setStatus("In Progress");
        taskDTO.setDueDate("2025-01-01");
        taskDTO.setAssignedTo(1L);
        taskDTO.setProject(1L);

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Task");

        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task updatedTask = taskService.updateTask(taskDTO);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getTitle()).isEqualTo("Updated Task");
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(mailSender, times(1)).sendNotificationEmail(eq(user), eq(updatedTask));
    }

    @Test
    void testUpdateTaskStatus() {
        Task task = new Task();
        task.setId(1L);
        task.setStatus("Pending");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTaskStatus(1L, "Completed");

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getStatus()).isEqualTo("Completed");
    }

    @Test
    void testUpdateTaskPriority() {
        Task task = new Task();
        task.setId(1L);
        task.setPriority("Low");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTaskPriority(1L, "High");

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getPriority()).isEqualTo("High");
    }

    @Test
    void testGeneratePriority() {
        String description = "Task description";
        when(priorityGeneration.generatePriority(description)).thenReturn("High");

        String priority = taskService.generatePriority(description);

        assertThat(priority).isEqualTo("High");
    }

    @Test
    void testDeleteTaskById() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTaskById(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
}
