package com.planner.journeyplanner.config;

import com.planner.journeyplanner.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll() // permit login for everyone
                .anyRequest().authenticated() // any other requests must be authenticated
                .and()
                .formLogin()
                .loginProcessingUrl("/login") // the URL on which the clients should post the login information
                .usernameParameter("username") // the username parameter in the queryString, default is 'username'
                .passwordParameter("password") // the password parameter in the queryString, default is 'password'
                .and()
                .logout()
                .logoutUrl("/logout") // the URL on which the clients should post if they want to logout
                .logoutSuccessUrl("/login"); // the URL to which the user will be redirected after they logout
    }
}