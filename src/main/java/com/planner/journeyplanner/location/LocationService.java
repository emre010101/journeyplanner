package com.planner.journeyplanner.location;

import com.planner.journeyplanner.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;


    public Location updateLocation(Location location, Boolean increase) {
        if(increase){
            location.setCount(location.getCount() + 1);
        }else{
            location.setCount((location.getCount() -1));
        }
        return locationRepository.save(location);
    }

    public Location createLocation(String name, String geocodedAddress) {
        Optional<Location> existingLocationOpt = locationRepository.findByNameIgnoreCase(name);

        if (existingLocationOpt.isPresent()) {
            return updateLocation(existingLocationOpt.get(), true);
        } else {
            Location location = Location
                    .builder()
                            .name(name)
                                    .geocodedAddress(geocodedAddress)
                                            .build();
            return locationRepository.save(location);
        }
    }



    public Optional<Location> findByName(String locationName)  {
        return (Optional<Location> ) locationRepository.findByNameIgnoreCase(locationName);
    }

    public void deleteLocation(Location location) {
        try {
            if(location.getCount() <= 1){
                locationRepository.delete(location);
            }else{
                updateLocation(location, false);
            }
        }catch (NullPointerException e){
            throw new NullPointerException("Locations are not coded for this journey");
        }
    }


}