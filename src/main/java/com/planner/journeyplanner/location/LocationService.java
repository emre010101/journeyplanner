package com.planner.journeyplanner.location;

import com.planner.journeyplanner.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;


    public Location updateLocation(Location location) {
        location.setCount(location.getCount() + 1);
        return locationRepository.save(location);
    }

    public Location createLocation(String name, String geocodedAddress) {
        Optional<Location> existingLocationOpt = locationRepository.findByName(name);

        if (existingLocationOpt.isPresent()) {
            return updateLocation(existingLocationOpt.get());
        } else {
            Location location = new Location();
            location.setName(name);
            location.setGeocodedAddress(geocodedAddress);
            return locationRepository.save(location);
        }
    }



    public Location findByName(String locationName) throws ResourceNotFoundException {
        return (Location) locationRepository.findByName(locationName)
                .orElseThrow(() -> new ResourceNotFoundException("Location with name " + locationName + " not found"));
    }

    // add other methods for reading, updating, and deleting locations as needed
}