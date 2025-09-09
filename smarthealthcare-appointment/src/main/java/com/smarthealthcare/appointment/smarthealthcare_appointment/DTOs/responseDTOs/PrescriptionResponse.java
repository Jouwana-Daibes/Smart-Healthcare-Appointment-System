package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs;

import java.time.LocalDateTime;
import java.util.List;

public record PrescriptionResponse (
        String id,
        Long doctorId,
        String doctorName,
        Long patientId,
        String patientName,
        List<String> medicines
){}
