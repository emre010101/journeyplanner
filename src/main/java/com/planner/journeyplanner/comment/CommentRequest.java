package com.planner.journeyplanner.comment;

import lombok.Data;

@Data
public class CommentRequest {
    private Long journeyId;
    private String content;
}
