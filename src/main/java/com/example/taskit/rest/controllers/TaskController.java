package com.example.taskit.rest.controllers;

import com.example.taskit.core.model.Task;
import com.example.taskit.core.service.TaskService;
import com.example.taskit.rest.dto.TaskDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.RequestEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public ResponseEntity<Optional<Task>> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findTaskById(id));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/assigned-to/{id}")
    public ResponseEntity<List<Task>> getUserTasks(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findByAssignedToId(id));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/by-project/{id}")
    public ResponseEntity<List<Task>> getProjectTasks(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findByProjectId(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/generate-priority")
    public ResponseEntity<String> generatePriority(@RequestBody String description) {
        return ResponseEntity.ok(taskService.generatePriority(description));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Task> updateTask(@RequestBody TaskDTO task) {
        return ResponseEntity.ok(taskService.updateTask(task));
    }

    @RequestMapping(method = RequestMethod.PUT, path = {"/status-update/{status}/{id}"})
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @PathVariable String status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @RequestMapping(method = RequestMethod.PUT, path = {"/priority-update/{priority}/{id}"})
    public ResponseEntity<Task> updateTaskPriority(@PathVariable Long id, @PathVariable String priority) {
        return ResponseEntity.ok(taskService.updateTaskPriority(id, priority));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
