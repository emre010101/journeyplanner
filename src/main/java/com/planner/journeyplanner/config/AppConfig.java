package com.planner.journeyplanner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;
/* @author Emre Kavak
*  Date :  10/06/
*  AppConfig.java
*  This class responsible for retrieving the API key from properties
* */
@Configuration
public class AppConfig {

    private Properties props;

    //This method will be called after spring loading completes
    @PostConstruct
    public void init() {
        Resource resource = new ClassPathResource("keys.env");
        System.out.println("init is called");
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            // Handle the exception
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }
}