package com.planner.journeyplanner.journey;

import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.comment.Comment;
import com.planner.journeyplanner.comment.CommentDTO;
import com.planner.journeyplanner.comment.CommentService;
import com.planner.journeyplanner.exception.ResourceNotFoundException;
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



    public Page<JourneyDTO> getJourneys(Pageable pageable, String origin, String destination) {
        Page<Journey> journeys = journeyRepository.findAll(pageable);
        Long userId = authenticationService.getAuthenticatedUser().getId();

        List<JourneyDTO> dtos = journeys.stream()
                .map(journey -> {
                    // Get likes for the journey
                    List<Like> likes = likeService.getLikesByJourney(journey.getId());
                    // Convert to LikeDTO
                    List<LikeDTO> likeDTOs = likes.stream()
                            .map(like -> new LikeDTO(like, userId))
                            .collect(Collectors.toList());
                    // Get comments for the journey
                    List<Comment> comments = commentService.getCommentsByJourneyId(journey.getId());
                    // Convert to CommentDTO
                    List<CommentDTO> commentDTOs = comments.stream()
                            .map(comment -> new CommentDTO(comment, userId))
                            .collect(Collectors.toList());
                    // Get count of likes and comments
                    Long likesCount = (long)likes.size();
                    Long commentsCount = (long)comments.size();

                    return new JourneyDTO(journey, likeDTOs, likesCount, commentDTOs, commentsCount, userId);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    // add additional methods
}