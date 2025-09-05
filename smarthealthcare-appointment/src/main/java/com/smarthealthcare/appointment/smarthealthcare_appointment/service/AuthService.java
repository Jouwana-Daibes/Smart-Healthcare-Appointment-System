package com.smarthealthcare.appointment.smarthealthcare_appointment.service;


import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserAlreadyExistsException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.User;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.UserRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles authentication and registration logic for the application.
 */
@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // Registration logic
    public User register(User user) {
        Optional<User> existingUser = userRepo.findByUsername(user.getUsername());
        if (existingUser.isPresent()){
            throw new UserAlreadyExistsException("Username " + user.getUsername() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    // Login logic and return JWT token
    public String login(String username, String password) {

        // let spring security handles the authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // extract authenticated user details (username & password) to generate JWT token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // extract roles (authorities)
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toSet());

        // generate and return JWT token
        return jwtUtil.generateToken(userDetails.getUsername(), roles);
    }
}
