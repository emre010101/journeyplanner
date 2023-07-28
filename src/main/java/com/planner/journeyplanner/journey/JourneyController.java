package com.planner.journeyplanner.journey;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.Optional;

@RestController
@RequestMapping("/api/jp/journey")
@RequiredArgsConstructor
public class JourneyController {

    private final JourneyService journeyService;


    @PostMapping("/create")
    public ResponseEntity<Void> journey(
            @RequestBody Journey journey
    ) {
        System.out.println("Received Journey: " + journey.toString());

        Journey savedJourney = journeyService.save(journey);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/journeys")
    public ResponseEntity<Page<JourneyDTO>> getJourneys(
            @RequestParam(required = false) String userEmail,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        System.out.println("Journey are asked from the server");


        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<JourneyDTO> journeyPage = journeyService.getJourneys(Optional.ofNullable(userEmail),
                Optional.ofNullable(sortBy),
                Optional.ofNullable(direction),
                Optional.ofNullable(origin),
                Optional.ofNullable(destination),
                pageable);

        return ResponseEntity.ok(journeyPage);
    }

    @GetMapping("/journeys-basic")
    public ResponseEntity<Page<Journey>> getBasicJourneys(
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Journey> journeyPage = journeyService.getBasicJourneys(pageable);

        return ResponseEntity.ok(journeyPage);
    }


}