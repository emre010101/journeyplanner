package com.planner.journeyplanner.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


/*
* User Repository extending to JpaRepository with arguments of the entity class and the id*/
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}