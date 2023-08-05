package com.planner.journeyplanner.apisLimit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jp/usage")
@RequiredArgsConstructor
public class ApiUsageController {

    private final ApiUsageService apiUsageService;

    @PostMapping("/increment/map")
    public ResponseEntity<ApiUsage> incrementCounter() {
        ApiUsage apiUsage = apiUsageService.incrementApiCount(Type.MAP);
        if (!apiUsage.getRunOut()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        } else {
            return ResponseEntity.ok().body(apiUsage);
        }
    }

    @GetMapping("/today")
    public ResponseEntity<ApiUsage> getTodayApiUsage() {
        ApiUsage apiUsage = apiUsageService.getTodayApiUsage();
        return ResponseEntity.ok(apiUsage);
    }

}
