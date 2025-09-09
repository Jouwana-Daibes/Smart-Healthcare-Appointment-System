package com.smarthealthcare.appointment.smarthealthcare_appointment.controller;


import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs.RegistrationAdminRequest;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs.RegistrationDoctorRequest;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs.RegistrationPatientRequest;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.DoctorDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.PatientDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.UserDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.User;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthService authService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register/patient")
    public ResponseEntity<PatientDTO> registerPatient(@Valid @RequestBody RegistrationPatientRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setEmail(request.getEmail());

        PatientDTO savedPatient = authService.registerPatient(user, patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<DoctorDTO> registerDoctor(@Valid @RequestBody RegistrationDoctorRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setEmail(request.getEmail());
        doctor.setSpeciality(request.getSpeciality());
        doctor.setStartTime(request.getStartTime());
        doctor.setEndTime(request.getEndTime());
        doctor.setAvailableDays(request.getAvailableDays());

        DoctorDTO savedDoctor = authService.registerDoctor(user, doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<UserDTO> registerAdmin(@Valid @RequestBody RegistrationAdminRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        UserDTO savedAdmin = authService.registerAdmin(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
    }

    // Login and return JWT token
    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody Map<String, String> userInfoBody){
        String username = userInfoBody.get("username");
        String password = userInfoBody.get("password");
        String token = authService.login(username, password);
        return Map.of("token", token);
    }
}
