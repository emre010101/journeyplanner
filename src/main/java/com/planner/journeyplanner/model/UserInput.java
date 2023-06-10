package com.planner.journeyplanner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInput {
    private String message;

    public UserInput(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

