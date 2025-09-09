package com.smarthealthcare.appointment.smarthealthcare_appointment.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "medical_records")
public class MedicalRecord {
    @Id
    private String id;
    private Long patientId;
    private String patientName;
    private LocalDateTime recordDate;
    private List<Prescription> prescriptions; // list of medicines
    private List<String> labReports;    // URLs or text of lab results
    private String notes;

    public MedicalRecord() {
    }

    public MedicalRecord(String id, Long patientId, String patientName, LocalDateTime recordDate, List<Prescription> prescriptions, List<String> labReports, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
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
        return "Record{" +
                "id='" + id + '\'' +
                ", patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", recordDate=" + recordDate +
                ", prescriptions=" + prescriptions +
                ", labReports=" + labReports +
                ", notes='" + notes + '\'' +
                '}';
    }
}
