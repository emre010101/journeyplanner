package com.planner.journeyplanner.journey;

import com.planner.journeyplanner.exception.ResourceNotFoundException;
import com.planner.journeyplanner.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.Optional;

/*
* date
*
* */

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


    @GetMapping("/getJourneys")
    public ResponseEntity<Page<JourneyDTO>> getJourneysWithLikesAndComments(
            @RequestParam(defaultValue = "false") Boolean onlyUserJourneys,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<JourneyDTO> journeyPage = journeyService.getJourneys(pageable, origin, destination, onlyUserJourneys);

        return ResponseEntity.ok(journeyPage);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJourney(@PathVariable Long id) {
        try {
            journeyService.deleteJourney(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UnauthorizedAccessException ue){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}