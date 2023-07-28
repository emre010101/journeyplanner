package com.planner.journeyplanner.journey;


import com.planner.journeyplanner.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JourneyDTO extends BasicJourneyDTO {
    private List<Comment> comments;
    private Long commentsCount;

    public JourneyDTO(Journey journey, Long likesCount, List<Comment> comments, Long commentsCount){
        super(journey, likesCount);
        this.comments = comments;
        this.commentsCount = commentsCount;
    }
}