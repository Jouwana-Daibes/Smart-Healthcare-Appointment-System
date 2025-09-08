package com.smarthealthcare.appointment.smarthealthcare_appointment.repository;

import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    Patient save(Patient patient);
    Optional<Patient> findById(Long id);
    // Return only the foreign key userId
    @Query("SELECT p.user.id FROM Patient p WHERE p.id = :id")
    Long findUserIdByPatientId(@Param("id") Long patientId);
}
