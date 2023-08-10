package com.planner.journeyplanner.like;

import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.journey.Journey;
import com.planner.journeyplanner.journey.JourneyRepository;
import com.planner.journeyplanner.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/*
* date: 22/07/2023
* LikeService.class
* author : Emre Kavak
* This class designed to handling like entity saving, deleting ...
* */
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


    public List<Like> getLikesByJourney(Long journeyId) {
        return likeRepository.findAllByJourneyId(journeyId);
    }

    //If the user doesn't have a like in this journey return false.
    public Boolean getLikeByUserJourney(Long userId, Long journeyId) {
        Optional<Like> like = likeRepository.findByUserIdAndJourneyId(userId, journeyId);
        if(like.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public void deleteByJourney(Journey journey) {
        System.out.println("Deleting the Likes associated with: " + journey.getJourneyTitle());
         likeRepository.deleteByJourneyId(journey.getId());
    }
}
