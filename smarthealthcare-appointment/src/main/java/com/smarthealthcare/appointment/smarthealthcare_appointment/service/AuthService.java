package com.smarthealthcare.appointment.smarthealthcare_appointment.service;


import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.DoctorDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.PatientDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.UserDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Role;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.User;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.UserRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.EntityMapper;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles authentication and registration logic for the application.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // Registration logic
    @Transactional
    public PatientDTO registerPatient(User user, Patient patient) {
        if (user.getPassword() == null || user.getPassword().isEmpty())
            throw new IllegalArgumentException("Password cannot be null or empty");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.PATIENT));

        patient.setUser(user);
        user.setPatient(patient);

        userRepository.save(user); // cascade saves patient
        return EntityMapper.toPatientDTO(patient);
    }

    @Transactional
    public DoctorDTO registerDoctor(User user, Doctor doctor) {
        if (user.getPassword() == null || user.getPassword().isEmpty())
            throw new IllegalArgumentException("Password cannot be null or empty");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.DOCTOR));

        doctor.setUser(user);
        user.setDoctor(doctor);

        userRepository.save(user); // CASCADE SAVES DOCTOR
        return EntityMapper.toDoctorDTO(doctor);
    }

    @Transactional
    public UserDTO registerAdmin(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty())
            throw new IllegalArgumentException("Password cannot be null or empty");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ADMIN));

        userRepository.save(user);
        return EntityMapper.toUserDTO(user);
    }

    // Login logic and return JWT token
    // TODO: Let it return user not found response instead of authentication 403 forbidden exception
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