package com.planner.journeyplanner.journey;

import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.comment.Comment;
import com.planner.journeyplanner.comment.CommentService;
import com.planner.journeyplanner.exception.ResourceNotFoundException;
import com.planner.journeyplanner.like.LikeService;
import com.planner.journeyplanner.location.Location;
import com.planner.journeyplanner.location.LocationService;
import com.planner.journeyplanner.user.User;
import com.planner.journeyplanner.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final LocationService locationService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final AuthenticationService authenticationService;


    public Journey save(Journey journey) {
        User authenticatedUser = authenticationService.getAuthenticatedUser();
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

    public Optional<Journey> findById(Long id) {
        return journeyRepository.findById(id);
    }

   /* public List<Journey> findByUserEmail(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return journeyRepository.findByUser(user);
    }*/

    public List<Journey> findByOriginAndDestination(String originName, String destinationName) throws ResourceNotFoundException {
        Location origin = locationService.findByName(originName);
        Location destination = locationService.findByName(destinationName);
        return journeyRepository.findByOriginAndDestination(origin, destination);
    }

    public Page<JourneyDTO> getJourneys(Optional<String> userEmail,
                                        Optional<String> sortBy,
                                        Optional<String> direction,
                                        Optional<String> origin,
                                        Optional<String> destination,
                                        Pageable pageable) {

        Page<Journey> journeys;

        if(userEmail.isPresent()){
            journeys = journeyRepository.findByUserEmail(userEmail.get(), pageable);
        } else if(origin.isPresent() && destination.isPresent()){
            journeys = journeyRepository.findByOriginAndDestination(origin.get(), destination.get(), pageable);
        } else {
            journeys = journeyRepository.findAll(pageable);
        }

        return journeys.map(this::convertToDto);
    }

    private JourneyDTO convertToDto(Journey journey) {
        Long likesCount = likeService.getLikesCountForJourney(journey.getId());
        List<Comment> comments = commentService.getCommentsByJourneyId(journey.getId());

        return new JourneyDTO(journey, likesCount, comments);
    }

    // add additional methods
}