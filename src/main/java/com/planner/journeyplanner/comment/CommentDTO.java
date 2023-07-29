package com.planner.journeyplanner.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;
    private String content;
    private String username;
    private String surname;
    private boolean isUserComment;

}
