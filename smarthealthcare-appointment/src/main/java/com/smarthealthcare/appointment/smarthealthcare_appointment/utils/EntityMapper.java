package com.smarthealthcare.appointment.smarthealthcare_appointment.utils;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.DoctorDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.PatientDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.UserDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.User;

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
                doctor.getAvailability(),
                toUserDTO(doctor.getUser())
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

}
