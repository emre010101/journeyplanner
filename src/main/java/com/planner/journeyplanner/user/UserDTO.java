package com.planner.journeyplanner.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* date: 30/07/2023
* UserDTO.class
* author: Emre Kavak
* This class will be used for data transferring to client
* */

@Data //This annotation will create getter and setters
@Builder //To build the object using design patter builder
@NoArgsConstructor //Will create constructor without arguments
@AllArgsConstructor //all the arguments constructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstname();
        this.lastName = user.getLastname();
    }
}
