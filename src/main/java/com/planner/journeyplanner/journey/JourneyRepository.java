package com.planner.journeyplanner.journey;

import com.planner.journeyplanner.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import com.planner.journeyplanner.user.User;

import java.util.List;


public interface JourneyRepository extends JpaRepository<Journey, Integer> {

    List<Journey> findByUser(User user);

    List<Journey> findByOriginAndDestination(Location origin, Location destination);


    // additional query methods go here
}