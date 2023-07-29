package com.planner.journeyplanner.like;

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
    private Long userId;
    private Long journeyId;

}
