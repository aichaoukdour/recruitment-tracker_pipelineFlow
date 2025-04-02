package com.example.GestionDesEntretient.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@Component
public class SecurityConfigChain {

    public void configureRequests(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                .requestMatchers(HttpMethod.GET, "/api/v1/entretiens/*").hasRole("ADMIN")  
                .requestMatchers(HttpMethod.POST, "/api/v1/entretiens/{Id}/**").hasRole("ADMIN")  
                .requestMatchers(HttpMethod.PUT, "/api/v1/entretiens/{Id}/").hasRole("ADMIN")  
                .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .csrf(csrf -> csrf.disable());
    }

}
