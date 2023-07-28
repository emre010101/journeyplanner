package com.planner.journeyplanner.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByJourneyId(Long journeyId);

    void delete(Comment comment);


}
