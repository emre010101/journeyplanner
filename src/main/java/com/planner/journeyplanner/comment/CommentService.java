package com.planner.journeyplanner.comment;

import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.exception.ResourceNotFoundException;
import com.planner.journeyplanner.exception.UnauthorizedAccessException;
import com.planner.journeyplanner.journey.Journey;
import com.planner.journeyplanner.journey.JourneyRepository;
import com.planner.journeyplanner.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final JourneyRepository journeyRepository;
    private final AuthenticationService authenticationService;

    public List<Comment> getCommentsByJourneyId(Long journeyId){
        return commentRepository.findByJourneyId(journeyId);
    }

    public Comment createComment(Long journeyId, String content) throws ResourceNotFoundException {
        User user = authenticationService.getAuthenticatedUser();
        Journey journey = journeyRepository.findById(journeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Journey with id " + journeyId + " not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setJourney(journey);
        comment.setUser(user);

        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) throws ResourceNotFoundException, UnauthorizedAccessException {
        User user = authenticationService.getAuthenticatedUser();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("User is not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }


}
