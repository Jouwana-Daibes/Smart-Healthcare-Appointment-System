package com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs;

public class RegistrationAdminRequest {
    private String username;
    private String password;

    public RegistrationAdminRequest() {
    }

    public RegistrationAdminRequest(String username, String password) {
        this.username = username;
        this.password = password;
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
}
