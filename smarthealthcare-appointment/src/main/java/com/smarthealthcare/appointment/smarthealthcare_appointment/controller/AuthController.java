package com.smarthealthcare.appointment.smarthealthcare_appointment.controller;


import com.smarthealthcare.appointment.smarthealthcare_appointment.model.User;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Authentication controller.
 * Provides REST APIs for:
 * - User Registration (Signup)
 * - User Login (JWT generation)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register a new user
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.register(user);
    }


    // Login and return JWT token
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> userInfoBody){
        String username = userInfoBody.get("username");
        String password = userInfoBody.get("password");
        String token = authService.login(username, password);
        return Map.of("token", token);
    }
}
