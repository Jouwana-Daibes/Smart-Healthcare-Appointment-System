package com.smarthealthcare.appointment.smarthealthcare_appointment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // maybe change it to phone number
    @Column(unique = true)
    private String email;
    private String name;
    private String speciality;
    private String availability;


    public Doctor() {
    }

    public Doctor(Long id, String email, String name, String speciality, String availability) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.speciality = speciality;
        this.availability = availability;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Doctor{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", speciality='" + speciality + '\'' +
                ", availability='" + availability + '\'' +
                '}';
    }
}
