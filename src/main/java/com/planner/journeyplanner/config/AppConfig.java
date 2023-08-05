package com.planner.journeyplanner.config;

import com.planner.journeyplanner.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;
/* @author Emre Kavak
*  Date :  10/06/2023
*  AppConfig.java
*  This class responsible for retrieving the API key from properties
* */
@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository repository;
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    //In order to authenticate the user
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    private Properties props;

    //This method will be called after spring loading completes
    /*@PostConstruct
    public void init() {
        Resource resource = new ClassPathResource("keys.env");
        System.out.println("init is called");
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            // Handle the exception
        }
    }*/
/*
    public String getProperty(String key) {
        return props.getProperty(key);
    }*/
}