package com.planner.journeyplanner.journey;

import com.planner.journeyplanner.comment.Comment;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class JourneyDTO {

    private Journey journey;
    private Long likesCount;
    private List<Comment> comments;


}
