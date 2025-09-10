// WANTEDDDDDDDDDDDD


package com.smarthealthcare.appointment.smarthealthcare_appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.requestDTOs.MedicalRecordRequestDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.DoctorDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.UserDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.config.SecurityConfig;
import com.smarthealthcare.appointment.smarthealthcare_appointment.controller.DoctorController;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserAlreadyExistsException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.MedicalRecord;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Prescription;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.*;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.CustomUserDetailsService;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.DoctorService;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.MedicalRecordService;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

///**
// * Unit tests for the DoctorController REST API endpoints.
// * Uses @WebMvcTest to test only the web layer (controller) without starting the full application context.
// * Dependencies like DoctorService are mocked using @MockBean to isolate the controller behavior.
// * MockMvc is used to simulate HTTP requests and verify responses.
// * ObjectMapper is used to serialize and deserialize JSON request and response bodies.
// * This class covers all CRUD operations:
// * - CREATE: addDoctor
// * - READ: getAllDoctors, getDoctorById
// * - UPDATE: updateDoctor
// * - DELETE: deleteDoctor
// * Security:
// * Endpoints can be secured with roles (e.g., ADMIN) and tested using @WithMockUser.
// */
//
@WebMvcTest(DoctorController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = {"ADMIN"})  // to simulate an authenticated user
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    @MockitoBean
    private  DoctorRepository doctorRepository;

    @MockitoBean
    private PatientRepository patientRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private  PasswordEncoder passwordEncoder;
    @MockitoBean
    private PrescriptionRepository prescriptionRepository;
    @MockitoBean
    private AppointmentRepository appointmentRepository;

    @MockitoBean
    private  MedicalRecordRepository medicalRecordRepository;



    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testGetAllDoctors() throws Exception {
        DoctorDTO doctor = new DoctorDTO(
                1L,
                "Dr. Emily",
                "emily@hospital.com",
                "Cardiology",
                LocalDateTime.of(2025, 1, 1, 9, 0),
                LocalDateTime.of(2025, 1, 1, 17, 0),
                "Mon-Fri",
                new UserDTO(100L, "emilyUser")
        );

        System.out.println(when(doctorService.getAllDoctors()).thenReturn(List.of(doctor)));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Dr. Emily"))
                .andExpect(jsonPath("$[0].speciality").value("Cardiology"))
                .andExpect(jsonPath("$[0].user.username").value("emilyUser"));
    }

    @Test
    void testUpdateDoctorSuccess() throws Exception {
        DoctorDTO updated = new DoctorDTO(
                1L,
                "Dr. Smith",
                "smith@hospital.com",
                "Neurology",
                LocalDateTime.of(2025, 1, 1, 9, 0),
                LocalDateTime.of(2025, 1, 1, 17, 0),
                "Tue-Thu",
                new UserDTO(101L, "smithUser")
        );
        when(doctorService.updateDoctor(eq(1L), any(Doctor.class))).thenReturn(updated);

        mockMvc.perform(put("/api/doctors/1")
                        .contentType("application/json")
                        .content("{\"name\":\"Dr. Smith\",\"email\":\"smith@hospital.com\",\"speciality\":\"Neurology\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.speciality").value("Neurology"))
                .andExpect(jsonPath("$.email").value("smith@hospital.com"));
    }

    @Test
    void testGetAllDoctorsEmptyList() throws Exception {
        when(doctorService.getAllDoctors()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]")); // Empty JSON array
    }

    @Test
    void testUpdateDoctorNotFound() throws Exception {
        DoctorDTO updateDTO = new DoctorDTO(
                999L, "Dr. Ghost", "ghost@hospital.com", "Unknown",
                LocalDateTime.now(), LocalDateTime.now(), "Mon-Fri", null
        );

        when(doctorService.updateDoctor(eq(999L), any(Doctor.class)))
                .thenThrow(new RuntimeException("Doctor not found"));

        mockMvc.perform(put("/api/doctors/999")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testDeleteDoctorNotFound() throws Exception {
        doThrow(new RuntimeException("Doctor not found")).when(doctorService).deleteDoctor(999L);

        mockMvc.perform(delete("/api/doctors/999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = {"DOCTOR"})  // or ADMIN if required by security
    void testCreateMedicalRecordSuccess() throws Exception {
        Prescription prescription = new Prescription(1L, "Dr. Emily", 10L, "Patient A", List.of("Med1", "Med2"));
        MedicalRecordRequestDTO request = new MedicalRecordRequestDTO(
                10L,
                "Patient A",
                List.of(prescription),
                List.of("Lab report 1"),
                "Patient notes"
        );

        MedicalRecord record = new MedicalRecord(
                "1",
                10L,
                "Patient A",
                LocalDateTime.now(),
                List.of(prescription),
                List.of("Lab report 1"),
                "Patient notes"
        );

        when(medicalRecordService.addRecord(any(MedicalRecordRequestDTO.class), eq(1L)))
                .thenReturn(record);

        mockMvc.perform(post("/api/doctors/records/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = {"DOCTOR"})
    void testCreateMedicalRecordDoctorNotFound() throws Exception {
        MedicalRecordRequestDTO request = new MedicalRecordRequestDTO(10L, "Patient A", null, null, "Notes");

        when(medicalRecordService.addRecord(any(MedicalRecordRequestDTO.class), eq(999L)))
                .thenThrow(new RuntimeException("Doctor not found"));

        mockMvc.perform(post("/api/doctors/records/999")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }


}

