package com.planner.journeyplanner.journey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //This annotation will create getter and setters
@Builder //To build the object using design patter builder
@NoArgsConstructor //Will create constructor without arguments
@AllArgsConstructor //all the arguments constructor
public class Leg {

    private Integer legNumber;
    private String startLocation;
    private String endLocation;
    private String startTitle;
    private String endTitle;
    private Coordinates startLocationCoordinates;
    private Coordinates endLocationCoordinates;
    private Double durationHours;
    private Double durationMinutes;
    private Double distanceKilometers;

}
