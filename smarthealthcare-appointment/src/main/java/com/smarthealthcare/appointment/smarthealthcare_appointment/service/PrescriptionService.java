package com.smarthealthcare.appointment.smarthealthcare_appointment.service;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs.PrescriptionRequest;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Appointment;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Prescription;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {
    private PrescriptionRepository prescriptionRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private UserRepository userRepository;
    private AppointmentRepository appointmentRepository;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               DoctorRepository doctorRepository, PatientRepository patientRepository
    , UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Prescription addPrescription(PrescriptionRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("In Prescription service: authentication = " + authentication);

        String doctorUsername = authentication.getName();
        Long doctorUserId = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Doctor User not found"))
                .getId();

        Doctor doctor = doctorRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new UserNotFoundException("Doctor with user ID " + doctorUserId + " not found!"));

        // Verify that the patient has an appointment with this doctor
        boolean hasAppointment = appointmentRepository.existsByDoctorIdAndPatientId(doctor.getId(), request.patientId());

        if (!hasAppointment) {
            throw new IllegalArgumentException("Doctor#" + doctor.getId() + " cannot add prescription: no appointment exists with this patient#" + request.patientId());
        }

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new UserNotFoundException("Patient with user ID " + request.patientId() + " not found!"));


        Prescription prescription = new Prescription(
                doctor.getId(),
                doctor.getName(),
                patient.getId(),
                patient.getName(),
                request.medicines()
        );

        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPatientPrescriptions(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    public void deletePrescription(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("In Prescription service: authentication = " + authentication);

        String doctorUsername = authentication.getName();
        Long doctorUserId = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Doctor User not found"))
                .getId();

        Doctor doctor = doctorRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new UserNotFoundException("Doctor with user ID " + doctorUserId + " not found!"));


        // Verify that the patient has an appointment with this doctor
        boolean hasAppointment = appointmentRepository.existsByDoctorIdAndPatientId(doctor.getId(), id);

        if (!hasAppointment) {
            throw new IllegalArgumentException("Doctor#" + doctor.getId() + " cannot delete prescription: no appointment exists with this patient#" + id);
        }
        prescriptionRepository.deleteByPatientId(id);
    }


    // Full update (PUT)
    public Prescription updatePrescription(String prescriptionId, PrescriptionRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("In Prescription service: authentication = " + authentication);

        String doctorUsername = authentication.getName();
        Long doctorUserId = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Doctor User not found"))
                .getId();

        Doctor doctor = doctorRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new UserNotFoundException("Doctor with user ID " + doctorUserId + " not found!"));

        // Find prescription by ID
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found"));

        // Validate doctor-patient relationship
        boolean hasAppointment = appointmentRepository.existsByDoctorIdAndPatientId(
                doctor.getId(), request.patientId());
        if (!hasAppointment) {
            throw new IllegalArgumentException("Doctor#" + doctor.getId() + " cannot update prescription for unrelated patient#" + request.patientId());
        }

        // Update only prescription fields from the request
        prescription.setMedicines(request.medicines());


        return prescriptionRepository.save(prescription);
    }

    public Prescription patchPrescription(String prescriptionId, PrescriptionRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("In Prescription service: authentication = " + authentication);

        String doctorUsername = authentication.getName();
        Long doctorUserId = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Doctor User not found"))
                .getId();

        Doctor doctor = doctorRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new UserNotFoundException("Doctor with user ID " + doctorUserId + " not found!"));

        // Find prescription by ID
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found"));

        // Validate doctor-patient relationship
        boolean hasAppointment = appointmentRepository.existsByDoctorIdAndPatientId(
                doctor.getId(), request.patientId());
        if (!hasAppointment) {
            throw new IllegalArgumentException("Doctor#" + doctor.getId() + " cannot update prescription for unrelated patient#" + request.patientId());
        }

        if (request.medicines() != null && !request.medicines().isEmpty()) {
            prescription.setMedicines(request.medicines());
        }

        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPrescriptionsForDoctor(Long doctorId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Doctor with userID " + userId + " not found!"));

        if (doctor.getId() != doctorId)
            throw new IllegalArgumentException("Doctor#" + doctor.getId() + " can view only its own prescriptions");

        return prescriptionRepository.findByDoctorId(doctor.getId());
    }

    // Patient views all their appointments
    public List<Prescription> getPrescriptionsForPatient(Long patientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Cancel appointment: authentication = " + authentication);

        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();


        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Patient with userID " + userId + " not found!"));

        if (patient.getId() != patientId)
            throw new IllegalArgumentException("Patient#" + patient.getId() + " can view only its own prescriptions");

        return prescriptionRepository.findByPatientId(patient.getId());
    }


}
