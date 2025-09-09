package com.smarthealthcare.appointment.smarthealthcare_appointment.controller;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs.PrescriptionRequest;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Appointment;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Prescription;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    // Doctor adds prescription
    @PostMapping
    public ResponseEntity<Prescription> addPrescription(@Valid @RequestBody PrescriptionRequest request) {

        Prescription savedPrescription = prescriptionService.addPrescription(request);
        return ResponseEntity.ok(savedPrescription);
    }

    // Patient views prescriptions
    // Doctor views their appointments
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Prescription>> getDoctorPrescriptions(@PathVariable Long doctorId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsForDoctor(doctorId));
    }

    // Patient views their appointments
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Prescription>> getPatientPrescriptions(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsForPatient(patientId));
    }
    // Patient Delete prescriptions
    @DeleteMapping("/patient/{patientId}")
    public void deletePatientPrescriptions(@PathVariable Long patientId) {
         prescriptionService.deletePrescription(patientId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(
            @PathVariable("id") String prescriptionId,
            @RequestBody PrescriptionRequest updatedPrescription) {
        Prescription prescription = prescriptionService.updatePrescription(prescriptionId, updatedPrescription);
        return ResponseEntity.ok(prescription);
    }

    @PatchMapping("/{prescriptionId}")
    public ResponseEntity<Prescription> patchPrescription(
            @PathVariable String prescriptionId,
            @RequestBody PrescriptionRequest request
    ) {
        Prescription updatedPrescription = prescriptionService.patchPrescription(prescriptionId, request);
        return ResponseEntity.ok(updatedPrescription);
    }

}
