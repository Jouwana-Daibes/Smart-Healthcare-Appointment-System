package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs;

public record PatientDTO(
        Long id,
        String name,
        String email,
        UserDTO user
) {}
