package com.planner.journeyplanner.service;

import com.planner.journeyplanner.model.User;

import java.util.List;

public interface UserService {
    User createUser(String username, String password, String email, String fullname);
    User findByUsername(String username);
    List<User> findAllUsers();
    User findUserById(int id);
    User updateUser(User user);
    void deleteUser(int id);
}
