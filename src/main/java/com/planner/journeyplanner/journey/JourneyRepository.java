package com.planner.journeyplanner.journey;

import com.planner.journeyplanner.location.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface JourneyRepository extends JpaRepository<Journey, Long> {
    
    Page<Journey> findByUserId(Long userId, Pageable pageable);
    Page<Journey> findByOriginAndDestination(String origin, String destination, Pageable pageable);
    Optional<Journey> findById(Long journeyId);

    Page<Journey> findByUserIdAndOriginIdAndDestinationId(Long userId, Long originId, Long destinationId, Pageable pageable);

    Page<Journey> findByOriginIdAndDestinationId(Long originId, Long destinationId, Pageable pageable);

    Page<Journey> findByUserIdAndOriginId(Long userId, Long originId, Pageable pageable);

    Page<Journey> findByOriginId(Long originId, Pageable pageable);

    Page<Journey> findByUserIdAndDestinationId(Long userId, Long destinationId, Pageable pageable);

    Page<Journey> findByDestinationId(Long destinationId, Pageable pageable);
}