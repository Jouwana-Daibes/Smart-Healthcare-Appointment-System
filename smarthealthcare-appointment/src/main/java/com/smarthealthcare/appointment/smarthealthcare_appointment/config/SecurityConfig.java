package com.smarthealthcare.appointment.smarthealthcare_appointment.config;

import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the application that defines security rules (who can access what).
 * - Password encoder (BCrypt)
 * - Authentication manager
 * - Security filter chain for request authorization
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Hashes passwords (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    // Spring Security object that handles  authentication (check credentials)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }


    // Defines which endpoints are protected
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable()) // CSRF is disabled because we’re using JWT in REST APIs, not browser sessions.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/register/doctor").hasRole("ADMIN")
                        .requestMatchers("/api/auth/register/patient").hasRole("ADMIN")
                        .requestMatchers("/api/auth/register/admin").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/doctors/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/patients/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/patients/**").hasAnyRole("ADMIN", "PATIENT")
                        .requestMatchers(HttpMethod.PATCH, "/api/patients/**").hasAnyRole("ADMIN", "PATIENT")
                        .requestMatchers("/api/patients/**").hasRole("ADMIN")
                        .requestMatchers("/api/doctors/**").hasRole("ADMIN")
                        .requestMatchers("/api/appointments/**").hasRole("PATIENT")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT filter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
