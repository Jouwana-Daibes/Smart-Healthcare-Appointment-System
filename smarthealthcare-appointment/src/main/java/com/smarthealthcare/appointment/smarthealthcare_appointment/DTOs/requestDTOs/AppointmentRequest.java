package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record AppointmentRequest(
        @NotNull(message = "Doctor ID is required")
        Long doctorId,
        @NotNull(message = "Appointment time is required")
        LocalTime startTime,         // e.g., 09:00
        LocalTime endTime,           // e.g., 17:00
        String Days
        ) {}
