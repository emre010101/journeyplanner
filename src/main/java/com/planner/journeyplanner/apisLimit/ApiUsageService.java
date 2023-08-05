package com.planner.journeyplanner.apisLimit;

import com.planner.journeyplanner.auth.AuthenticationService;
import com.planner.journeyplanner.user.Role;
import com.planner.journeyplanner.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
/*
* date: 04/08/2023
* author: Emre Kavak
* ApiUsageService.class
* This class designed keep track of api usages
* */
@Service
@RequiredArgsConstructor
public class ApiUsageService {

    private final ApiUsageRepository apiUsageRepository;
    private final AuthenticationService authenticationService;

    public ApiUsageDTO incrementApiCount(Type type) {
        User user = authenticationService.getAuthenticatedUser();
        LocalDate currentDate = LocalDate.now();
        Optional<ApiUsage> optionalApiUsage = apiUsageRepository.findByUserAndUsageDate(user, currentDate);
        ApiUsage apiUsage;
        if (!optionalApiUsage.isPresent()) {
            apiUsage = ApiUsage
                    .builder()
                    .user(user)
                    .usageDate(currentDate)
                    .gptApiCount(type == Type.GPT ? 1 : 0)
                    .mapApiCount(type == Type.GPT ? 0 : 1)
                    .runOut(false).build();
        } else {
            apiUsage = optionalApiUsage.get();
            System.out.println("The role: " + apiUsage.getUser().getRole());

            if (type == Type.GPT) {
                apiUsage.setGptApiCount(apiUsage.getGptApiCount() + 1);
            } else {
                apiUsage.setMapApiCount(apiUsage.getMapApiCount() + 1);
            }

            // Check if the user role is "USER" and a specific count has been reached
            if (user.getRole() == Role.USER && (apiUsage.getGptApiCount() >= 5 || apiUsage.getMapApiCount() >= 5)) {
                apiUsage.setRunOut(true);
                apiUsage = apiUsageRepository.save(apiUsage); // Update the record with the new "runOut" value
            }
        }
        apiUsageRepository.save(apiUsage);
        return new ApiUsageDTO(apiUsage, user.getRole());
    }

    public ApiUsageDTO getTodayApiUsage() {
        User user = authenticationService.getAuthenticatedUser();
        LocalDate currentDate = LocalDate.now();

        Optional<ApiUsage> optionalApiUsage = apiUsageRepository.findByUserAndUsageDate(user, currentDate);
        ApiUsage apiUsage;

        if (!optionalApiUsage.isPresent()) {
            apiUsage = ApiUsage
                    .builder()
                    .user(user)
                    .usageDate(currentDate)
                    .gptApiCount(0)
                    .mapApiCount(0)
                    .runOut(false) // Initialize with false
                    .build();
            apiUsage = apiUsageRepository.save(apiUsage);
        } else {
            apiUsage = optionalApiUsage.get();
        }

        // Check if the user role is "USER" and a specific count has been reached
        if (user.getRole() == Role.USER && (apiUsage.getGptApiCount() >= 5 || apiUsage.getMapApiCount() >= 5)) {
            apiUsage.setRunOut(true);
            apiUsage = apiUsageRepository.save(apiUsage); // Update the record with the new "runOut" value
        }

        return new ApiUsageDTO(apiUsage, user.getRole());
    }


    public ApiUsageDTO toDTO(ApiUsage apiUsage){
        User user = authenticationService.getAuthenticatedUser();
        return new ApiUsageDTO(apiUsage, user.getRole());
    }



}
