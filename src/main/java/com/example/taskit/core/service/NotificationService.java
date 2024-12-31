package com.example.taskit.core.service;

import com.example.taskit.core.model.Notification;
import com.example.taskit.core.model.User;
import com.example.taskit.core.repository.NotificationRepository;
import com.example.taskit.rest.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserService userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    public List<Notification> findByRecipientId(Long recipientId) {
        return notificationRepository.findByRecipientId(recipientId);
    }

    public Notification createNotification(NotificationDTO notification) {
        Notification newNotification = new Notification();
        newNotification.setMessage(notification.getMessage());
        newNotification.setTimestamp(notification.getTimestamp());

        Optional<User> recipient = userRepository.getUserById(notification.getRecipient());
        recipient.ifPresent(newNotification::setRecipient);

        return notificationRepository.save(newNotification);
    }

    public Notification updateNotification(Long id, NotificationDTO notification) {
        Optional<Notification> existingNotification = notificationRepository.findById(id);
        if (existingNotification.isPresent()) {
            Notification updatedNotification = existingNotification.get();

            Optional<User> recipient = userRepository.getUserById(notification.getRecipient());
            updatedNotification.setRecipient(recipient.get());

            updatedNotification.setMessage(notification.getMessage());
            updatedNotification.setTimestamp(notification.getTimestamp());
            return notificationRepository.save(updatedNotification);
        }
        throw new RuntimeException("Notification not found");
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
