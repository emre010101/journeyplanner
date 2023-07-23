package com.planner.journeyplanner.journey;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.planner.journeyplanner.user.User;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "journey")
public class Journey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String journeyTitle;

    @Column(length = 2048)
    private String staticMapUrl;

    private String urlToGoGMap;

    @Column(columnDefinition = "json")
    private JourneyDetails journeyDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // additional fields for future use
    private String description;

    @Column(length = 2048)
    private String photoUrl;
}
