package com.planner.journeyplanner.journey;

import com.planner.journeyplanner.exception.ResourceNotFoundException;
import com.planner.journeyplanner.location.Location;
import com.planner.journeyplanner.location.LocationService;
import com.planner.journeyplanner.user.User;
import com.planner.journeyplanner.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class JourneyService {

    private final JourneyRepository journeyRepository;
    private final UserRepository userRepository;
    private final LocationService locationService;


    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Fetch the user from the database using the email/username (assuming you have a findByEmail method in your UserRepository).
        Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());

        if (!userOptional.isPresent()) {
            // Throw an exception or handle this case as you see fit
            throw new UsernameNotFoundException("User not found with username: " + userDetails.getUsername());
        }

        return userOptional.get();
    }

    public Journey save(Journey journey) {
        User authenticatedUser = getAuthenticatedUser();
        journey.setUser(authenticatedUser);
        // Create Location entities for origin and destination
        // Use LocationService to create Location entities
        Location origin = locationService.createLocation(
                journey.getOrigin().getName(),
                journey.getOrigin().getGeocodedAddress()
        );
        Location destination = locationService.createLocation(
                journey.getDestination().getName(),
                journey.getDestination().getGeocodedAddress()
        );

        // Associate Locations with Journey
        journey.setOrigin(origin);
        journey.setDestination(destination);

        return journeyRepository.save(journey);
    }

    public List<Journey> findAll() {
        return journeyRepository.findAll();
    }

    public Optional<Journey> findById(Integer id) {
        return journeyRepository.findById(id);
    }

    public List<Journey> findByOriginAndDestination(String originName, String destinationName) throws ResourceNotFoundException {
        Location origin = locationService.findByName(originName);
        Location destination = locationService.findByName(destinationName);
        return journeyRepository.findByOriginAndDestination(origin, destination);
    }

    // add additional methods as needed
}