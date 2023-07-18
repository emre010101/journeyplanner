package com.planner.journeyplanner.auth;

import com.planner.journeyplanner.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* RegisterRequest.class
* @author: Emre Kavak
* @date: 17/07/2023
* This class will hold user details for registration
* */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
}
