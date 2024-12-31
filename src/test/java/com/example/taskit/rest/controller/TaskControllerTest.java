package com.example.taskit.rest.controller;

import com.example.taskit.core.model.Task;
import com.example.taskit.core.service.TaskService;
import com.example.taskit.rest.controllers.TaskController;
import com.example.taskit.rest.dto.TaskDTO;
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

public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllTasks() throws Exception {
        // Given
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        List<Task> tasks = Arrays.asList(task);

        when(taskService.getAllTasks()).thenReturn(tasks);

        // When & Then
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void testGetTaskById() throws Exception {
        // Given
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        when(taskService.findTaskById(1L)).thenReturn(Optional.of(task));

        // When & Then
        mockMvc.perform(get("/api/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void testGetUserTasks() throws Exception {
        // Given
        Task task = new Task();
        task.setId(1L);
        task.setTitle("User Task");

        List<Task> tasks = Arrays.asList(task);

        when(taskService.findByAssignedToId(1L)).thenReturn(tasks);

        // When & Then
        mockMvc.perform(get("/api/tasks/assigned-to/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("User Task"));
    }

    @Test
    void testGetProjectTasks() throws Exception {
        // Given
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Project Task");

        List<Task> tasks = Arrays.asList(task);

        when(taskService.findByProjectId(1L)).thenReturn(tasks);

        // When & Then
        mockMvc.perform(get("/api/tasks/by-project/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Project Task"));
    }

    @Test
    void testCreateTask() throws Exception {
        // Given
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("New Task");
        taskDTO.setDescription("Description of new task");
        taskDTO.setPriority("High");
        taskDTO.setStatus("Pending");

        Task task = new Task();
        task.setId(1L);
        task.setTitle(taskDTO.getTitle());

        when(taskService.createTask(any(TaskDTO.class))).thenReturn(task);

        // When & Then
        mockMvc.perform(post("/api/tasks")
                        .content(objectMapper.writeValueAsString(taskDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGeneratePriority() throws Exception {
        // Given
        String description = "Urgent task";
        String priority = "High";

        when(taskService.generatePriority(description)).thenReturn(priority);

        // When & Then
        mockMvc.perform(post("/api/tasks/generate-priority")
                        .content(description)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("High"));
    }

    @Test
    void testUpdateTask() throws Exception {
        // Given
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Updated Task");
        taskDTO.setDescription("Updated Description");
        taskDTO.setPriority("Medium");
        taskDTO.setStatus("In Progress");

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle(taskDTO.getTitle());

        when(taskService.updateTask(any(TaskDTO.class))).thenReturn(updatedTask);

        // When & Then
        mockMvc.perform(put("/api/tasks")
                        .content(objectMapper.writeValueAsString(taskDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateTaskStatus() throws Exception {
        // Given
        String status = "Completed";
        Task task = new Task();
        task.setId(1L);
        task.setStatus(status);

        when(taskService.updateTaskStatus(1L, status)).thenReturn(task);

        // When & Then
        mockMvc.perform(put("/api/tasks/status-update/{status}/{id}", status, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Completed"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateTaskPriority() throws Exception {
        // Given
        String priority = "Low";
        Task task = new Task();
        task.setId(1L);
        task.setPriority(priority);

        when(taskService.updateTaskPriority(1L, priority)).thenReturn(task);

        // When & Then
        mockMvc.perform(put("/api/tasks/priority-update/{priority}/{id}", priority, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priority").value("Low"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteTask() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/tasks/{id}", 1L))
                .andExpect(status().isOk());

        // Verify that deleteTaskById is called
        verify(taskService, times(1)).deleteTaskById(1L);
    }
}
