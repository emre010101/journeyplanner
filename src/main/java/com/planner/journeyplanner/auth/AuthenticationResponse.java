package com.planner.journeyplanner.auth;

/*
* AuthenticationResponse.class
* @date: 17/07/2023
* @author: Emre Kavak
* This class will hold the token created for user session
* */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
}
