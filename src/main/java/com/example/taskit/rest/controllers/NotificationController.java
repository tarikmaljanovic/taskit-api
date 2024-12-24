package com.example.taskit.rest.controllers;

import com.example.taskit.core.model.Notification;
import com.example.taskit.core.service.NotificationService;
import com.example.taskit.rest.dto.NotificationDTO;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<Optional<Notification>> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/user/{id}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findByRecipientId(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationDTO notification) {
        return ResponseEntity.ok(notificationService.createNotification(notification));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public ResponseEntity<Notification> updateNotification(@RequestBody NotificationDTO notification, @PathVariable Long id) {
        return ResponseEntity.ok(notificationService.updateNotification(id, notification));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }
}
