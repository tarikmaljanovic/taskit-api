package com.example.taskit.core.service;

import com.example.taskit.core.model.Notification;
import com.example.taskit.core.model.Project;
import com.example.taskit.core.model.User;
import com.example.taskit.core.repository.NotificationRepository;
import com.example.taskit.core.repository.ProjectRepository;
import com.example.taskit.core.repository.UserRepository;
import com.example.taskit.rest.dto.ProjectDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindProjectById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.findProjectById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test Project");
    }

    @Test
    void testGetAllProjects() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<Project> projects = projectService.getAllProjects();

        assertThat(projects).hasSize(1);
        assertThat(projects.get(0).getName()).isEqualTo("Test Project");
    }

    @Test
    void testFindByMembersId() {
        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setId(1L);
        project.setMembers(Set.of(user));

        when(projectRepository.findByMembersId(user.getId())).thenReturn(List.of(project));

        List<Project> projects = projectService.findByMembersId(user.getId());

        assertThat(projects).hasSize(1);
        assertThat(projects.get(0).getMembers()).contains(user);
    }

    @Test
    void testFindByCreatedById() {
        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setId(1L);
        project.setCreatedBy(user);

        when(projectRepository.findByCreatedById(user.getId())).thenReturn(List.of(project));

        List<Project> projects = projectService.findByCreatedById(user.getId());

        assertThat(projects).hasSize(1);
        assertThat(projects.get(0).getCreatedBy()).isEqualTo(user);
    }

    @Test
    void testAddMemberToProject() {
        // Given
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setMembers(new HashSet<>());  // Initialize the members set to avoid NPE

        User member = new User();
        member.setId(1L);

        // Mock the behavior of the repositories
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));  // Return the mock project
        when(userRepository.findById(1L)).thenReturn(Optional.of(member));  // Return the mock member

        String isoTimestamp = Instant.now().toString();
        Notification notification = new Notification();
        notification.setRecipient(member);
        notification.setTimestamp(isoTimestamp);
        notification.setMessage("You have been added to this project: Test Project");

        // Mock the save method for the notification
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Mock the save method for the project, ensuring it returns the updated project
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // When
        Set<User> updatedMembers = projectService.addMemberToProject(1L, 1L);

        // Then
        assertThat(updatedMembers).contains(member);  // Assert that the member was added
        verify(notificationRepository, times(1)).save(any(Notification.class));  // Verify that the notification was saved
        assertThat(project.getMembers()).contains(member);  // Ensure that the member was added to the project
    }

    @Test
    void testCreateProject() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("New Project");
        projectDTO.setDescription("Project Description");
        projectDTO.setCreatedBy(1L);

        User creator = new User();
        creator.setId(1L);

        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setCreatedBy(creator);

        when(userRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project createdProject = projectService.createProject(projectDTO);

        assertThat(createdProject).isNotNull();
        assertThat(createdProject.getName()).isEqualTo("New Project");
        assertThat(createdProject.getDescription()).isEqualTo("Project Description");
    }

    @Test
    void testUpdateProject() {
        // Given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setName("Updated Project");
        projectDTO.setDescription("Updated Description");
        projectDTO.setCreatedBy(1L);
        projectDTO.setMembers(new HashSet<>(Arrays.asList(1L, 2L)));  // Member IDs 1L and 2L

        // Mock the existing project
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("Old Project");
        existingProject.setDescription("Old Description");
        existingProject.setMembers(new HashSet<>());  // Initialize empty set for members

        // Mock user repository
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        // Mock the projectRepository.save() to return the updated project
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project updatedProject = invocation.getArgument(0);
            updatedProject.setId(1L);  // Ensure the ID stays the same
            return updatedProject;
        });

        // When
        Project updatedProject = projectService.updateProject(projectDTO);

        // Then
        assertThat(updatedProject).isNotNull();
        assertThat(updatedProject.getName()).isEqualTo("Updated Project");
        assertThat(updatedProject.getDescription()).isEqualTo("Updated Description");

        // Verify members update
        assertThat(updatedProject.getMembers()).hasSize(2);  // Ensure two members are added
        assertThat(updatedProject.getMembers()).contains(user1, user2);  // Ensure both users are members

        // Optional: log to check actual members in the updated project
        System.out.println("Updated Project Members: " + updatedProject.getMembers());
    }

    @Test
    void testDeleteProjectById() {
        doNothing().when(projectRepository).deleteById(1L);

        projectService.deleteProjectById(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }
}
