package com.smarthealthcare.appointment.smarthealthcare_appointment.service;

import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.DoctorDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserAlreadyExistsException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.User;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.DoctorRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    public DoctorService(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
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
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new UserNotFoundException("Doctor with id " + id + " is not found!");
        }
        doctorRepository.deleteById(id);
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
                  case "availability" -> doctor.setAvailability((String) value);
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
        updatedDoctor.setAvailability(doctor.getAvailability());
        Doctor newDoctor = doctorRepository.save(updatedDoctor);
        return  EntityMapper.toDoctorDTO(newDoctor);
    }
}

