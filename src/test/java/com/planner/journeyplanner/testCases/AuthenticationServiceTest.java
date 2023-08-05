package com.planner.journeyplanner.testCases;
import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.user.Role;
import com.planner.journeyplanner.user.User;
import com.planner.journeyplanner.user.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/*
 * author: Emre Kavak
 * date: 05/08/2023#
 * AuthenticationServiceTest.class
 * This class designed to test if the application can get the logged-in user details from the security context holder
 * which play crucial role in the system design.
 * */
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    void getAuthenticatedUser_userFound_success() {
        // Prepare user details
        String username = "john@example.com";
        User user = new User();
        user.setEmail(username);
        UserDetails userDetails = User.builder().email(username).password("password").role(Role.USER).build();

        // Set up the authentication context
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock the repository behavior
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        // Call the method
        User authenticatedUser = authenticationService.getAuthenticatedUser();

        // Verify the result
        assertNotNull(authenticatedUser);
        assertEquals(username, authenticatedUser.getEmail());

        // Clear the authentication context for other tests
        SecurityContextHolder.clearContext();
    }

    @Test
    void getAuthenticatedUser_userNotFound_throwsException() {
        // Prepare user details
        String username = "john@example.com";
        UserDetails userDetails = User.builder().email(username).password("password").role(Role.USER).build();

        // Set up the authentication context
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock the repository behavior
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Expect an exception
        assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.getAuthenticatedUser();
        });

        // Clear the authentication context for other tests
        SecurityContextHolder.clearContext();
    }



}
