package com.smarthealthcare.appointment.smarthealthcare_appointment.controller;


import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs.MedicalRecordRequestDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.DoctorDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.MedicalRecordResponseDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.MedicalRecord;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.DoctorService;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.MedicalRecordService;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing doctor-related endpoints.
 */
//TODO: ADD UNIT TESTING + DELETE + PUT DON'T WORK
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private MedicalRecordService medicalRecordService;

    @Autowired
    public DoctorController(DoctorService doctorService, MedicalRecordService medicalRecordService) {
        this.doctorService = doctorService;
        this.medicalRecordService = medicalRecordService;
    }

//    // Create (insert) / replace Doctor
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public Doctor addDoctor(@RequestBody Doctor newDoctor) {
//        return doctorService.addDoctor(newDoctor);
//    }

    // Read (get) all doctors - accessible by all Roles
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // READ by id
    @GetMapping(params = "id")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }


    // Whole update (replace) - but not changing id
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> replaceDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, updatedDoctor));
    }

    // Partial update
    @PatchMapping("/{id}")
    public ResponseEntity<DoctorDTO> patchDoctor(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(doctorService.patchDoctor(id, updates));
    }

    // Delete doctor By id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorById(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build(); //  Returns 204
    }

    // GET /api/doctors/search?specialty=cardiology
    @GetMapping(value = "/search", params = "speciality")
    public ResponseEntity<List<DoctorDTO>> getDoctorBySpeciality(@RequestParam String speciality) {
        List<DoctorDTO> doctors = doctorService.findDoctorsBySpeciality(speciality);
        return ResponseEntity.ok(doctors);
    }

    @PostMapping("/records/{doctorId}")
    public ResponseEntity<MedicalRecordResponseDTO> createRecord(
            @RequestBody MedicalRecordRequestDTO request,
            @PathVariable Long doctorId) {
        MedicalRecord record = medicalRecordService.addRecord(request, doctorId);
        return ResponseEntity.ok(EntityMapper.toRecordDTO(record));
    }



}
