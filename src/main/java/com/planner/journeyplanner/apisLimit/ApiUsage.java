package com.planner.journeyplanner.apisLimit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.planner.journeyplanner.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data //This annotation will create getter and setters
@Builder //To build the object using design patter builder
@NoArgsConstructor //Will create constructor without arguments
@AllArgsConstructor //all the arguments constructor
@Entity
@Table(name = "api_usage")
public class ApiUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "gpt_api_counter")
    private int gptApiCount;

    @Column(name = "map_api_counter")
    private int mapApiCount;

    @Column(name = "run_out")
    private Boolean runOut;

    @JsonIgnore
    @Column(name = "usage_date")
    @Temporal(TemporalType.DATE)
    private LocalDate usageDate;
}



