package com.planner.journeyplanner.Gpt;
import java.io.IOException;
import java.net.HttpRetryException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

/*
 * @author: Emre Kavak
 * 04/06/2023
 * GptService.java
 * The primary service class that uses:
 * 'GptApiService' and 'GptResponseHandler'
 * It builds the message specific format and also handle the request limit.
* */

@Service
public class GptService {
    private static final int MAX_RETRIES = 5;
    private static final long INITIAL_WAIT_TIME_MS = 1000;

    @Autowired
    private GptApiService gptApiService;

    @Autowired
    private GptResponseHandler gptResponseHandler;

    public JSONObject sendMessageToGpt(String text) throws Exception {
        int attempts = 0;
        long waitTime = INITIAL_WAIT_TIME_MS;
        text = buildPrompt(text);

        while (attempts <= MAX_RETRIES) {
            try {
                String response = gptApiService.sendRequest(text);
                System.out.println("LET SEE IF THIS WILL BE PRINTED AFTER SENDING THE REQUEST" + "\n");
                System.out.println("RESPONSE: " + response + "\n");
                String gptResponse = gptResponseHandler.handleResponse(response);
                System.out.println("LET SEE IF THIS WILL BE PRINTED AFTER HANDLING THE RESPONSE" + "\n");
                System.out.println("HANDLEDRESPONSE: " + gptResponse + "\n");
                return gptResponseHandler.parseResponse(gptResponse);
            } catch (IOException e) {
                System.out.println("SOMETHING BAD HAPPENED WITH API CALL");
                if (e instanceof HttpRetryException && ((HttpRetryException) e).responseCode() == 429) {
                    System.out.println("429 HIT HIT HIT HIT");
                    handleRateLimitExceeded(waitTime, attempts++);
                } else {
                    throw new Exception("Received error from API.", e);
                }
            }
        }
        throw new Exception("Failed to get a successful response after " + MAX_RETRIES + " attempts.");
    }

    //It combines user text with default message to make gpt api understand
    private String buildPrompt(String text) {
        return "Can you analyze the text below and provide the origin and destination cities with their full name? " +
                "If any stops or points of interest are mentioned, please include them as well. " +
                "Otherwise, the 'Stops' field should be left empty. " +
                "The response should be in the following format: 'Origin: <origin city>, " +
                "Destination: <destination city>, Stops: [<stop1>, <stop2>, ...]'. " + "\n" + text;

    }

    private void handleRateLimitExceeded(long waitTime, int attempts) throws InterruptedException {
        System.out.println("Rate limit reached, waiting for " + waitTime + "ms before retrying...");
        Thread.sleep(waitTime);
        waitTime *= 2;
    }
}
