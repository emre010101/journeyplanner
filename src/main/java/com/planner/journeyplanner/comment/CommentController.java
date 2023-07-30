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
            Comment comment = commentService.createComment(request.getJourneyId(), request.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(comment);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) throws UnauthorizedAccessException, ResourceNotFoundException {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        try {
            Comment comment = commentService.updateComment(id, request.getContent());
            return ResponseEntity.status(HttpStatus.OK).body(comment);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    //Only for Testing, this endpoint will not be used.
    @GetMapping("/getAllComments")
    public ResponseEntity<List<Comment>> getComments() {
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }
}
