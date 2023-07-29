package com.planner.journeyplanner.journey;


import com.planner.journeyplanner.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JourneyDTO {
    private Long id;
    private String journeyTitle;
    private String staticMapUrl;
    private String urlToGoGMap;
    private JourneyDetails journeyDetails;
    private LocalDateTime dateCreated;
    private String description;
    private String firstName;
    private String lastName;
    private Integer userId;
    private Long likesCount;
    private List<Comment> comments;
    private Long commentsCount;

    public JourneyDTO(Journey journey, Long likesCount, Long commentsCount, List<Comment> comments){
        this.id = journey.getId();
        this.journeyTitle = journey.getJourneyTitle();
        this.staticMapUrl = journey.getStaticMapUrl();
        this.urlToGoGMap = journey.getUrlToGoGMap();
        this.journeyDetails = journey.getJourneyDetails();
        this.dateCreated = journey.getDateCreated();
        this.description = journey.getDescription();
        this.firstName = journey.getUser().getFirstname();
        this.lastName = journey.getUser().getLastname();
        this.userId = journey.getUser().getId();
        this.likesCount = likesCount;
        this.comments = comments;
        this.commentsCount = commentsCount;
    }
}