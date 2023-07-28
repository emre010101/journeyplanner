package com.planner.journeyplanner.like;

import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.journey.Journey;
import com.planner.journeyplanner.journey.JourneyRepository;
import com.planner.journeyplanner.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final JourneyRepository journeyRepository;
    private final AuthenticationService authenticationService;

    public Long getLikesCountForJourney(Long journeyId){
        return likeRepository.countByJourneyId(journeyId);
    }

    public void createLike(Long journeyId){
        User user = authenticationService.getAuthenticatedUser();
        Journey journey = journeyRepository.findById(journeyId).orElseThrow();
        Optional<Like> existingLike = likeRepository.findByUserIdAndJourneyId(user.getId(), journeyId);
        if(existingLike.isEmpty()){
            Like like = new Like();
            like.setUser(user);
            like.setJourney(journey);
            likeRepository.save(like);
        }
    }

    public void deleteLike(Long journeyId) {
        User user = authenticationService.getAuthenticatedUser();
        Optional<Like> like = likeRepository.findByUserIdAndJourneyId(user.getId(), journeyId);
        like.ifPresent(likeRepository::delete); //it will get the like from optional and call to delete
    }


}
