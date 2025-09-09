package com.smarthealthcare.appointment.smarthealthcare_appointment.service;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.DoctorDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserAlreadyExistsException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Appointment;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Prescription;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.User;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.AppointmentRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.DoctorRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.PrescriptionRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.EntityMapper;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Service class responsible for managing doctor entities.
 */
 @Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private PrescriptionRepository prescriptionRepository;
    private AppointmentRepository appointmentRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder, PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

//    // Create doctor
//    public Doctor addDoctor(Doctor newDoctor) {
//        Optional<Doctor> existingUser = doctorRepository.findByEmail(newDoctor.getEmail());
//        // To avoid adding the same doctor more than once
//        if (existingUser.isPresent()){
//            throw new UserAlreadyExistsException(newDoctor.getName() + " already exists");
//        }
//        return doctorRepository.save(newDoctor);
//    }

    // Read all doctors
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(EntityMapper::toDoctorDTO)
                .collect(Collectors.toList());
    }

    // Read by id
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Doctor with ID " + id + " not found!"));
        return EntityMapper.toDoctorDTO(doctor);
    }

    // Delete doctor
    @Transactional
    //TODO: Test it in postman
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new UserNotFoundException("Doctor with id " + id + " is not found!");
        }
        doctorRepository.deleteById(id);
        List<Prescription> prescriptions = prescriptionRepository.findByDoctorId(id);
        System.out.println("prescriptions = " + prescriptions);
        for(Prescription prescription : prescriptions){
                prescriptionRepository.deleteByDoctorId(id);
        }

        List<Appointment> appointments = appointmentRepository.findByDoctorId(id);
        for(Appointment appointment : appointments){
                prescriptionRepository.deleteByDoctorId(id);
        }
    }


    public DoctorDTO patchDoctor(Long id, Map<String, Object> updates) {
          Doctor doctor = doctorRepository.findById(id)
                  .orElseThrow(() -> new UserNotFoundException("Doctor with ID " + id + " is not found!"));

          User user = doctor.getUser();
          updates.forEach((key , value) -> {
              switch (key) {
                  case "email" -> doctor.setEmail((String) value);
                  case "name"  -> doctor.setName((String) value);
                  case "speciality" -> doctor.setSpeciality((String) value);
                  case "startTime" -> doctor.setStartTime((LocalDateTime) value);
                  case "endTime" -> doctor.setEndTime((LocalDateTime) value);
                  case "availableDays" -> doctor.setAvailableDays((String) value);
                  case "username" -> user.getUsername();
                  case "password" -> user.setPassword(passwordEncoder.encode(user.getPassword()));
              }
          });

        Doctor updatedDoctor = doctorRepository.save(doctor);
        return EntityMapper.toDoctorDTO(updatedDoctor);
    }

    public DoctorDTO updateDoctor(Long id, Doctor doctor){
        Doctor updatedDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Doctor with ID " + id + " not found"));

        updatedDoctor.setName(doctor.getName());
        updatedDoctor.setEmail(doctor.getEmail());
        updatedDoctor.setSpeciality(doctor.getSpeciality());
        updatedDoctor.setStartTime(doctor.getStartTime());
        updatedDoctor.setEndTime(doctor.getEndTime());
        updatedDoctor.setAvailableDays(doctor.getAvailableDays());
        Doctor newDoctor = doctorRepository.save(updatedDoctor);
        return  EntityMapper.toDoctorDTO(newDoctor);
    }

    public List<DoctorDTO> findDoctorsBySpeciality(String speciality) {
        List<Doctor> doctors = doctorRepository.findBySpecialityIgnoreCaseContaining(speciality);
        return doctors.stream()
                .map(EntityMapper::toDoctorDTO)
                .toList();
    }
}

