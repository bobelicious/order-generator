package com.augusto.usersystemapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.augusto.usersystemapi.exceptions.UserException;
import com.augusto.usersystemapi.security.JwtTokenFilter;
import com.augusto.usersystemapi.security.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        try {
            return configuration.getAuthenticationManager();
        } catch (Exception e) {
            throw new UserException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
        httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sem
                                                                                                              // sessões
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(
                                "/api/v1/users/new",
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated() // Todas as rotas exigem autenticação
                )
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
