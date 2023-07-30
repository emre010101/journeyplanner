package com.planner.journeyplanner.comment;

import com.planner.journeyplanner.user.UserDTO;
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
    private UserDTO userDTO;
    private boolean isUserComment;

    public CommentDTO(Comment comment, Long userId) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.userDTO = new UserDTO(comment.getUser());
        this.isUserComment = comment.getUser().getId().equals(userId);
    }


}
