package com.smarthealthcare.appointment.smarthealthcare_appointment.repository;

import com.smarthealthcare.appointment.smarthealthcare_appointment.model.MedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String> {
    List<MedicalRecord> findByPatientId(Long patientId);
    MedicalRecord save(MedicalRecord record);
    void deleteByPatientId(Long id);
}
