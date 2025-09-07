package com.smarthealthcare.appointment.smarthealthcare_appointment.service;

import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserAlreadyExistsException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Service class responsible for managing doctor entities.
 */
 @Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Create doctor
    public Doctor addDoctor(Doctor newDoctor) {
        Optional<Doctor> existingUser = doctorRepository.findByEmail(newDoctor.getEmail());
        // To avoid adding the same doctor more than once
        if (existingUser.isPresent()){
            throw new UserAlreadyExistsException(newDoctor.getName() + " already exists");
        }
        return doctorRepository.save(newDoctor);
    }

    // Read all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Read by id
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    // Delete doctor
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new UserNotFoundException("Doctor with id " + id + " is not found!");
        }
        doctorRepository.deleteById(id);
    }


    public Optional<Doctor> patchDoctor(Long id, Map<String, Object> updates) {
          Doctor doctor = doctorRepository.findById(id)
                  .orElseThrow(() -> new UserNotFoundException("Doctor with ID " + id + " is not found!"));

          updates.forEach((key , value) -> {
              switch (key) {
                  case "email" -> doctor.setEmail((String) value);
                  case "name"  -> doctor.setName((String) value);
                  case "speciality" -> doctor.setSpeciality((String) value);
                  case "availability" -> doctor.setAvailability((String) value);
              }
          });

          return Optional.of(doctor);
    }

    public Optional<Doctor> updateDoctor(Long id, Doctor doctor){
        Doctor updatedDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Doctor with ID " + id + " not found"));


        updatedDoctor.setName(doctor.getName());
        updatedDoctor.setEmail(doctor.getEmail());
        updatedDoctor.setSpeciality(doctor.getSpeciality());
        updatedDoctor.setAvailability(doctor.getAvailability());
        doctorRepository.save(updatedDoctor);
        return  Optional.of(updatedDoctor);
    }
}

