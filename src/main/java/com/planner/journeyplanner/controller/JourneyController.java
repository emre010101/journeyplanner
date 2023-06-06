package com.planner.journeyplanner.controller;

import com.planner.journeyplanner.model.UserInput;
import com.planner.journeyplanner.service.GptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/journey")
public class JourneyController {

    private final GptService gptService;

    @Autowired
    public JourneyController(GptService gptService) {
        this.gptService = gptService;
    }

    @PostMapping
    public ResponseEntity<String> sendMessageToGpt(@RequestBody UserInput userInput) {
        String gptResponse = gptService.sendMessageToGpt(userInput.getMessage());
        return ResponseEntity.ok(gptResponse);
    }
}
