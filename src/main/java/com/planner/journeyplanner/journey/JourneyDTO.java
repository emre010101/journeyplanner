package com.planner.journeyplanner.journey;


import com.planner.journeyplanner.comment.Comment;
import com.planner.journeyplanner.comment.CommentDTO;
import com.planner.journeyplanner.like.LikeDTO;
import com.planner.journeyplanner.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JourneyDTO {
    private Long id;
    private String journeyTitle;
    private String staticMapUrl;
    private String urlToGoGMap;
    private JourneyDetails journeyDetails;
    private LocalDateTime dateCreated;
    private String description;
    private UserDTO userDTO;
    //private List<LikeDTO> likeDTO;
    private Long likesCount;
    private Boolean isUserLike;
    private List<CommentDTO> comments;
    private Long commentsCount;
    private boolean isUserJourney;

    public JourneyDTO(Journey journey, Boolean isUserLike, Long likesCount, List<CommentDTO> comments, Long commentsCount, Long userId) {
        this.id = journey.getId();
        this.journeyTitle = journey.getJourneyTitle();
        this.staticMapUrl = journey.getStaticMapUrl();
        this.urlToGoGMap = journey.getUrlToGoGMap();
        this.journeyDetails = journey.getJourneyDetails();
        this.dateCreated = journey.getDateCreated();
        this.description = journey.getDescription();
        this.userDTO = new UserDTO(journey.getUser());
        this.isUserLike = isUserLike;
        //this.likeDTO = likeDTO;
        this.likesCount = likesCount;
        this.comments = comments;
        this.commentsCount = commentsCount;
        isUserJourney = journey.getUser().getId() == userId;
    }
}