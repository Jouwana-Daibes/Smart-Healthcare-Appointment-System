package com.smarthealthcare.appointment.smarthealthcare_appointment.controller;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.DoctorDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.MedicalRecordResponseDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.PatientDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.MedicalRecord;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.MedicalRecordService;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.PatientService;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.EntityMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/patients")
public class PatientController {

    private final PatientService patientService;
    private final MedicalRecordService medicalRecordService;

    @Autowired
    public PatientController(PatientService patientService, MedicalRecordService medicalRecordService) {
        this.patientService = patientService;
        this.medicalRecordService = medicalRecordService;
    }

//    // admin registers new patients
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public Patient registerPatient(@RequestBody Patient newPatient) {
//        return patientService.registerPatient(newPatient);
//    }


    // patents can update their own details
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatientInfo(@PathVariable Long id, @RequestBody Patient updates) {
        return ResponseEntity.ok(patientService.updatePatientInfo(id, updates));
    }


    // Read (get) all patients - accessible by all Roles
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // READ by id
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    // Partial update
    @PatchMapping("/{id}")
    public ResponseEntity<PatientDTO> patchPatient(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(patientService.patchPatient(id, updates));
    }

    // Delete doctor By id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientById(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build(); //  Returns 204
    }

    @GetMapping("/MyRecords/{patientId}")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMyRecords(@PathVariable Long patientId) {
        List<MedicalRecordResponseDTO> recordsDTOs = new ArrayList<>();
        List<MedicalRecord> records = medicalRecordService.getRecordsByPatientId(patientId);
        for(MedicalRecord record : records){
            recordsDTOs.add(EntityMapper.toRecordDTO(record));
        }
        return ResponseEntity.ok(recordsDTOs);
    }




}
