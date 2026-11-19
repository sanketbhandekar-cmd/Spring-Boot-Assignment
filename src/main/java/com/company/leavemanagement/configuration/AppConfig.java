package com.company.leavemanagement.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class AppConfig {
    // Development-specific configurations
}
