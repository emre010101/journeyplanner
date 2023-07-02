package com.planner.journeyplanner.service;

import com.planner.journeyplanner.config.AppConfig;
import com.planner.journeyplanner.exception.BadRequestException;
import com.planner.journeyplanner.exception.UnauthorizedException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/*
 * @author: Emre Kavak
 * 17/06/2023
 * GptApiService.java
 * This class is specifically tailored to communicate with the OpenAI GPT API.
* */
@Service
public class GptApiService {

    @Autowired
    private HttpService httpService;

    @Autowired
    private AppConfig config;

    private static String API_KEY;

    @PostConstruct
    public void init() {
        API_KEY = config.getProperty("CHATGPT_API_KEY");
    }

    public String sendRequest(String text) throws IOException, BadRequestException, UnauthorizedException {
        System.out.println("TESTING PURPOSE IN GPTAPISERVICE");
        System.out.println(text);
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = httpService.createConnection(url);
        httpService.setRequestProperties(con, API_KEY);

        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", text);
        data.put("max_tokens", 3800);
        data.put("temperature", 0.4); //Adjusting the randomness

        System.out.println("HEre is the data to check " + "\n");
        System.out.println(data.toString());

        httpService.sendRequestData(con, data);

        int responseCode = con.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            String badResponse = readErrorResponse(con);
            if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                throw new BadRequestException("Invalid request body or parameters here is the details: " + "\n" + badResponse);
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new UnauthorizedException("Invalid API key");
            } else {
                System.out.println(badResponse);
            }
        }

        return httpService.readResponse(con);
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
