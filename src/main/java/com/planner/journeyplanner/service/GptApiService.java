package com.planner.journeyplanner.service;

import com.planner.journeyplanner.config.AppConfig;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
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

    public String sendRequest(String text) throws IOException {
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = httpService.createConnection(url);
        httpService.setRequestProperties(con, API_KEY);

        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", text);
        data.put("max_tokens", 4000);
        data.put("temperature", 1.0);

        httpService.sendRequestData(con, data);

        return httpService.readResponse(con);
    }
}
