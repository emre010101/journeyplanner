package com.planner.journeyplanner.service;

import com.planner.journeyplanner.model.User;

public interface UserService {

    User findByUsername(String username);
}
