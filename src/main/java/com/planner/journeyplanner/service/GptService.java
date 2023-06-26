package com.planner.journeyplanner.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.planner.journeyplanner.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import javax.annotation.PostConstruct;

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
                if (e instanceof java.net.HttpRetryException && ((HttpRetryException) e).responseCode() == 429) {
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
        return "Can you analyze the text below and provide the origin and destination cities? " +
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

/*
    @Service
    public class GptService {
        private static final int MAX_RETRIES = 5;
        private static final long INITIAL_WAIT_TIME_MS = 1000;

        private static String API_KEY;

        //annotation tells Spring to inject an instance of AppConfig into the GptService bean
        // Autowire AppConfig
        @Autowired
        private AppConfig config;

        //Spring call the method itself after dependency injection completes
        @PostConstruct
        public void init() {
            API_KEY = config.getProperty("CHATGPT_API_KEY");
        }

        public JSONObject sendMessageToGpt(String text) throws Exception {
            String url = "https://api.openai.com/v1/completions"; //"https://api.openai.com/v1/chat/completions"
            int attempts = 0;
            long waitTime = INITIAL_WAIT_TIME_MS;
            text = buildPrompt(text);

            while (attempts <= MAX_RETRIES) {
                HttpURLConnection con = createConnection(url);
                setRequestProperties(con);
                sendRequestData(con, text);

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String gptResponse = readResponse(con);
                    return parseResponse(gptResponse);
                } else if (responseCode == 429) {
                    handleRateLimitExceeded(waitTime, attempts++);
                }else {
                    String errorResponse = readErrorResponse(con);
                    throw new Exception("Received HTTP status code " + responseCode + " from API. Response: " + errorResponse);
                }
            }
            throw new Exception("Failed to get a successful response after " + MAX_RETRIES + " attempts.");
        }

        private String buildPrompt(String text) {
            return "Can you analyze the text below and provide the origin and destination cities? " +
                    "If any stops or points of interest are mentioned, please include them as well. " +
                    "Otherwise, the 'Stops' field should be left empty. " +
                    "The response should be in the following format: 'Origin: <origin city>, " +
                    "Destination: <destination city>, Stops: [<stop1>, <stop2>, ...]'. " + "\n" + text;
//            return "Can you analyze the text below and provide the origin and destination cities in the following format? 'Origin: <origin city>, Destination: <destination city>'. " + "\n" + text;
        }

        private HttpURLConnection createConnection(String url) throws IOException {
            return (HttpURLConnection) new URL(url).openConnection();
        }

        private void setRequestProperties(HttpURLConnection con) throws ProtocolException {
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", API_KEY);
        }

        private void sendRequestData(HttpURLConnection con, String text) throws IOException {
            JSONObject data = new JSONObject();
            data.put("model", "text-davinci-003");   //  text-davinci-003  gpt-3.5-turbo-0613
            data.put("prompt", text);
            data.put("max_tokens", 3900);
            data.put("temperature", 1.0);
            con.setDoOutput(true);
            con.getOutputStream().write(data.toString().getBytes(StandardCharsets.UTF_8));
        }

        private String readResponse(HttpURLConnection con) throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println("After the READ: !!!");
            System.out.println(content.toString());
            return new JSONObject(content.toString()).getJSONArray("choices").getJSONObject(0).getString("text");
        }

        private JSONObject parseResponse(String gptResponse) throws Exception {
            // Parse the response string
            String[] parts = gptResponse.split(", ");
            if (parts.length == 2) {
                String originCity = parts[0].split(": ")[1];  // "Ankara"
                String destinationCity = parts[1].split(": ")[1];  // "Sivas"

                // Construct a new JSON object
                JSONObject journey = new JSONObject();
                journey.put("origin", originCity);
                journey.put("destination", destinationCity);
                return journey;
            } else {
                // Handle the case when GPT-3 response does not contain origin and destination lines
                throw new Exception("Unexpected GPT-3 response format");
            }
        }

        private void handleRateLimitExceeded(long waitTime, int attempts) throws InterruptedException {
            System.out.println("Rate limit reached, waiting for " + waitTime + "ms before retrying...");
            Thread.sleep(waitTime);
            waitTime *= 2;
        }

        private String readErrorResponse(HttpURLConnection con) throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content.toString();
        }

    }
*/