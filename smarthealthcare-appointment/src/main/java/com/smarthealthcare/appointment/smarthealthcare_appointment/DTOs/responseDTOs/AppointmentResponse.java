package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs;

import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Status;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record AppointmentResponse(
        Long id,
        Long doctorId,
        Long patientId,
        LocalDateTime startTime,         // e.g., 09:00
        LocalDateTime endTime,           // e.g., 17:00
        String Days,
        Status status,
        String doctorName,
        String patientName
) {}
