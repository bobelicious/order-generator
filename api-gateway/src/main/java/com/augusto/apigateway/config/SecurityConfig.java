package com.augusto.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
            .authorizeExchange(auth -> auth
                .pathMatchers("/api/v1/auth/**").permitAll() // Public access to /auth/**
                .pathMatchers("/actuator/health").permitAll() // Public access to health endpoint
                .pathMatchers("/config/**").hasIpAddress("127.0.0.1")// Public access to health endpoint
                .pathMatchers("/actuator/**").hasRole("ADMIN") // Restrict other Actuator endpoints
                .anyExchange().authenticated() // Require authentication for all other routes
            );
        return http.build();
    }
}