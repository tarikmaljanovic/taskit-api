package com.example.taskit.api.impl.mailgun;

import com.example.taskit.core.api.mailsender.MailSender;
import com.example.taskit.core.model.User;
import com.example.taskit.core.model.Task;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class MailgunMailSender implements MailSender {
    @Value("${email.mailgun.from-email}")
    private String fromEmail;

    @Value("${email.mailgun.password}")
    private String apiKey;

    @Value("${email.mailgun.domain}")
    private String domain;

    public MailgunMailSender() {}

    @Override
    public String sendNotificationEmail(User user, Task task) {
        try {
            String subject = "Task Notification:";
            String body = String.format(
                    "Hello %s,<br><br>You have been assigned a task: %s.<br><br>Details:<br>- Description: %s<br>- Due Date: %s<br><br>Best regards,<br>Taskit Team",
                    user.getName(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate().toString()
            );

            CloseableHttpClient httpClient = HttpClients.createDefault();

            String url = String.format(
                    "https://api.eu.mailgun.net/v3/%s/messages?from=%s&to=%s&subject=%s&html=%s",
                    domain,
                    URLEncoder.encode(fromEmail, StandardCharsets.UTF_8),
                    URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8),
                    URLEncoder.encode(subject, StandardCharsets.UTF_8),
                    URLEncoder.encode(body, StandardCharsets.UTF_8)
            );

            HttpPost request = new HttpPost(url);
            request.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(("api:" + apiKey).getBytes()));

            // Execute the request
            HttpResponse response = httpClient.execute(request);

            // Get the response
            return response.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
