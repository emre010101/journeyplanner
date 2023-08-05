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

/*
* date: 04/08/2023
* author: Emre Kavak
* LocationServiceTest.class
* This class designed to test if the findByName works as it's expected.
* */

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    void findByName_found()  {
        String locationName = "Galway";
        Location expectedLocation = new Location();
        when(locationRepository.findByNameIgnoreCase(locationName)).thenReturn(Optional.of(expectedLocation));

        Optional<Location> actualLocation = locationService.findByName(locationName);

        assertTrue(actualLocation.isPresent());
        assertEquals(expectedLocation, actualLocation.get());
    }

    @Test
    void findByName_notFound() {
        String locationName = "NonexistentLocation";
        when(locationRepository.findByNameIgnoreCase(locationName)).thenReturn(Optional.empty());

        Optional<Location> actualLocation = locationService.findByName(locationName);

        assertFalse(actualLocation.isPresent());
    }
}
