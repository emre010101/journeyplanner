package com.planner.journeyplanner.journey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data //This annotation will create getter and setters
@Builder //To build the object using design patter builder
@NoArgsConstructor //Will create constructor without arguments
@AllArgsConstructor //all the arguments constructor
public class JourneyDetails {

    private Double totalDurationHours;
    private Double totalDurationMinutes;
    private Double totalDistanceKilometers;
    private List<Leg> legs;

}
