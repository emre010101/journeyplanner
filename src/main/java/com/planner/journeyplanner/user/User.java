package com.planner.journeyplanner.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.planner.journeyplanner.comment.Comment;
import com.planner.journeyplanner.journey.Journey;
import com.planner.journeyplanner.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data //This annotation will create getter and setters
@Builder //To build the object using design patter builder
@NoArgsConstructor //Will create constructor without arguments
@AllArgsConstructor //all the arguments constructor
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue //default value is auto
    private int id;

    //@Column(nullable = false, unique = true)
    private String email;

    //@Column(nullable = false)
    private String password;

    //@Column(name = "full_name", nullable = false)
    private String firstname;

    private String lastname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value="user-journey")
    private List<Journey> journeys;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value="user-comment")
    private List<Comment> comments;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name())); //authorising the user
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}