package com.planner.journeyplanner.testCases;

import com.planner.journeyplanner.exception.ResourceNotFoundException;
import com.planner.journeyplanner.location.Location;
import com.planner.journeyplanner.location.LocationRepository;
import com.planner.journeyplanner.location.LocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    void findByName_found() throws ResourceNotFoundException {
        String locationName = "Galway";
        Location expectedLocation = new Location();
        when(locationRepository.findByName(locationName)).thenReturn(Optional.of(expectedLocation));

        Location actualLocation = locationService.findByName(locationName);


        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    void findByName_notFound() {
        String locationName = "NonexistentLocation";
        when(locationRepository.findByName(locationName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> locationService.findByName(locationName));
    }

    // Add similar tests for the other methods in LocationService
}
