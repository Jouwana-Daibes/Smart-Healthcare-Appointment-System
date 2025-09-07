package com.smarthealthcare.appointment.smarthealthcare_appointment.controller;

import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // admin registers new patients
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Patient registerPatient(@RequestBody Patient newPatient) {
        return patientService.registerPatient(newPatient);
    }


    // patents can update their own details
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public Optional<Patient> updatePatientInfo(@PathVariable Long id,  @RequestBody Map<String, Object> updates) {
        return patientService.updatePatientInfo(id, updates);
    }

    // get patient details
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    public Optional<Patient> getPatient(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }


}
