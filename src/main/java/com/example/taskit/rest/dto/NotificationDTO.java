package com.example.taskit.rest.dto;

public class NotificationDTO {
    private Long id;
    private Long recipient;
    private String message;
    private String timestamp;

    public NotificationDTO() {}

    public NotificationDTO(Long id, Long recipient, String message, String timestamp) {
        this.id = id;
        this.recipient = recipient;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipient() {
        return recipient;
    }

    public void setRecipient(Long recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
