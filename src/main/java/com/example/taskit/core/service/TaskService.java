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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PriorityGeneration priorityGeneration;
    private final MailSender mailSender;
    private final NotificationRepository notificationRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository,
            ProjectRepository projectRepository,
            PriorityGeneration priorityGeneration,
            MailSender mailSender,
            NotificationRepository notificationRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.priorityGeneration = priorityGeneration;
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findByAssignedToId(Long assignedToId) {
        return taskRepository.findByAssignedToId(assignedToId);
    }

    public List<Task> findByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

        public Task createTask(TaskDTO task) {
            Task newTask = new Task();
            newTask.setTitle(task.getTitle());
            newTask.setDescription(task.getDescription());
            newTask.setPriority(task.getPriority());
            newTask.setStatus(task.getStatus());

            LocalDate date = LocalDate.parse(task.getDueDate(), formatter);
            Date dueDate = Date.from(date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
            newTask.setDueDate(dueDate);

            Optional<User> user = userRepository.findById(task.getAssignedTo());
            user.ifPresent(newTask::setAssignedTo);

            Optional<Project> project = projectRepository.findById(task.getProject());
            project.ifPresent(newTask::setProject);

            String isoTimestamp = Instant.now().toString();
            Notification notification = new Notification();
            notification.setRecipient(user.get());
            notification.setTimestamp(isoTimestamp);
            notification.setMessage("You have been assigned a task: " + task.getTitle());
            notificationRepository.save(notification);

            this.mailSender.sendNotificationEmail(user.get(), newTask);

            return taskRepository.save(newTask);
        }

        public Task updateTask(TaskDTO task) {
            Task updatedTask = taskRepository.findById(task.getId()).get();
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setPriority(task.getPriority());
            updatedTask.setStatus(task.getStatus());

            LocalDate date = LocalDate.parse(task.getDueDate(), formatter);
            Date dueDate = Date.from(date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
            updatedTask.setDueDate(dueDate);

            Optional<User> user = userRepository.findById(task.getAssignedTo());
            user.ifPresent(updatedTask::setAssignedTo);

            Optional<Project> project = projectRepository.findById(task.getProject());
            project.ifPresent(updatedTask::setProject);

            this.mailSender.sendNotificationEmail(user.get(), updatedTask);

            String isoTimestamp = Instant.now().toString();
            Notification notification = new Notification();
            notification.setRecipient(user.get());
            notification.setTimestamp(isoTimestamp);
            notification.setMessage("Your task has been updated: " + task.getTitle());
            notificationRepository.save(notification);


            return taskRepository.save(updatedTask);
        }

    public Task updateTaskStatus(Long taskId, String newStatus) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setStatus(newStatus);
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found");
        }
    }

    public Task updateTaskPriority(Long taskId, String newPriority) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setPriority(newPriority);
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found");
        }
    }

    public String generatePriority(String description) {
        return priorityGeneration.generatePriority(description);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}
