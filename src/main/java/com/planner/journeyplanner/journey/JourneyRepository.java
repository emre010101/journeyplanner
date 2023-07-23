package com.planner.journeyplanner.journey;

import org.springframework.data.jpa.repository.JpaRepository;
import com.planner.journeyplanner.user.User;

import java.util.List;


public interface JourneyRepository extends JpaRepository<Journey, Integer> {

    List<Journey> findByUser(User user);

    // additional query methods go here
}