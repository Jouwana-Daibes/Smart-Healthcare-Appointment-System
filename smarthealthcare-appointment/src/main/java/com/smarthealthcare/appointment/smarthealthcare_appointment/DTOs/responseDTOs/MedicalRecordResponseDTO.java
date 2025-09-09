package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs;

import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Prescription;

import java.time.LocalDateTime;
import java.util.List;

public class MedicalRecordResponseDTO {
    private String id;
    private LocalDateTime recordDate;
    private List<Prescription> prescriptions;
    private List<String> labReports;
    private String notes;

    public MedicalRecordResponseDTO() {
    }

    public MedicalRecordResponseDTO(String id, LocalDateTime recordDate, List<Prescription> prescriptions, List<String> labReports, String notes) {
        this.id = id;
        this.recordDate = recordDate;
        this.prescriptions = prescriptions;
        this.labReports = labReports;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDateTime recordDate) {
        this.recordDate = recordDate;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<String> getLabReports() {
        return labReports;
    }

    public void setLabReports(List<String> labReports) {
        this.labReports = labReports;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "MedicalRecordResponseDTO{" +
                "id='" + id + '\'' +
                ", recordDate=" + recordDate +
                ", prescriptions=" + prescriptions +
                ", labReports=" + labReports +
                ", notes='" + notes + '\'' +
                '}';
    }
}
