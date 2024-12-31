package com.example.taskit.rest.controller;

import com.example.taskit.core.model.Project;
import com.example.taskit.core.model.User;
import com.example.taskit.core.service.ProjectService;
import com.example.taskit.rest.controllers.ProjectController;
import com.example.taskit.rest.dto.ProjectDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllProjects() throws Exception {
        // Given
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        List<Project> projects = Arrays.asList(project);

        when(projectService.getAllProjects()).thenReturn(projects);

        // When & Then
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Project"));
    }

    @Test
    void testGetProjectById() throws Exception {
        // Given
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        when(projectService.findProjectById(1L)).thenReturn(Optional.of(project));

        // When & Then
        mockMvc.perform(get("/api/projects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void testGetMyProjects() throws Exception {
        // Given
        Project project = new Project();
        project.setId(1L);
        project.setName("User Project");

        List<Project> projects = Arrays.asList(project);

        when(projectService.findByMembersId(1L)).thenReturn(projects);

        // When & Then
        mockMvc.perform(get("/api/projects/my-projects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("User Project"));
    }

    @Test
    void testGetOwnedProjects() throws Exception {
        // Given
        Project project = new Project();
        project.setId(1L);
        project.setName("Owned Project");

        List<Project> projects = Arrays.asList(project);

        when(projectService.findByCreatedById(1L)).thenReturn(projects);

        // When & Then
        mockMvc.perform(get("/api/projects/owned-projects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Owned Project"));
    }

    @Test
    void testGetProjectMembers() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        Set<User> users = new HashSet<>();
        users.add(user);
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setMembers(users);


        when(projectService.getAllMembers(1L)).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/projects/members/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    void testCreateProject() throws Exception {
        // Given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("New Project");
        projectDTO.setDescription("Description of new project");

        Project project = new Project();
        project.setId(1L);
        project.setName(projectDTO.getName());

        when(projectService.createProject(any(ProjectDTO.class))).thenReturn(project);

        // When & Then
        mockMvc.perform(post("/api/projects")
                        .content(objectMapper.writeValueAsString(projectDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Project"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateProject() throws Exception {
        // Given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Updated Project");
        projectDTO.setDescription("Updated Description");

        Project updatedProject = new Project();
        updatedProject.setId(1L);
        updatedProject.setName(projectDTO.getName());

        when(projectService.updateProject(any(ProjectDTO.class))).thenReturn(updatedProject);

        // When & Then
        mockMvc.perform(put("/api/projects")
                        .content(objectMapper.writeValueAsString(projectDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Project"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteProject() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/projects/{id}", 1L))
                .andExpect(status().isOk());

        // Verify that deleteProjectById is called
        verify(projectService, times(1)).deleteProjectById(1L);
    }
}
