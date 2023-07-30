package com.planner.journeyplanner.like;

import com.planner.journeyplanner.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {

    private Long id;
    private UserDTO userDTO;
    private Long journeyId;
    private boolean isUserLike;

    public LikeDTO(Like like, Long userId) {
        this.id = like.getId();
        this.userDTO = new UserDTO(like.getUser());
        this.isUserLike = like.getUser().getId().equals(userId);
    }

}
