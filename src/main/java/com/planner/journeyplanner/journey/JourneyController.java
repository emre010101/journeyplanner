package com.planner.journeyplanner.journey;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/jp/journey")
@RequiredArgsConstructor
public class JourneyController {

    private final JourneyService journeyService;


    @PostMapping("/create")
    public ResponseEntity<Journey> journey(
            @RequestBody Journey journey
    ) {
        System.out.println("Received Journey: " + journey.toString());

        Journey savedJourney = journeyService.save(journey);
        return new ResponseEntity<>(savedJourney, HttpStatus.CREATED);
    }



}