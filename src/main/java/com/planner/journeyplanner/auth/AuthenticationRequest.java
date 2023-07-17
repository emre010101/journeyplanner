package com.planner.journeyplanner.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* AuthenticationRequest.clas
* @author: Emre Kavak
* @date: 17/07/2023
* This class will only hold the user details for login
* */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    private String email;
    String password;
}
