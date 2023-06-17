package com.planner.journeyplanner.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

/*
 * @author: Emre Kavak
 * 17/06/2023
 * GptResponseHandler.java
 * Designed to handle the response from API
* */

//Note : Come back to this class divide parseResponse method to handle mid point and send another API request.
@Service
public class GptResponseHandler {

    public String handleResponse(String response) throws Exception {
        return new JSONObject(response.toString()).getJSONArray("choices").getJSONObject(0).getString("text");
    }

    public JSONObject parseResponse(String gptResponse) throws Exception {
        String[] parts = gptResponse.split(", ");
        if (parts.length == 2) {
            String originCity = parts[0].split(": ")[1];
            String destinationCity = parts[1].split(": ")[1];

            JSONObject journey = new JSONObject();
            journey.put("origin", originCity);
            journey.put("destination", destinationCity);
            return journey;
        } else {
            throw new Exception("Unexpected GPT-3 response format");
        }
    }
}
