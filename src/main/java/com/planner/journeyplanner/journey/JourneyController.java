package com.planner.journeyplanner.journey;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jp/journey")
@RequiredArgsConstructor
public class JourneyController {

    @PostMapping("/create")
    public ResponseEntity<String> journey(
            @RequestBody Journey journey
    ) {
        System.out.println("Journey Details = " + journey.toString());
        return ResponseEntity.ok("Hello data is received in the server");
    }


}