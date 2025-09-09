package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs;

import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Prescription;

import java.util.List;

public class MedicalRecordRequestDTO {
    private Long patientId;
    private String patientName;
    private List<Prescription> prescriptions;
    private List<String> labReports;
    private String notes;

    public MedicalRecordRequestDTO() {
    }

    public MedicalRecordRequestDTO(Long patientId, String patientName, List<Prescription> prescriptions, List<String> labReports, String notes) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.prescriptions = prescriptions;
        this.labReports = labReports;
        this.notes = notes;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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
        return "MedicalRecordRequestDTO{" +
                "patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", prescriptions=" + prescriptions +
                ", labReports=" + labReports +
                ", notes='" + notes + '\'' +
                '}';
    }
}
