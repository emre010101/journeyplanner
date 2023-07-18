package com.planner.journeyplanner.Gpt;

import com.planner.journeyplanner.Gpt.GptService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
*
* */

import java.util.Map;
//@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/jp/gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    //@CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/analyze")
    public ResponseEntity<String> sendMessageToGpt(
            @RequestBody Map<String, String> payload) throws Exception {
        String message = payload.get("message");
        //
        System.out.println("Received message in JourneyController: " + message);
        //
        JSONObject response = gptService.sendMessageToGpt(message);
        //
        System.out.println("Response from GPTService in JourneyController: " + "\n" + response.toString());
        //
        // Manually construct a new JSONObject and populate it
       /* JSONObject journey = new JSONObject();
        journey.put("origin", response.getString("origin"));
        journey.put("destination", response.getString("destination"));*/

      //  System.out.println("Cleaned Response from GPT: " + journey.toString());

        return ResponseEntity.ok().body(response.toString()); // Send it as a String
    }

}
