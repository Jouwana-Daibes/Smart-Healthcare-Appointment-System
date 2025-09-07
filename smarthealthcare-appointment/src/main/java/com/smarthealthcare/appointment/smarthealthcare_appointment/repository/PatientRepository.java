package com.smarthealthcare.appointment.smarthealthcare_appointment.repository;

import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    Patient save(Patient patient);
    Optional<Patient> findById(Long id);
}
