package com.smarthealthcare.appointment.smarthealthcare_appointment.service;

import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserAlreadyExistsException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Admin registers new patient
    public Patient registerPatient(Patient patient) {
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Patient " + patient.getName() + "with email " + patient.getEmail() + "already exists!");
        }

        return patientRepository.save(patient);
    }

    // patients update their own details
    public Optional<Patient> updatePatientInfo(Long id, Map<String, Object> updates) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedPatientName = authentication.getName();
        System.out.println("authentication = " + authentication);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Patient with ID " + id + " is not found!"));


        // check if logg-in user matches the patient being updated
        if (!patient.getName().equals(loggedPatientName)){
            throw new SecurityException("You can only update your own profile");
        }

        updates.forEach((key , value) -> {
            switch (key) {
                case "email" -> patient.setEmail((String) value);
                case "name"  -> patient.setName((String) value);
            }
        });

        patientRepository.save(patient);
        return Optional.of(patient);
    }

    public Optional<Patient> getPatientById(Long id) {
        if (!patientRepository.findById(id).isPresent()){
            throw new UserNotFoundException("User with ID " + id + " is not found");
        }

        return patientRepository.findById(id);
    }




}
