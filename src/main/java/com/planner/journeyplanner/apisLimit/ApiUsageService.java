package com.planner.journeyplanner.apisLimit;

import com.planner.journeyplanner.auth.AuthenticationService;
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

    public ApiUsage incrementApiCount(Type type) {
        User user = authenticationService.getAuthenticatedUser();
        LocalDate currentDate = LocalDate.now();
        Optional<ApiUsage> optionalApiUsage = apiUsageRepository.findByUserAndUsageDate(user, currentDate);
        ApiUsage apiUsage;
        if (!optionalApiUsage.isPresent()) {
            apiUsage = new ApiUsage();
            apiUsage.setUser(user);
            apiUsage.setUsageDate(currentDate);
            apiUsage.setGptApiCount(type == Type.GPT ? 1 : 0);
            apiUsage.setMapApiCount(type == Type.GPT ? 0 : 1);
            apiUsage.setRunOut(false);
        } else {
            apiUsage = optionalApiUsage.get();

            if (type == Type.GPT) {
                apiUsage.setGptApiCount(apiUsage.getGptApiCount() + 1);
            } else {
                apiUsage.setMapApiCount(apiUsage.getMapApiCount() + 1);
            }

            if ((apiUsage.getGptApiCount() >= 5 || apiUsage.getMapApiCount() >= 5) && !user.getRole().equals("ADMIN")) {
                apiUsage.setRunOut(true);
            }
        }
        return apiUsageRepository.save(apiUsage);
    }

    public ApiUsage getTodayApiUsage() {
        User user = authenticationService.getAuthenticatedUser();
        LocalDate currentDate = LocalDate.now();

        Optional<ApiUsage> optionalApiUsage = apiUsageRepository.findByUserAndUsageDate(user, currentDate);
        ApiUsage apiUsage;

        if (!optionalApiUsage.isPresent()) {
            apiUsage = new ApiUsage();
            apiUsage.setUser(user);
            apiUsage.setUsageDate(currentDate);
            apiUsage.setGptApiCount(0);
            apiUsage.setMapApiCount(0);
            apiUsage = apiUsageRepository.save(apiUsage);
        } else {
            apiUsage = optionalApiUsage.get();
        }

        return apiUsage;
    }




}
