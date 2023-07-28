package com.planner.journeyplanner.journey;

import com.planner.journeyplanner.location.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.planner.journeyplanner.user.User;

import java.util.List;


public interface JourneyRepository extends JpaRepository<Journey, Long> {

    List<Journey> findByUser(User user);

    List<Journey> findByOriginAndDestination(Location origin, Location destination);

    Page<Journey> findByUserEmail(String email, Pageable pageable);

    Page<Journey> findByOriginAndDestination(String origin, String destination, Pageable pageable);


    // additional query methods go here
}