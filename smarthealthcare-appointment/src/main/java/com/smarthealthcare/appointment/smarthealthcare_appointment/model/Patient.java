package com.smarthealthcare.appointment.smarthealthcare_appointment.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    // Doctor can have many appointments
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments  = new ArrayList<>();

    public Patient() {
    }

    public Patient(Long id, String name, String email, User user, List<Appointment> appointments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.user = user;
        this.appointments = appointments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", user=" + user +
                ", appointments=" + appointments +
                '}';
    }
}
