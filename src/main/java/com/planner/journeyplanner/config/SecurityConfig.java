package com.planner.journeyplanner.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

/*
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .httpBasic()
        .disable()
        .authorizeRequests()
        .antMatchers("/login")
        .permitAll()
        .antMatchers("/customError")
        .permitAll()
        .antMatchers("/access-denied")
        .permitAll()
        .antMatchers("/secured")
        .hasRole("ADMIN")
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .failureHandler(authenticationFailureHandler())
        .successHandler(authenticationSuccessHandler())
        .and()
        .exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler())
        .and()
        .logout();
    return http.build();
}
}
*/