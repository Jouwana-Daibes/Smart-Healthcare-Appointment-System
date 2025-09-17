package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs;

import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Status;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record AppointmentResponse(
        Long id,
        Long doctorId,
        Long patientId,
        LocalTime appointmentStartTime,         // e.g., 09:00
        LocalTime appointmentEndTime,           // e.g., 17:00
        String appointmentDay,
        Status status,
        String doctorName,
        String patientName
) {}
