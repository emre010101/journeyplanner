package com.planner.journeyplanner.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/*
* @author: Emre Kavak
* 17/06/2023
* HttpService.java
* This class provides utility functions related to HTTP connections.
* */
@Service
public class HttpService {

    //Creates the connection object
    public HttpURLConnection createConnection(String url) throws IOException {
        return (HttpURLConnection) new URL(url).openConnection();
    }

    //Setting the properties
    public void setRequestProperties(HttpURLConnection con, String apiKey) throws ProtocolException {
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", apiKey);
    }
/*
    public void sendRequestData(HttpURLConnection con, JSONObject data) throws IOException {
        con.setDoOutput(true);//as it's post request, it should be set to true
        con.getOutputStream().write(data.toString().getBytes());
    }

 */
    public void sendRequestData(HttpURLConnection con, JSONObject data) throws IOException {
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            os.write(data.toString().getBytes());
            os.flush();
        }
    }


    public String readResponse(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        System.out.println("THIS IS PRINTED IN HttpService.class" + "\n" + content.toString());
        return content.toString();
    }
}
