package com.smarthealthcare.appointment.smarthealthcare_appointment.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments", // to prevent duplicates at DB level
        uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "appointment_time" }))
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // doctor foreign key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // patient foreign key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Appointment start and end times
    @Column(name = "appointment_start_time", nullable = false)
    private LocalDateTime appointmentStartTime;

    @Column(name = "appointment_end_time", nullable = false)
    private LocalDateTime appointmentEndTime;

    @Column(name = "appointment_day", nullable = false)
    private String appointmentDay;

    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    public Appointment() {
    }

    public Appointment(Long id, Doctor doctor, Patient patient, LocalDateTime appointmentStartTime, LocalDateTime appointmentEndTime, String appointmentDay, Status status) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentStartTime = appointmentStartTime;
        this.appointmentEndTime = appointmentEndTime;
        this.appointmentDay = appointmentDay;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(LocalDateTime appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public LocalDateTime getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(LocalDateTime appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public String getAppointmentDay() {
        return appointmentDay;
    }

    public void setAppointmentDay(String appointmentDay) {
        this.appointmentDay = appointmentDay;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", doctor=" + doctor +
                ", patient=" + patient +
                ", appointmentStartTime=" + appointmentStartTime +
                ", appointmentEndTime=" + appointmentEndTime +
                ", appointmentDay=" + appointmentDay +
                ", status=" + status +
                '}';
    }
}
