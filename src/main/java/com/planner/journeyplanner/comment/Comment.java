package com.planner.journeyplanner.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.planner.journeyplanner.journey.Journey;
import com.planner.journeyplanner.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value="user-comment")
    private User user;

    @ManyToOne
    @JoinColumn(name = "journey_id", nullable = false)
    @JsonBackReference(value="journey-comment")
    private Journey journey;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "updated_at")
    private LocalDateTime dateUpdated;

    @PrePersist
    protected void onCreate() {
        dateCreated = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        dateUpdated = LocalDateTime.now();
    }
}
