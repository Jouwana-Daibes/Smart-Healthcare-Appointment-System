package com.smarthealthcare.appointment.smarthealthcare_appointment.controller;


import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing doctor-related endpoints.
 */
//TODO: ADD UNIT TESTING
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Create (insert) / replace Doctor
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Doctor addDoctor(@RequestBody Doctor newDoctor) {
        return doctorService.addDoctor(newDoctor);
    }

    // Read (get) all doctors - accessible by all Roles
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'DOCTOR')")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    // READ by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PATIENT','DOCTOR')")
    public Doctor getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }


    // Whole update (replace) - but not changing id
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Doctor replaceDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {
        return doctorService.updateDoctor(id, updatedDoctor);
    }

    // Partial update
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Doctor patchDoctor(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return doctorService.patchDoctor(id, updates);
    }

    // Delete doctor By id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDoctorById(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "Doctor with ID " + id + " is deleted successfully";
    }

}
