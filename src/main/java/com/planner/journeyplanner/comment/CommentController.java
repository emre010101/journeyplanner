package com.planner.journeyplanner.comment;

import com.planner.journeyplanner.exception.ResourceNotFoundException;
import com.planner.journeyplanner.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jp/comment")
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequest request) throws ResourceNotFoundException {
        Comment comment = commentService.createComment(request.getJourneyId(), request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) throws UnauthorizedAccessException, ResourceNotFoundException {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
