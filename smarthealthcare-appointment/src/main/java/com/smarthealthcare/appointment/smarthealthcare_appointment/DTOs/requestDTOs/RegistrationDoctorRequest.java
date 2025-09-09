package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs;

import java.time.LocalDateTime;

public class RegistrationDoctorRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String speciality;
    LocalDateTime startTime;        // e.g., 09:00
    LocalDateTime endTime;           // e.g., 17:00
    String availableDays;

    public RegistrationDoctorRequest() {
    }

    public RegistrationDoctorRequest(String username, String password, String name, String email, String speciality, LocalDateTime startTime, LocalDateTime endTime, String availableDays) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.speciality = speciality;
        this.startTime = startTime;
        this.endTime = endTime;
        this.availableDays = availableDays;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }

    @Override
    public String toString() {
        return "RegistrationDoctorRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", speciality='" + speciality + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", availableDays='" + availableDays + '\'' +
                '}';
    }
}
