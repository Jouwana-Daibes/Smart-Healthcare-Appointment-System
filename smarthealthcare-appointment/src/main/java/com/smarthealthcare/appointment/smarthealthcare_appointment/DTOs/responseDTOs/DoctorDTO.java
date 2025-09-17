package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record DoctorDTO(
        Long id,
        String name,
        String email,
        String speciality,
        // Recurring schedule
        LocalTime startTime,         // e.g., 09:00
        LocalTime endTime,           // e.g., 17:00
        String availableDays,
        UserDTO user
) {}