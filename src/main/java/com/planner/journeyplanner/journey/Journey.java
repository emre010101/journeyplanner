package com.planner.journeyplanner.journey;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.planner.journeyplanner.comment.Comment;
import com.planner.journeyplanner.location.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.planner.journeyplanner.user.User;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
/*
* date: 23/07/2023
* author: Emre Kavak
* Journey.class
* It's a model class used for Journey DTO
* */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "journey")
public class Journey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "journey_title")
    private String journeyTitle;

    @Column(name = "static_map_url", length = 2048)
    private String staticMapUrl;

    @Column(name = "url_to_gogmap")
    private String urlToGoGMap;

    @Column(name = "journey_details", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private JourneyDetails journeyDetails;

    @Column(name = "created_at")
    private LocalDateTime dateCreated;

    @Column(name = "updated_at")
    private LocalDateTime dateUpdated;

    // additional fields for future use
    @Column(name = "description")
    private String description;
    @Column(name = "photo_url", length = 2048)
    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value="user-journey")
    private User user;

    @OneToMany(mappedBy = "journey")
    @JsonManagedReference(value="journey-comment")
    private List<Comment> comments;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "origin_id", referencedColumnName = "id")
    private Location origin;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "destination_id", referencedColumnName = "id")
    private Location destination;

    @PrePersist
    protected void onCreate() { dateCreated = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() {
        dateUpdated = LocalDateTime.now();
    }
}
