package com.example.taskit.core.service;

import com.example.taskit.core.model.Notification;
import com.example.taskit.core.model.User;
import com.example.taskit.core.repository.NotificationRepository;
import com.example.taskit.rest.dto.NotificationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserService userRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllNotifications() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test Notification");

        when(notificationRepository.findAll()).thenReturn(List.of(notification));

        List<Notification> notifications = notificationService.getAllNotifications();
        assertThat(notifications).hasSize(1);
        assertThat(notifications.get(0).getMessage()).isEqualTo("Test Notification");
    }

    @Test
    void testGetNotificationById() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test Notification");

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        Optional<Notification> result = notificationService.getNotificationById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().getMessage()).isEqualTo("Test Notification");
    }

    @Test
    void testFindByRecipientId() {
        User user = new User();
        user.setId(1L);

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setRecipient(user);

        when(notificationRepository.findByRecipientId(user.getId())).thenReturn(List.of(notification));

        List<Notification> notifications = notificationService.findByRecipientId(user.getId());
        assertThat(notifications).hasSize(1);
        assertThat(notifications.get(0).getRecipient()).isEqualTo(user);
    }

    @Test
    void testCreateNotification() {
        // Given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("New Notification");
        notificationDTO.setTimestamp("2024-12-30");
        notificationDTO.setRecipient(1L);

        // Mock UserService behavior
        User user = new User();
        user.setId(1L);

        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));

        // Prepare the Notification object that will be saved
        Notification notification = new Notification();
        notification.setMessage(notificationDTO.getMessage());
        notification.setTimestamp(notificationDTO.getTimestamp());
        notification.setRecipient(user);

        // Mock the behavior of the NotificationRepository
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // When
        Notification createdNotification = notificationService.createNotification(notificationDTO);

        // Then
        assertThat(createdNotification).isNotNull();
        assertThat(createdNotification.getMessage()).isEqualTo("New Notification");
        assertThat(createdNotification.getRecipient()).isEqualTo(user);
        assertThat(createdNotification.getTimestamp()).isEqualTo("2024-12-30");

        // Verify that the save method was called on the repository
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testUpdateNotification() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("Updated Notification");
        notificationDTO.setTimestamp("2024-12-30");
        notificationDTO.setRecipient(1L);

        Notification existingNotification = new Notification();
        existingNotification.setId(1L);
        existingNotification.setMessage("Old Notification");

        User user = new User();
        user.setId(1L);

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(existingNotification));
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.save(existingNotification)).thenReturn(existingNotification);

        Notification updatedNotification = notificationService.updateNotification(1L, notificationDTO);

        assertThat(updatedNotification).isNotNull();
        assertThat(updatedNotification.getMessage()).isEqualTo("Updated Notification");
    }

    @Test
    void testDeleteNotification() {
        doNothing().when(notificationRepository).deleteById(1L);

        notificationService.deleteNotification(1L);

        verify(notificationRepository, times(1)).deleteById(1L);
    }
}
