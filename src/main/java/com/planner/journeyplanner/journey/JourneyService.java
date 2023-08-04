package com.planner.journeyplanner.journey;

import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.comment.Comment;
import com.planner.journeyplanner.comment.CommentDTO;
import com.planner.journeyplanner.comment.CommentService;
import com.planner.journeyplanner.exception.ResourceNotFoundException;
import com.planner.journeyplanner.exception.UnauthorizedAccessException;
import com.planner.journeyplanner.like.Like;
import com.planner.journeyplanner.like.LikeDTO;
import com.planner.journeyplanner.like.LikeService;
import com.planner.journeyplanner.location.Location;
import com.planner.journeyplanner.location.LocationService;
import com.planner.journeyplanner.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
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


    public Page<JourneyDTO> getJourneys(Pageable pageable, String origin, String destination, Boolean onlyUserJourneys)  {
        Long userId = authenticationService.getAuthenticatedUser().getId();
        //Create scope journey page
        Page<Journey> journeys;

        //Handle the get locations ids
        Long originId = null;
        if (origin != null && !origin.trim().isEmpty()) {
            System.out.println("Received origin: " + origin);
            Optional<Location> originLocation = locationService.findByName(origin);
            if (originLocation.isPresent()) {
                originId = originLocation.get().getId();
            }
        }
        Long destinationId = null;
        if (destination != null && !destination.trim().isEmpty()) {
            System.out.println("REceived destinarion: " + destination);
            Optional<Location> destinationLocation = locationService.findByName(destination);
            if (destinationLocation.isPresent()) {
                destinationId = destinationLocation.get().getId();
            }
        }
        //Check what user want to filter
        if (originId != null && destinationId != null) {
            journeys = onlyUserJourneys ? journeyRepository.findByUserIdAndOriginIdAndDestinationId(userId, originId, destinationId, pageable)
                    : journeyRepository.findByOriginIdAndDestinationId(originId, destinationId, pageable);
        } else if (originId != null) {
            journeys = onlyUserJourneys ? journeyRepository.findByUserIdAndOriginId(userId, originId, pageable)
                    : journeyRepository.findByOriginId(originId, pageable);
        } else if (destinationId != null) {
            journeys = onlyUserJourneys ? journeyRepository.findByUserIdAndDestinationId(userId, destinationId, pageable)
                    : journeyRepository.findByDestinationId(destinationId, pageable);
        } else {
            journeys = onlyUserJourneys ? journeyRepository.findByUserId(userId, pageable)
                    : journeyRepository.findAll(pageable);
        }
        List<JourneyDTO> dtos = journeys.stream()
                .map(journey -> {
                    // Get likes for the journey
                    List<Like> likes = likeService.getLikesByJourney(journey.getId());
                    //Check if User Like it Before
                    Boolean isUserLike = likeService.getLikeByUserJourney(userId, journey.getId());

                    // Get comments for the journey
                    List<Comment> comments = commentService.getCommentsByJourneyId(journey.getId());
                    // Convert to CommentDTO
                    List<CommentDTO> commentDTOs = comments.stream()
                            .map(comment -> new CommentDTO(comment, userId))
                            .collect(Collectors.toList());
                    // Get count of likes and comments
                    Long likesCount = (long)likes.size();
                    Long commentsCount = (long)comments.size();

                    return new JourneyDTO(journey, isUserLike, likesCount, commentDTOs, commentsCount, userId);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    public void deleteJourney(Long id) throws ResourceNotFoundException, UnauthorizedAccessException {
        User user = authenticationService.getAuthenticatedUser();
        Journey journey = journeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));

        if (!Objects.equals(journey.getUser().getId(), user.getId())) {
            throw new UnauthorizedAccessException("User is not authorized to delete this comment");
        }
        //Firstly, save the locations
        Location origin = journey.getOrigin();
        Location destination = journey.getDestination();
        //Secondly, set the origin and destination to null then save it in the database
        journey.setOrigin(null);
        journey.setDestination(null);
        journeyRepository.save(journey);
        //Delete the locations in the database
        locationService.deleteLocation(origin);
        locationService.deleteLocation(destination);
        //Delete comments and likes associated with it
        likeService.deleteByJourney(journey);
        commentService.deleteByJourney(journey);
        //Lastly delete the journey
        journeyRepository.delete(journey);
    }
}