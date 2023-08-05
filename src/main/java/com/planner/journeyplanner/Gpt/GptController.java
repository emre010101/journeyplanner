package com.planner.journeyplanner.Gpt;

import com.planner.journeyplanner.apisLimit.ApiUsage;
import com.planner.journeyplanner.apisLimit.ApiUsageService;
import com.planner.journeyplanner.apisLimit.Type;
import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.user.User;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
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
    private final AuthenticationService authenticationService;
    private final ApiUsageService apiUsageService;

    //@CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/analyze")
    public ResponseEntity<String> sendMessageToGpt(
            @RequestBody Map<String, String> payload) throws Exception {
        String message = payload.get("message");
        //
        System.out.println("Received message in JourneyController: " + message);
        //
        User currentUser = authenticationService.getAuthenticatedUser(); //Getting the current user who is making the call
        System.out.println("The role: " + currentUser.getRole());
        Boolean runOut = apiUsageService.incrementApiCount(Type.GPT).getRunOut();
        if (runOut) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Request limit exceeded");
        }
        //
        JSONObject response = gptService.sendMessageToGpt(message);
        //
        System.out.println("Response from GPTService in JourneyController: " + "\n" + response.toString());

        return ResponseEntity.ok().body(response.toString()); // Send it as a String
    }

}
