package com.planner.journeyplanner.controller;

import com.planner.journeyplanner.service.GptService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JourneyController {

    private final GptService gptService;

    public JourneyController(GptService gptService) {
        this.gptService = gptService;
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/api/journey")
    public ResponseEntity<String> sendMessageToGpt(@RequestBody Map<String, String> payload) throws Exception {
        String message = payload.get("message");
        System.out.println("Received message: " + message);
        JSONObject response = gptService.sendMessageToGpt(message);
        System.out.println("Response from GPT: " + response.toString());
        // Manually construct a new JSONObject and populate it
        JSONObject journey = new JSONObject();
        journey.put("origin", response.getString("origin"));
        journey.put("destination", response.getString("destination"));

        System.out.println("Cleaned Response from GPT: " + journey.toString());

        return ResponseEntity.ok().body(journey.toString()); // Send it as a String
    }

}
