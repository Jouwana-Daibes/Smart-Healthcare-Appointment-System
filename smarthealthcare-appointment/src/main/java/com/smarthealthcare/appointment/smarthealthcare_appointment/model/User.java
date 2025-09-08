package com.smarthealthcare.appointment.smarthealthcare_appointment.model;


import jakarta.persistence.*;
import java.util.Set;

/**
 * Represents a system user (Admin, Doctor, or Patient).
 * Holds credentials + roles.
 * Password will be BCrypt hashed before saving.
 * Stored in relational database.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;


    // This mapping makes sure each user can have one or more roles, stored in a separate join table like user_roles.
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)   // Saves enum names as text in DB
    private Set<Role> roles;

    // link to doctor (nullable if not a doctor)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL )
    private Doctor doctor;

    // Link to patient (nullable if not a patient)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Patient patient;

    public User() {
    }

    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", doctor=" + doctor +
                ", patient=" + patient +
                '}';
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
