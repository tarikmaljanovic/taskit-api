package com.example.taskit.api.impl.openai;

import com.example.taskit.core.api.prioritygenerator.PriorityGeneration;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class OpenAIPriorityGeneration implements PriorityGeneration {
    private final OpenAiService openAiService;

    public OpenAIPriorityGeneration(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public String generatePriority(String taskDescription) {
        String prompt = "Suggest the priority level (Low, Medium, or High) of the task, based on its description: " + taskDescription;
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("gpt-3.5-turbo-instruct")
                .maxTokens(10)
                .build();
        return openAiService.createCompletion(completionRequest).getChoices().get(0).getText().trim();
    }

}
