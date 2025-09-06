package com.smarthealthcare.appointment.smarthealthcare_appointment.repository;

import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor save(Doctor doctor);
    Optional<Doctor> findByEmail(String email);
    List<Doctor> findAll();
    Optional<Doctor> findById(Long id);
    void deleteById(Long id);


}
