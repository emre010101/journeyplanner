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
import java.util.Objects;
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

    //public Long getCommentsCountByJourneyId(Long journeyId){return commentRepository.}

    public CommentDTO createComment(Long journeyId, String content) throws ResourceNotFoundException {
        User user = authenticationService.getAuthenticatedUser();
        Journey journey = journeyRepository.findById(journeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Journey with id " + journeyId + " not found"));

        Comment comment = Comment
                .builder()
                        .content(content)
                                .journey(journey)
                                        .user(user)
                                                .build();
        commentRepository.save(comment);
        CommentDTO commentDTO = new CommentDTO(comment, user.getId());
        return commentDTO;
    }

    public void deleteComment(Long id) throws ResourceNotFoundException, UnauthorizedAccessException {
        User user = authenticationService.getAuthenticatedUser();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));

        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new UnauthorizedAccessException("User is not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }


    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }


    public CommentDTO updateComment(Long id, String content) throws ResourceNotFoundException, UnauthorizedAccessException {
        User user = authenticationService.getAuthenticatedUser();
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent()) {
            throw new ResourceNotFoundException("The comment is now exist!");
        }
        Comment existingComment = comment.get();
        //Check if the comment is belonged to user
        if(existingComment.getUser()!=user){
            throw new UnauthorizedAccessException("The comment is not belong to user");
        }

        existingComment.setContent(content);
        commentRepository.save(existingComment);
        CommentDTO commentDTO = new CommentDTO(existingComment, user.getId());

        return commentDTO;
    }

    public void deleteByJourney(Journey journey) {
        commentRepository.deleteByJourneyId(journey.getId());
    }
}
