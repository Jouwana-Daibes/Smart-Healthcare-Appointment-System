package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs;

public class RegistrationDoctorRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String speciality;
    private String availability;

    public RegistrationDoctorRequest() {
    }

    public RegistrationDoctorRequest(String username, String password, String name, String email, String speciality, String availability) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.speciality = speciality;
        this.availability = availability;
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

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "RegistrationDoctorRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", speciality='" + speciality + '\'' +
                ", availability='" + availability + '\'' +
                '}';
    }
}
