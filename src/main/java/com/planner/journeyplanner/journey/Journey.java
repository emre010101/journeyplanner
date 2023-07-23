package com.planner.journeyplanner.journey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.planner.journeyplanner.user.User;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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
    private Integer id;

    @Column(name = "journey_title")
    private String journeyTitle;

    @Column(name = "static_map_url", length = 2048)
    private String staticMapUrl;

    @Column(name = "url_to_gogmap")
    private String urlToGoGMap;

    @Column(name = "journey_details", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private JourneyDetails journeyDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // additional fields for future use
    @Column(name = "description")
    private String description;

    @Column(name = "photo_url", length = 2048)
    private String photoUrl;

}