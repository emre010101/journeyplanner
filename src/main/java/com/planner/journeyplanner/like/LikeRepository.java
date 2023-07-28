package com.planner.journeyplanner.like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Long countByJourneyId(Long journeyId);
}
