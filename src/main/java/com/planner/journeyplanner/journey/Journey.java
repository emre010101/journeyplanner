package com.planner.journeyplanner.journey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //This annotation will create getter and setters
@Builder //To build the object using design patter builder
@NoArgsConstructor //Will create constructor without arguments
@AllArgsConstructor //all the arguments constructor
public class Journey {

    private String staticMapUrl;
    private JourneyDetails journeyDetails;
    private String journeyTitle;
    private String urlToGoGMap;

}
