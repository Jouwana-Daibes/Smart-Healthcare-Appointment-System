package com.smarthealthcare.appointment.smarthealthcare_appointment.repository;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.AppointmentResponse;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Appointment;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    //boolean existsByDoctorIdAndAppointmentTime(Long doctorId, LocalDateTime appointmentTime);
    Appointment save(Appointment appointment);
    List<Appointment> findAll();
    Optional<Appointment> findById(Long id);
    void delete(Appointment appointment);
    // tO CKECK APPOINTMENT TIME OVERLAPS WITH THE SAME DOCTOR
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId " +
            "AND a.appointmentDay = :day " +
            "AND (a.appointmentStartTime < :requestedEndTime " +
            "AND a.appointmentEndTime > :requestedStartTime)")
    boolean existsByDoctorIdAndTimeOverlapAndDay(
            @Param("doctorId") Long doctorId,
            @Param("requestedStartTime") LocalTime requestedStartTime,
            @Param("requestedEndTime") LocalTime requestedEndTime,
            @Param("day") String day
    );

    boolean existsByDoctorIdAndPatientId(Long doctorId, Long patientId);
    void deleteByPatientId(Long id);
    void deleteByDoctorId(Long doctorId);
    // Get all appointments for a specific doctor
    List<Appointment> findByDoctorId(Long doctorId);
    // Get all appointments for a specific patient
    List<Appointment> findByPatientId(Long patientId);
//    List<Appointment> findByDoctorIdAndAppointmentStartTimeBetween(
//            Long doctorId,
//            LocalTime startOfDay,
//            LocalTime endOfDay
//    );

    List<AppointmentResponse> findByDoctorIdAndAppointmentDay(
            Long doctorId,
            String appointmentDay
    );


}
