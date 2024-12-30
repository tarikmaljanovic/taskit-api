package com.example.taskit.core.service;

import com.example.taskit.core.model.Notification;
import com.example.taskit.core.model.Project;
import com.example.taskit.core.model.User;
import com.example.taskit.core.repository.NotificationRepository;
import com.example.taskit.core.repository.ProjectRepository;
import com.example.taskit.core.repository.UserRepository;
import com.example.taskit.rest.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository,
                          NotificationRepository notificationRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public Optional<Project> findProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> findByMembersId(Long memberId) {
        return projectRepository.findByMembersId(memberId);
    }

    public List<Project> findByCreatedById(Long userId) {
        return projectRepository.findByCreatedById(userId);
    }

    public Set<User> getAllMembers(Long projectId) {
        return projectRepository.findById(projectId).get().getMembers();
    }

    public User getProjectOwner(Long projectId) {
        return projectRepository.findById(projectId).get().getCreatedBy();
    }

    public Set<User> addMemberToProject(Long projectId, Long memberId) {
        Project project = projectRepository.findById(projectId).get();
        User member = userRepository.findById(memberId).get();
        project.getMembers().add(member);

        String isoTimestamp = Instant.now().toString();
        Notification notification = new Notification();
        notification.setRecipient(member);
        notification.setTimestamp(isoTimestamp);
        notification.setMessage("You have been added to this project: " + project.getName());
        notificationRepository.save(notification);


        return projectRepository.save(project).getMembers();
    }

    public Project createProject(ProjectDTO project) {
        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setDescription(project.getDescription());

        Optional<User> user = userRepository.findById(project.getCreatedBy());
        user.ifPresent(newProject::setCreatedBy);

        return projectRepository.save(newProject);
    }

    public Project updateProject(ProjectDTO project) {
        Project updatedProject = projectRepository.findById(project.getId()).get();
        updatedProject.setName(project.getName());
        updatedProject.setDescription(project.getDescription());

        User creator = userRepository.findById(project.getCreatedBy()).get();
        updatedProject.setCreatedBy(creator);

        if(project.getMembers() != null) {
            Set<User> members = null;
            for(Long memberId : project.getMembers()){
                assert members != null;
                members.add(userRepository.findById(memberId).get());
            }
            updatedProject.setMembers(members);
        }

        return projectRepository.save(updatedProject);
    }

    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }
}
