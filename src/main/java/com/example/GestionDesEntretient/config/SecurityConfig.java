package com.example.GestionDesEntretient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityConfigChain securityConfigChain;  // Inject the path configuration class

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Use the path configuration defined in SecurityConfigChain
        securityConfigChain.configureRequests(http);

        return http.build();  // Build and return the security filter chain
    }
}
