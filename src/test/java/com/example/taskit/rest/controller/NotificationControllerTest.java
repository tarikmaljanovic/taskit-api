package com.example.taskit.rest.controller;

import com.example.taskit.core.model.Notification;
import com.example.taskit.core.service.NotificationService;
import com.example.taskit.rest.controllers.NotificationController;
import com.example.taskit.rest.dto.NotificationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllNotifications() throws Exception {
        // Given
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test Notification");

        List<Notification> notifications = Arrays.asList(notification);

        when(notificationService.getAllNotifications()).thenReturn(notifications);

        // When & Then
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].message").value("Test Notification"));
    }

    @Test
    void testGetNotificationById() throws Exception {
        // Given
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test Notification");

        when(notificationService.getNotificationById(1L)).thenReturn(Optional.of(notification));

        // When & Then
        mockMvc.perform(get("/api/notifications/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test Notification"));
    }

    @Test
    void testGetUserNotifications() throws Exception {
        // Given
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage("User Notification");

        List<Notification> notifications = Arrays.asList(notification);

        when(notificationService.findByRecipientId(1L)).thenReturn(notifications);

        // When & Then
        mockMvc.perform(get("/api/notifications/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].message").value("User Notification"));
    }

    @Test
    void testCreateNotification() throws Exception {
        // Given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("New Notification");
        notificationDTO.setTimestamp("2024-12-30T10:00:00Z");
        notificationDTO.setRecipient(1L);

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage(notificationDTO.getMessage());
        notification.setTimestamp(notificationDTO.getTimestamp());

        when(notificationService.createNotification(any(NotificationDTO.class))).thenReturn(notification);

        // When & Then
        mockMvc.perform(post("/api/notifications")
                        .content(objectMapper.writeValueAsString(notificationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("New Notification"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateNotification() throws Exception {
        // Given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("Updated Notification");
        notificationDTO.setTimestamp("2024-12-31T10:00:00Z");
        notificationDTO.setRecipient(1L);

        Notification updatedNotification = new Notification();
        updatedNotification.setId(1L);
        updatedNotification.setMessage(notificationDTO.getMessage());

        when(notificationService.updateNotification(eq(1L), any(NotificationDTO.class))).thenReturn(updatedNotification);

        // When & Then
        mockMvc.perform(put("/api/notifications/{id}", 1L)
                        .content(objectMapper.writeValueAsString(notificationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated Notification"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteNotification() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/notifications/{id}", 1L))
                .andExpect(status().isOk());

        // Verify that deleteNotification is called
        verify(notificationService, times(1)).deleteNotification(1L);
    }
}