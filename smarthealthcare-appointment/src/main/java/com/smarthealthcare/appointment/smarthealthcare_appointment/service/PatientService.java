package com.smarthealthcare.appointment.smarthealthcare_appointment.service;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.DoctorDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.PatientDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserAlreadyExistsException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.*;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.AppointmentRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.PatientRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.PrescriptionRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.UserRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.EntityMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private PrescriptionRepository prescriptionRepository;
    private AppointmentRepository appointmentRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

//    // Admin registers new patient
//    public PatientDTO registerPatient(Patient patient) {
//        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
//            throw new UserAlreadyExistsException("Patient " + patient.getName() + "with email " + patient.getEmail() + "already exists!");
//        }
//
//        Patient newPatient = patientRepository.save(patient);
//        return EntityMapper.toPatientDTO(newPatient);
//    }

    // patients update their own details
    public PatientDTO updatePatientInfo(Long id, Patient patient) {
        Patient updatePatient = patientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Patient with ID " + id + " not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication);

        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isPatientUpdatingSelf = updatePatient.getUser().getId().equals(userId) &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"));

        if (!isAdmin && !isPatientUpdatingSelf) {
            throw new SecurityException("You can only update your own profile");
        }
        updatePatient.setName(patient.getName());
        updatePatient.setEmail(patient.getEmail());
        Patient newPatient = patientRepository.save(updatePatient);
        return  EntityMapper.toPatientDTO(newPatient);
    }

    // Read all doctors
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(EntityMapper::toPatientDTO)
                .collect(Collectors.toList());
    }

    // Read by id
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Patient with ID " + id + " not found!"));
        return EntityMapper.toPatientDTO(patient);
    }

    // Delete doctor
    //TODO: Test it in postman
    @Transactional
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new UserNotFoundException("Patient with id " + id + " is not found!");
        }
        patientRepository.deleteById(id);
        List<Prescription> prescriptions = prescriptionRepository.findByPatientId(id);
        for(Prescription prescription : prescriptions){
                prescriptionRepository.deleteByPatientId(id);
            }

        List<Appointment> appointments = appointmentRepository.findByDoctorId(id);
        for(Appointment appointment : appointments){
                prescriptionRepository.deleteByPatientId(id);
        }
    }


    public PatientDTO patchPatient(Long id, Map<String, Object> updates) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication);

        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Patient with ID " + id + " not found!"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isPatientUpdatingSelf = patient.getUser().getId().equals(userId) &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"));

        if (!isAdmin && !isPatientUpdatingSelf) {
            throw new SecurityException("You can only update your own profile");
        }
        User user = patient.getUser();
        updates.forEach((key , value) -> {
            switch (key) {
                case "email" -> patient.setEmail((String) value);
                case "name"  -> patient.setName((String) value);
                case "username" -> user.getUsername();
                case "password" -> user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        });

        Patient updatedPatient = patientRepository.save(patient);
        return EntityMapper.toPatientDTO(updatedPatient);
    }

    public Long getUserIdFromPatient(Long patientId) {
        return patientRepository.findUserIdByPatientId(patientId);
    }



}
