package com.example.taskit.core.repository;

import com.example.taskit.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.taskit.core.model.Notification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class NotificationRepositoryTest {

    @Mock
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByRecipientId() {
        User user = new User();
        user.setId(1L);

        Notification notification = new Notification();
        notification.setRecipient(user);

        when(notificationRepository.findByRecipientId(user.getId())).thenReturn(List.of(notification));

        List<Notification> notifications = notificationRepository.findByRecipientId(user.getId());

        assertThat(notifications).hasSize(1);
        assertThat(notifications.get(0)).isEqualTo(notification);

    }

}
