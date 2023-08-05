package com.planner.journeyplanner.testCases;
import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.comment.Comment;
import com.planner.journeyplanner.comment.CommentDTO;
import com.planner.journeyplanner.comment.CommentRepository;
import com.planner.journeyplanner.comment.CommentService;
import com.planner.journeyplanner.exception.ResourceNotFoundException;
import com.planner.journeyplanner.exception.UnauthorizedAccessException;
import com.planner.journeyplanner.journey.Journey;
import com.planner.journeyplanner.journey.JourneyRepository;
import com.planner.journeyplanner.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JourneyRepository journeyRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService; // Replace with the actual name of the class containing createComment method

    @Test
    void createComment_validInput_commentCreated() throws ResourceNotFoundException {
        // Arrange
        Long journeyId = 1L;
        String content = "Test Comment";
        User user = new User();
        user.setId(1L);
        Journey journey = new Journey();
        journey.setId(journeyId);

        when(authenticationService.getAuthenticatedUser()).thenReturn(user);
        when(journeyRepository.findById(journeyId)).thenReturn(Optional.of(journey));

        // Act
        CommentDTO result = commentService.createComment(journeyId, content);

        // Assert
        assertEquals(content, result.getContent());
        assertEquals(user.getId(), result.getUserDTO().getId());
        assertEquals(journeyId, result.getJourneyId());

        // Verify the Comment object was saved with correct values
        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(commentCaptor.capture());
        Comment savedComment = commentCaptor.getValue();

        assertEquals(content, savedComment.getContent());
        assertEquals(journey, savedComment.getJourney());
        assertEquals(user, savedComment.getUser());
    }

    @Test
    void updateComment_validInput_commentUpdated() throws ResourceNotFoundException, UnauthorizedAccessException {
        // Arrange
        Long commentId = 1L;
        String content = "New content";
        User user = new User();
        user.setId(1L);
        Comment existingComment = new Comment();
        existingComment.setUser(user);

        Journey journey = new Journey(); // Assuming Journey is the required class
        journey.setId(1L); // Assuming Journey has an ID
        existingComment.setJourney(journey); // Set the journey to the comment

        when(authenticationService.getAuthenticatedUser()).thenReturn(user);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        // Act
        CommentDTO result = commentService.updateComment(commentId, content);

        // Assert
        assertEquals(content, result.getContent());
        assertEquals(user.getId(), result.getUserDTO().getId());
    }


    @Test
    void updateComment_commentNotFound_throwsResourceNotFoundException() {
        // Arrange
        Long commentId = 1L;
        String content = "New content";
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> commentService.updateComment(commentId, content));
    }

    @Test
    void updateComment_userNotAuthorized_throwsUnauthorizedAccessException() throws ResourceNotFoundException {
        // Arrange
        Long commentId = 1L;
        String content = "New content";
        User user = new User();
        user.setId(1L);
        User otherUser = new User();
        otherUser.setId(2L);
        Comment existingComment = new Comment();
        existingComment.setUser(otherUser);

        when(authenticationService.getAuthenticatedUser()).thenReturn(user);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        // Act & Assert
        assertThrows(UnauthorizedAccessException.class, () -> commentService.updateComment(commentId, content));
    }
}

