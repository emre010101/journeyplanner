package com.planner.journeyplanner.journey;

import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.comment.Comment;
import com.planner.journeyplanner.comment.CommentService;
import com.planner.journeyplanner.exception.ResourceNotFoundException;
import com.planner.journeyplanner.like.LikeService;
import com.planner.journeyplanner.location.Location;
import com.planner.journeyplanner.location.LocationService;
import com.planner.journeyplanner.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    public List<Journey> findByOriginAndDestination(String originName, String destinationName) throws ResourceNotFoundException {
        Location origin = locationService.findByName(originName);
        Location destination = locationService.findByName(destinationName);
        return journeyRepository.findByOriginAndDestination(origin, destination);
    }

    public Page<JourneyDTODeprecated> getJourneys(Optional<String> userEmail,
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
        } else if(origin.isPresent()) {
            journeys = journeyRepository.findByOrigin(origin.get(), pageable);
        } else if(destination.isPresent()) {
            journeys = journeyRepository.findByDestination(destination.get(), pageable);
        } else {
            journeys = journeyRepository.findAll(pageable);
        }

        return journeys.map(this::convertToDto);
    }

    private JourneyDTODeprecated convertToDto(Journey journey) {
        Long likesCount = likeService.getLikesCountForJourney(journey.getId());
        List<Comment> comments = commentService.getCommentsByJourneyId(journey.getId());

        return new JourneyDTODeprecated(journey, likesCount, comments, (long) comments.size());
    }

    public Page<Journey> getBasicJourneys(Pageable pageable) {
        return journeyRepository.findAll(pageable);
    }

    public Page<BasicJourneyDTO> getJourneysWithLikesCount(Pageable pageable) {
        Page<Journey> journeys = journeyRepository.findAll(pageable);

        // Use a method to convert each Journey to a BasicJourneyDTO
        List<BasicJourneyDTO> dtos = journeys.stream()
                .map(journey -> {
                    Long likesCount = likeService.getLikesCountForJourney(journey.getId());
                    return new BasicJourneyDTO(journey, likesCount);
                })
                .collect(Collectors.toList());

        // Convert the List<BasicJourneyDTO> back to a Page<BasicJourneyDTO> for consistency
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    public Page<JourneyDTO> getJourneysWithLikesAndComments(Pageable pageable) {
        Page<Journey> journeys = journeyRepository.findAll(pageable);

        List<JourneyDTO> dtos = journeys.stream()
                .map(journey -> {
                    Long likesCount = likeService.getLikesCountForJourney(journey.getId());
                    List<Comment> comments = commentService.getCommentsByJourneyId(journey.getId());
                    Long commentsCount = (long) comments.size();
                    return new JourneyDTO(journey, likesCount, comments, commentsCount);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, dtos.size());
    }


    // add additional methods
}