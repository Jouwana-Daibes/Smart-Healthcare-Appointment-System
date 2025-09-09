package com.smarthealthcare.appointment.smarthealthcare_appointment.controller;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs.AppointmentRequest;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.AppointmentResponse;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Appointment;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.User;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.PatientRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.UserRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class PatientAppointmentController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Autowired
    public PatientAppointmentController(AppointmentService appointmentService, PatientRepository patientRepository, AuthenticationManager authenticationManager,UserRepository userRepository) {
        this.appointmentService = appointmentService;
        this.patientRepository = patientRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }


    // TODO: Implement Appointment + Prescription RESPONSE DTOs
    @PostMapping
    public ResponseEntity<AppointmentResponse> bookAppointment(@Valid @RequestBody AppointmentRequest doctorRequest) {
        // get logged-in patient id from DB
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("In controller: User " + username + "  not found"));

        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new UserNotFoundException("Patient not found for user id: " + user.getId()));

        Long patientId = patient.getId();
        System.out.println("In controlLer PATIENTiD = " + patientId);
       // Long patientId = patient.getId();
        Appointment appointment = appointmentService.bookAppointment(doctorRequest.doctorId(), patientId, doctorRequest.startTime(), doctorRequest.endTime(), doctorRequest.Days());

        AppointmentResponse response = new AppointmentResponse(
                appointment.getId(),
                appointment.getDoctor().getId(),
                appointment.getPatient().getId(),
                appointment.getAppointmentStartTime(),
                appointment.getAppointmentEndTime(),
                appointment.getAppointmentDay(),
                appointment.getStatus(),
                appointment.getDoctor().getName(),
                appointment.getPatient().getName()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Cancel appointment: authentication = " + authentication);

        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Patient with userID " + userId + " not found!"));

        Long patientId = patient.getId();
        return ResponseEntity.ok("Appointment#" + appointmentId + " cancelled successfully by patient#" + patientId);
    }

    // Doctor views their appointments
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsForDoctor(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    // Patient views their appointments
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsForPatient(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}/today")
    public ResponseEntity<List<Appointment>> getTodaysAppointmentsForDoctor(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getTodaysAppointmentsForDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

}
