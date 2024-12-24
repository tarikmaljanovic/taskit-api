package com.example.taskit.rest.controllers;

import com.example.taskit.core.model.Project;
import com.example.taskit.core.model.User;
import com.example.taskit.core.service.ProjectService;
import com.example.taskit.rest.dto.ProjectDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<Optional<Project>> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findProjectById(id));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/my-projects/{id}")
    public ResponseEntity<Iterable<Project>> getMyProjects(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findByMembersId(id));
    }

    @RequestMapping(method = RequestMethod.GET, path = {"/owned-projects/{id}"})
    public ResponseEntity<Iterable<Project>> getOwnedProjects(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findByCreatedById(id));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/members/{id}")
    public ResponseEntity<Iterable<User>> getProjectMembers(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getAllMembers(id));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/created-by/{id}")
    public ResponseEntity<User> getProjectOwner(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectOwner(id));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/add-member/{projectId}/{userId}")
    public ResponseEntity<Iterable<User>> addMemberToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        return ResponseEntity.ok(projectService.addMemberToProject(projectId, userId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO project) {
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Project> updateProject(@RequestBody ProjectDTO project) {
        return ResponseEntity.ok(projectService.updateProject(project));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void deleteProjectById(@PathVariable Long id) {
        projectService.deleteProjectById(id);
    }
}
