package com.planner.journeyplanner.location;

import com.planner.journeyplanner.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location createLocation(String name, String geocodedAddress) {
        Location location = new Location();
        location.setName(name);
        location.setGeocodedAddress(geocodedAddress);
        return locationRepository.save(location);
    }

    public Location findByName(String locationName) throws ResourceNotFoundException {
        return (Location) locationRepository.findByName(locationName)
                .orElseThrow(() -> new ResourceNotFoundException("Location with name " + locationName + " not found"));
    }

    // add other methods for reading, updating, and deleting locations as needed
}