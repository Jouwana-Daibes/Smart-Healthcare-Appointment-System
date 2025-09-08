package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs;

public record DoctorDTO(
        Long id,
        String name,
        String email,
        String speciality,
        String availability,
        UserDTO user
) {}