package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PrescriptionRequest(
        @NotNull(message = "Patient ID is required")
        Long patientId,

        @NotNull(message = "Medicines cannot be empty")
        List<String> medicines
) {
}
