package com.smarthealthcare.appointment.smarthealthcare_appointment.service;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs.MedicalRecordRequestDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.MedicalRecord;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public MedicalRecordService(PatientRepository patientRepository, UserRepository userRepository, DoctorRepository doctorRepository, MedicalRecordRepository medicalRecordRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<MedicalRecord> getRecordsByPatientId(Long patientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new UserNotFoundException("Patient with ID " + patientId + " not found!"));

        if (patient.getId() != patientId)
            throw new IllegalArgumentException("Patient#" + patient.getId() + " can view its own records only");

        return medicalRecordRepository.findByPatientId(patientId);
    }


    public MedicalRecord addRecord(MedicalRecordRequestDTO request, Long doctorId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Doctor with ID " + doctorId + " not found!"));

        if (doctor.getId() != doctorId)
            throw new IllegalArgumentException("Doctor#" + doctor.getId() + " can create records for its patients only");
        System.out.println("url doctor id = " + doctorId + " doctor id " + doctor.getId());
        MedicalRecord record = new MedicalRecord();
        record.setPatientId(request.getPatientId());
        record.setPatientName(request.getPatientName());
        record.setPrescriptions(request.getPrescriptions());
        record.setLabReports(request.getLabReports());
        record.setNotes(request.getNotes());
        record.setRecordDate(LocalDateTime.now()); // automatically set date
        record.setPatientId(request.getPatientId());
        record.setPatientName(request.getPatientName());

        return medicalRecordRepository.save(record);
    }
}
