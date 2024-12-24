package com.example.taskit.rest.configuration;

import com.example.taskit.api.impl.openai.OpenAIPriorityGeneration;
import com.example.taskit.core.api.prioritygenerator.PriorityGeneration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.theokanning.openai.service.OpenAiService;

@Configuration
public class OpenAIConfiguration {
    @Value("${openai.secret}")
    private String secret;

    @Bean
    public PriorityGeneration generatePriority() {
        return new OpenAIPriorityGeneration(this.openAiService());
    }

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(secret);
    }
}
