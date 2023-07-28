package com.planner.journeyplanner.journey;

import com.planner.journeyplanner.comment.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JourneyDTODeprecated {
    //The attributes from Journey
    private Long id;
    private String journeyTitle;
    private String staticMapUrl;
    private String urlToGoGMap;
    private JourneyDetails journeyDetails;
    private LocalDateTime dateCreated;
    private String description;
    private Long userId;
    ////////////////
    private Long likesCount;
    private List<Comment> comments;
    private Long commentsCount;

    public JourneyDTODeprecated(Journey journey, Long likesCount, List<Comment> comments, Long commentsCount){
        this.likesCount = likesCount;
        this.comments = comments;
        this.commentsCount = commentsCount;

        this.id = journey.getId();
        this.journeyTitle = journey.getJourneyTitle();
        this.staticMapUrl = journey.getStaticMapUrl();
        this.urlToGoGMap = journey.getUrlToGoGMap();
        this.journeyDetails = journey.getJourneyDetails();
        this.dateCreated = journey.getDateCreated();
        this.description = journey.getDescription();
        this.userId = journey.getUser().getId();
    }

}
