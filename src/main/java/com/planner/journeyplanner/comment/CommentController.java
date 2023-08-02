package com.planner.journeyplanner.comment;

import com.planner.journeyplanner.exception.ResourceNotFoundException;
import com.planner.journeyplanner.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jp/comment")
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest request) {
        try {
            CommentDTO comment = commentService.createComment(request.getJourneyId(), request.getContent());
            return ResponseEntity.ok(comment);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) throws UnauthorizedAccessException, ResourceNotFoundException {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        try {
            CommentDTO comment = commentService.updateComment(id, request.getContent());
            return ResponseEntity.status(HttpStatus.OK).body(comment);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (UnauthorizedAccessException ue){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ue.getMessage());
        }
    }

    //Only for Testing, this endpoint will not be used in the client side.
    @GetMapping("/getAllComments")
    public ResponseEntity<List<Comment>> getComments() {
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }
}
