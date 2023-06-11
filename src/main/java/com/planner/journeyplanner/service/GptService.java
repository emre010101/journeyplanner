package com.planner.journeyplanner.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.planner.journeyplanner.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import javax.annotation.PostConstruct;


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
        System.out.println(API_KEY);
    }


    public JSONObject sendMessageToGpt(String text) throws Exception {
        String url = "https://api.openai.com/v1/completions";
        int attempts = 0;
        long waitTime = INITIAL_WAIT_TIME_MS;
        text = "Can you analyze the text below and provide the origin and destination cities in the following format? 'Origin: <origin city>, Destination: <destination city>'. " + "\n" + text;

        while (attempts <= MAX_RETRIES) {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", API_KEY);

            JSONObject data = new JSONObject();
            data.put("model", "text-davinci-003");
            data.put("prompt", text);
            data.put("max_tokens", 4000);
            data.put("temperature", 1.0);

            con.setDoOutput(true);
            con.getOutputStream().write(data.toString().getBytes());

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                String gptResponse = new JSONObject(content.toString()).getJSONArray("choices").getJSONObject(0).getString("text");
                System.out.println(gptResponse);
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
            } else if (responseCode == 429) {
                System.out.println("Rate limit reached, waiting for " + waitTime + "ms before retrying...");
                Thread.sleep(waitTime);
                attempts++;
                waitTime *= 2;
            } else {
                throw new Exception("Received HTTP status code " + responseCode + " from API.");
            }
        }

        throw new Exception("Failed to get a successful response after " + MAX_RETRIES + " attempts.");
    }


    /*public String sendMessageToGpt(String text) throws Exception {
        String url = "https://api.openai.com/v1/completions";
        int attempts = 0;
        long waitTime = INITIAL_WAIT_TIME_MS;

        while (attempts <= MAX_RETRIES) {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
          //  System.out.println("athe time executin" + API_KEY);
            con.setRequestProperty("Authorization", API_KEY);

            JSONObject data = new JSONObject();
            data.put("model", "text-davinci-003");
            data.put("prompt", text);
            data.put("max_tokens", 4000);
            data.put("temperature", 1.0);

            con.setDoOutput(true);
            con.getOutputStream().write(data.toString().getBytes());

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                System.out.println(new JSONObject(content.toString()).getJSONArray("choices").getJSONObject(0).getString("text"));
                String togo = (new JSONObject(content.toString()).getJSONArray("choices").getJSONObject(0).getString("text"));

                return togo;
            } else if (responseCode == 429) {
                System.out.println("Rate limit reached, waiting for " + waitTime + "ms before retrying...");
                Thread.sleep(waitTime);
                attempts++;
                waitTime *= 2;
            } else {
                throw new Exception("Received HTTP status code " + responseCode + " from API.");
            }
        }

        throw new Exception("Failed to get a successful response after " + MAX_RETRIES + " attempts.");
    }*/

}
