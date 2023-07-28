package com.planner.journeyplanner.like;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jp/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{journeyId}")
    public ResponseEntity<Void> createLike(@PathVariable Long journeyId){
        likeService.createLike(journeyId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{journeyId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long journeyId){
        likeService.deleteLike(journeyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
