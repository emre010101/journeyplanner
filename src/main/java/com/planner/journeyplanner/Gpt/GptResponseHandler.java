package com.planner.journeyplanner.Gpt;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        return new JSONObject(response).getJSONArray("choices").getJSONObject(0).getString("text");
    }

    public JSONObject parseResponse(String gptResponse) throws Exception {
        JSONObject journey = new JSONObject();

        Pattern originPattern = Pattern.compile("Origin: (.*?),");
        Matcher originMatcher = originPattern.matcher(gptResponse);
        if (originMatcher.find()) {
            journey.put("origin", originMatcher.group(1).trim());
        }

        Pattern destinationPattern = Pattern.compile("Destination: (.*?)(, Stops: |$)");
        Matcher destinationMatcher = destinationPattern.matcher(gptResponse);
        if (destinationMatcher.find()) {
            journey.put("destination", destinationMatcher.group(1).trim());
        }

        Pattern stopsPattern = Pattern.compile("Stops: \\[(.*?)]");
        Matcher stopsMatcher = stopsPattern.matcher(gptResponse);
        if (stopsMatcher.find() && !stopsMatcher.group(1).trim().isEmpty()) {
            System.out.println("\n" + "addStopsToJourney METHOD is invoked!!" + "\n");
            addStopsToJourney(journey, stopsMatcher.group(1).trim());
        }

        if (!journey.has("origin") || !journey.has("destination")) {
            throw new Exception("Unexpected GPT-3 response format");
        }

        return journey;
    }
    private void addStopsToJourney(JSONObject journey, String stops) throws Exception {
        if (!stops.isEmpty()) {
            String[] stopsArray = stops.split(", ");
            Map<String, List<String>> stopsMap = new HashMap<>();
            for (String stop : stopsArray) {
                String request = "Can you find up to 3 " + stop + "s between " +
                        journey.getString("origin") + " and " +
                        journey.getString("destination") + "? Please provide their full addresses for each place in a semicolon separated list, semicolons are only used separating the each address.";
                try {
                    String response = gptApiService.sendRequest(request);
                    String gptResponseforStops = handleResponse(response);
                    System.out.println("THE FULL ADDRESS OF THE STOP" + "\n" + response);
                    List<String> stopList = Arrays.stream(gptResponseforStops.split(";"))
                            .map(String::trim)
                            .collect(Collectors.toList());
                    stopsMap.put(stop, stopList);
                } catch (Exception e) {
                    System.out.println("Error finding stops: " + e.getMessage());
                }
            }
            journey.put("stops", new JSONObject(stopsMap));
        }
    }


    /*

     It should work if there is no response keyword not exist in the response
    public JSONObject parseResponse(String gptResponse) throws Exception {
        String[] parts = gptResponse.split(", ");

        JSONObject journey = new JSONObject();

        for (String part : parts) {
            if (part.contains("Origin: ")) {
                String originCity = part.split(": ")[1];
                System.out.println("Orijin has been found !!!!!!: " + originCity);
                journey.put("origin", originCity);
            } else if (part.contains("Destination: ")) {
                String destinationCity = part.split(": ")[1];
                journey.put("destination", destinationCity);
            } else if (part.contains("Stops: ") && !part.trim().equals("Stops: []")) {
                System.out.println("\n" + "addStopsToJourney METHOD is invoked!!" + "\n");
                System.out.println(journey.toString());
                addStopsToJourney(journey, part);
            }
        }

        if (journey.has("origin") && journey.has("destination")) {
            return journey;
        } else {
            throw new Exception("Unexpected GPT-3 response format");
        }
    }*/


}
