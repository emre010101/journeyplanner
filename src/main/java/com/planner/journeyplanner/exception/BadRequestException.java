package com.planner.journeyplanner.exception;

public class BadRequestException extends Exception{
    public BadRequestException(String invalidRequestBodyOrParameters) {
        super(invalidRequestBodyOrParameters);
    }
}
