package com.smarthealthcare.appointment.smarthealthcare_appointment.repository;

import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    List<Prescription> findByPatientId(Long patientId);
    List<Prescription> findByDoctorId(Long doctorId);
    Prescription save(Prescription prescription);
    void deleteByPatientId(Long id);
    void deleteByDoctorId(Long doctorId);
    Optional<Prescription> findById(String id);
}
