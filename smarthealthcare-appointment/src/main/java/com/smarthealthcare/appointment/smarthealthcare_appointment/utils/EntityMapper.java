package com.smarthealthcare.appointment.smarthealthcare_appointment.utils;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.*;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.*;

public class EntityMapper {
    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername()
        );
    }

    public static DoctorDTO toDoctorDTO(Doctor doctor) {
        return new DoctorDTO(
                doctor.getId(),
                doctor.getName(),
                doctor.getEmail(),
                doctor.getSpeciality(),
                doctor.getStartTime(),
                doctor.getEndTime(),
                doctor.getAvailableDays(),
                toUserDTO(doctor.getUser())
        );
    }

    public static AppointmentResponse toAppointment(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDoctor().getId(),
                appointment.getPatient().getId(),
                appointment.getAppointmentStartTime(),
                appointment.getAppointmentEndTime(),
                appointment.getAppointmentDay(),
                appointment.getStatus(),
                appointment.getDoctor().getName(),
                appointment.getPatient().getName()
        );
    }

    public static PrescriptionResponse toPrescription(Prescription prescription) {
        return new PrescriptionResponse(
                prescription.getId(),
                prescription.getDoctorId(),
                prescription.getDoctorName(),
                prescription.getPatientId(),
                prescription.getPatientName(),
                prescription.getMedicines()
        );
    }

    public static PatientDTO toPatientDTO(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getName(),
                patient.getEmail(),
                toUserDTO(patient.getUser())
        );
    }

    public static MedicalRecordResponseDTO toRecordDTO(MedicalRecord record) {
        return new MedicalRecordResponseDTO(
                record.getId(),
                record.getRecordDate(),
                record.getPrescriptions(),
                record.getLabReports(),
                record.getNotes()
        );
    }

}
