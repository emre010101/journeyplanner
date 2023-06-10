package com.planner.journeyplanner.service;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import org.springframework.stereotype.Service;

import javax.annotation.processing.Completion;
/*
@Service
public class GptService {
    public String sendMessageToGpt(String message) {

        OpenAiService service = new OpenAiService("your_token");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(message)
                .model("text-davinci-002")
                .echo(true)
                .build();

        Completion completion = service.createCompletion(completionRequest);
        StringBuilder result = new StringBuilder();
        completion.getChoices().forEach(choice -> result.append(choice.getText()));

        return result.toString();
    }


}*/
