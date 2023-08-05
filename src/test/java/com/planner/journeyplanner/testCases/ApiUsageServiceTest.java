package com.planner.journeyplanner.testCases;
import com.planner.journeyplanner.apisLimit.*;
import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.user.Role;
import com.planner.journeyplanner.user.User;
import java.time.LocalDate;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/*
* date: 05/08/2023
* author: Emre Kavak
* ApiUsageServiceTest.class
* This class deigned to test if api usages are updated correctly
* */

@ExtendWith(MockitoExtension.class)
class ApiUsageServiceTest {

    @Mock
    private ApiUsageRepository apiUsageRepository;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private ApiUsageService apiUsageService;


    @Test
    void incrementApiCount_newUser_GPT() {
        // Prepare a new user with a role
        User user = new User();
        user.setRole(Role.USER);

        // Mocking the authentication service to return our user
        when(authenticationService.getAuthenticatedUser()).thenReturn(user);

        // Assuming no existing ApiUsage record
        when(apiUsageRepository.findByUserAndUsageDate(eq(user), any(LocalDate.class))).thenReturn(Optional.empty());

        // Call the method for the GPT type
        ApiUsageDTO result = apiUsageService.incrementApiCount(Type.GPT);

        // Verify the result
        assertEquals(1, result.getGptApiCount());
        assertEquals(0, result.getMapApiCount());
        assertEquals(false, result.getRunOutGpt());
        assertEquals(false, result.getRunOutMap());
    }

    @Test
    void incrementApiCount_runOutMap() {
        // Prepare a user with the USER role
        User user = new User();
        user.setRole(Role.USER);

        // Mocking the authentication service to return our user
        when(authenticationService.getAuthenticatedUser()).thenReturn(user);

        // Prepare an existing ApiUsage record with 50 map requests
        ApiUsage existingApiUsage = ApiUsage.builder()
                .user(user)
                .usageDate(LocalDate.now())
                .gptApiCount(0)
                .mapApiCount(50)
                .runOutGpt(false)
                .runOutMap(false)
                .build();

        // Mocking the repository to return our existing ApiUsage record
        when(apiUsageRepository.findByUserAndUsageDate(eq(user), any(LocalDate.class))).thenReturn(Optional.of(existingApiUsage));

        // Call the method for the map type
        ApiUsageDTO result = apiUsageService.incrementApiCount(Type.MAP);

        // Verify the result
        assertEquals(0, result.getGptApiCount());
        assertEquals(51, result.getMapApiCount()); // 51 because we incremented once
        assertEquals(false, result.getRunOutGpt());
        assertEquals(true, result.getRunOutMap()); // Run out should be true
    }

    @Test
    void resetApiUsage_onNewDay() {
        // Prepare a user with the USER role
        User user = new User();
        user.setRole(Role.USER);

        // Mocking the authentication service to return the user
        when(authenticationService.getAuthenticatedUser()).thenReturn(user);

        // Mocking the repository to return an empty Optional, simulating no existing record for today
        when(apiUsageRepository.findByUserAndUsageDate(eq(user), eq(LocalDate.now()))).thenReturn(Optional.empty());

        // Call the method for the new day
        ApiUsageDTO result = apiUsageService.getTodayApiUsage();

        // Verify the result - everything should be reset for the new day
        assertEquals(0, result.getGptApiCount());
        assertEquals(0, result.getMapApiCount());
        assertFalse(result.getRunOutGpt());
        assertFalse(result.getRunOutMap());

        // Additionally, checking the new ApiUsage record is saved with correct values
        ArgumentCaptor<ApiUsage> apiUsageCaptor = ArgumentCaptor.forClass(ApiUsage.class);
        verify(apiUsageRepository).save(apiUsageCaptor.capture());
        ApiUsage savedApiUsage = apiUsageCaptor.getValue();
        assertEquals(0, savedApiUsage.getGptApiCount());
        assertEquals(0, savedApiUsage.getMapApiCount());
        assertFalse(savedApiUsage.getRunOutGpt());
        assertFalse(savedApiUsage.getRunOutMap());
    }



}
