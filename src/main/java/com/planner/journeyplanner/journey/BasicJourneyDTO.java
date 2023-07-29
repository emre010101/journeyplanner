package com.planner.journeyplanner.journey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicJourneyDTO {
    private Long id;
    private String journeyTitle;
    private String staticMapUrl;
    private String urlToGoGMap;
    private JourneyDetails journeyDetails;
    private LocalDateTime dateCreated;
    private String description;
    private Integer userId;
    private Long likesCount;

    public BasicJourneyDTO(Journey journey, Long likesCount){
        this.id = journey.getId();
        this.journeyTitle = journey.getJourneyTitle();
        this.staticMapUrl = journey.getStaticMapUrl();
        this.urlToGoGMap = journey.getUrlToGoGMap();
        this.journeyDetails = journey.getJourneyDetails();
        this.dateCreated = journey.getDateCreated();
        this.description = journey.getDescription();
        this.userId = journey.getUser().getId();
        this.likesCount = likesCount;
    }
}
