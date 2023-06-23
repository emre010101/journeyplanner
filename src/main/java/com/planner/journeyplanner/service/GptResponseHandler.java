package com.planner.journeyplanner.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * @author: Emre Kavak
 * 17/06/2023
 * GptResponseHandler.java
 * Designed to handle the response from API
* */

//Note : Come back to this class divide parseResponse method to handle mid point and send another API request.
@Service
public class GptResponseHandler {

    @Autowired
    GptApiService gptApiService;

    public String handleResponse(String response) throws Exception {
        return new JSONObject(response.toString()).getJSONArray("choices").getJSONObject(0).getString("text");
    }

    public JSONObject parseResponse(String gptResponse) throws Exception {
        String[] parts = gptResponse.split(", ");
        System.out.println();
        for(String s : parts){
            System.out.print("TEsting the parts" + s + "\t");
        }
        if (parts.length >= 2) {
            String originCity = parts[0].split(": ")[1];
            String destinationCity = parts[1].split(": ")[1];

            JSONObject journey = new JSONObject();
            journey.put("origin", originCity);
            journey.put("destination", destinationCity);

            if (parts.length > 2) {
                addStopsToJourney(journey, gptResponse);
            }
            System.out.println("TESTING THE STOPS after added in GptResponseHandler.jaca: " +"\n" + journey.toString());

            return journey;
        } else {
            throw new Exception("Unexpected GPT-3 response format");
        }
    }

    private void addStopsToJourney(JSONObject journey, String gptResponse) throws Exception {
        String stopsPart = gptResponse.substring(gptResponse.indexOf("Stops: [") + 8, gptResponse.lastIndexOf("]")).trim();
        String[] stopsArray = stopsPart.split(", ");

        System.out.println("It should be 2 : " + stopsArray.length);
        Map<String, List<String>> stops = new HashMap<>();
        int counter = 1;
        for (String stop : stopsArray) {
            System.out.println("COUNTER FOR EACH STOP TOPIC : " + counter++ + "\n") ;
            String request = "Can you find up to 3 " + stop + "s between " +
                    journey.getString("origin") + " and " +
                    journey.getString("destination") + "? Please provide them in a comma-separated list.";

            try {
                String response = gptApiService.sendRequest(request);
                String gptResponseforStops = handleResponse(response);
                List<String> stopList = Arrays.asList(gptResponseforStops.split(", "));
                stops.put(stop, stopList);
            } catch (Exception e) {
                System.out.println("Error finding stops: " + e.getMessage());
            }
        }

        journey.put("stops", new JSONObject(stops));
    }




}
