package com.example.taskit.core.api.mailsender;

import com.example.taskit.core.model.User;
import com.example.taskit.core.model.Task;
import java.util.List;

public interface MailSender {
    String sendNotificationEmail(User user, Task task);
}
