package com.planner.journeyplanner.apisLimit;

import com.planner.journeyplanner.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;


public interface ApiUsageRepository extends JpaRepository<ApiUsage, Long> {
    Optional<ApiUsage> findByUserAndUsageDate(User user, LocalDate currentDate);
}
