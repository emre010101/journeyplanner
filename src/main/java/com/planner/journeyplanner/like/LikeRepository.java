package com.planner.journeyplanner.like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Long countByJourneyId(Long journeyId);

    Optional<Like> findByUserIdAndJourneyId(Long userId, Long journeyId);

    void delete(Like like);

    List<Like> findAllByJourneyId(Long journeyId);
}
