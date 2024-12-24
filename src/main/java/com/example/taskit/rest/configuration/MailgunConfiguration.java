package com.example.taskit.rest.configuration;

import com.example.taskit.core.model.Task;
import com.example.taskit.core.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.taskit.core.api.mailsender.MailSender;
import com.example.taskit.api.impl.mailgun.MailgunMailSender;

@Configuration
public class MailgunConfiguration {
    @Value("${email.mailgun.from-email}")
    private String fromEmail;

    @Value("${email.mailgun.password}")
    private String apiKey;

    @Value("${email.mailgun.username}")
    private String username;

    @Value("${email.mailgun.domain}")
    private String domain;

    @Bean
    public MailSender sendNotificationEmail() {
        return new MailgunMailSender();
    }

}
