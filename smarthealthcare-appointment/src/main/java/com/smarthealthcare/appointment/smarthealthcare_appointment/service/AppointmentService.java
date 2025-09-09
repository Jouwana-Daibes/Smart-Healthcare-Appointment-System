package com.smarthealthcare.appointment.smarthealthcare_appointment.service;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.DoctorDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.*;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.*;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private AuthenticationManager authenticationManger;
    private UserRepository userRepository;

    public AppointmentService() {
    }

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, AuthenticationManager authenticationManger, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.authenticationManger = authenticationManger;
        this.userRepository = userRepository;
    }


    public Appointment bookAppointment(Long doctorId, Long patientId,
                                       LocalDateTime requestedStart,
                                       LocalDateTime requestedEnd,
                                       String days) {

        // Validate that end time is after start time
        if (requestedEnd.isBefore(requestedStart) || requestedEnd.isEqual(requestedStart)) {
            throw new IllegalArgumentException("Appointment end time must be after start time");
        }

        // Fetch doctor
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor with ID " + doctorId + " not found"));

        // Check if requested day is in doctor's availableDays
        DayOfWeek dayOfWeek = requestedStart.getDayOfWeek(); // e.g., MONDAY
        if (!isDoctorAvailableOnDay(doctor.getAvailableDays(), dayOfWeek)) {
            throw new IllegalArgumentException("Oops! Doctor is not available on this day");
        }

        // Check if requested time is within startTime and endTime
        LocalDateTime startTime = doctor.getStartTime();
        LocalDateTime endTime = doctor.getEndTime();
        LocalDateTime appointmentStartTime = requestedStart;
        LocalDateTime appointmentEndTime = requestedEnd;

        if (appointmentStartTime.isBefore(startTime) || appointmentEndTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Oops! Doctor is not available at this time");
        }

        // Prevent overlapping appointments
        boolean occupied = appointmentRepository.existsByDoctorIdAndTimeOverlapAndDay(
                doctorId, requestedStart, requestedEnd, days
        );
        if (occupied) {
            throw new IllegalArgumentException("Oops! Time slot already taken");
        }

        // Fetch patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new UserNotFoundException("Patient with ID " + patientId + " not found"));

        // Save appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentStartTime(requestedStart);
        appointment.setAppointmentEndTime(requestedEnd);
        appointment.setAppointmentDay(days);
        appointment.setStatus(Status.OCCUPIED);

        return appointmentRepository.save(appointment);
    }

    // check if doctor works on the requested day
    private boolean isDoctorAvailableOnDay(String availableDays, DayOfWeek requestedDay) {
        if (availableDays == null || availableDays.isEmpty()) return false;

        // Handle single days or ranges like "Mon-Fri"
        if (availableDays.contains("-")) {
            String[] parts = availableDays.split("-");
            DayOfWeek start = DayOfWeekShortDay.fromString(parts[0].trim());
            DayOfWeek end = DayOfWeekShortDay.fromString(parts[1].trim());
            return requestedDay.getValue() >= start.getValue() && requestedDay.getValue() <= end.getValue();
        } else {
            DayOfWeek day = DayOfWeekShortDay.fromString(availableDays.trim());
            return requestedDay.equals(day);
        }
    }



    public void cancelAppointment(Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Cancel appointment: authentication = " + authentication);

        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Patient with userID " + userId + " not found!"));

        Long patientId = patient.getId();

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new UserNotFoundException("Appointment#" + appointmentId + " not found"));

        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new IllegalArgumentException("You can only cancel your own appointment!");
        }

        appointmentRepository.delete(appointment);
    }

    // Read all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Doctor views all their appointments
    public List<Appointment> getAppointmentsForDoctor(Long doctorId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Doctor with userID " + userId + " not found!"));

        if (doctor.getId() != doctorId)
            throw new IllegalArgumentException("Doctor#" + doctor.getId() + " can view only its own appointments");

        return appointmentRepository.findByDoctorId(doctor.getId());
    }

    // Patient views all their appointments
    public List<Appointment> getAppointmentsForPatient(Long patientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Cancel appointment: authentication = " + authentication);

        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Patient with userID " + userId + " not found!"));

        if (patient.getId() != patientId)
            throw new IllegalArgumentException("Patient#" + patient.getId() + " can view only its own appointments");

        return appointmentRepository.findByPatientId(patient.getId());
    }

    public List<Appointment> getTodaysAppointmentsForDoctor(Long doctorId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Doctor with userID " + userId + " not found!"));

        if (doctor.getId() != doctorId)
            throw new IllegalArgumentException("Doctor#" + doctor.getId() + " can view only its own appointments");

        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return appointmentRepository.findByDoctorIdAndAppointmentStartTimeBetween(
                doctorId, startOfDay, endOfDay
        );
    }
}
