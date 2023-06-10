package com.planner.journeyplanner.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.stereotype.Service;
import org.json.JSONObject;


@Service
public class GptService211 {
    private static final int MAX_RETRIES = 5;
    private static final long INITIAL_WAIT_TIME_MS = 1000;

    public String sendMessageToGpt(String text) throws Exception {
        String url = "https://api.openai.com/v1/completions";
        int attempts = 0;
        long waitTime = INITIAL_WAIT_TIME_MS;

        while (attempts <= MAX_RETRIES) {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer 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");

            JSONObject data = new JSONObject();
            data.put("model", "text-davinci-003");
            data.put("prompt", text);
            data.put("max_tokens", 4000);
            data.put("temperature", 1.0);

            con.setDoOutput(true);
            con.getOutputStream().write(data.toString().getBytes());

            if (con.getResponseCode() != 429) {
                String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                        .reduce((a, b) -> a + b).get();

                System.out.println(new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));
                String togo = (new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));

                return togo;
            } else {
                System.out.println("Rate limit reached, waiting for " + waitTime + "ms before retrying...");
                Thread.sleep(waitTime);
                attempts++;
                waitTime *= 2;
            }
        }

        throw new Exception("Failed to get a successful response after " + MAX_RETRIES + " attempts.");
    }
}
