package com.planner.journeyplanner.controller;

import com.planner.journeyplanner.service.GptService211;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JourneyController {

    private final GptService211 gptService;

    public JourneyController(GptService211 gptService) {
        this.gptService = gptService;
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/api/journey")
    public String sendMessageToGpt(@RequestBody Map<String, String> payload) throws Exception {
        String message = payload.get("message");
        System.out.println("Received message: " + message)  ;
        return gptService.sendMessageToGpt(message);
    }
}
