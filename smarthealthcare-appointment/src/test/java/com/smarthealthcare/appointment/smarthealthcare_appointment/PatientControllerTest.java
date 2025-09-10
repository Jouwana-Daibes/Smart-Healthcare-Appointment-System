package com.smarthealthcare.appointment.smarthealthcare_appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.MedicalRecordResponseDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.PatientDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.UserDTO;
import com.smarthealthcare.appointment.smarthealthcare_appointment.config.SecurityConfig;
import com.smarthealthcare.appointment.smarthealthcare_appointment.controller.PatientController;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.MedicalRecord;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Prescription;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.User;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.*;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.CustomUserDetailsService;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.DoctorService;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.MedicalRecordService;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.PatientService;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.EntityMapper;
import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@Import(SecurityConfig.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private DoctorRepository doctorRepository;

    @MockitoBean
    private PatientRepository patientRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;
    @MockitoBean
    private PrescriptionRepository prescriptionRepository;
    @MockitoBean
    private AppointmentRepository appointmentRepository;

    @MockitoBean
    private  MedicalRecordRepository medicalRecordRepository;



    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllPatientsSuccess() throws Exception {
        List<PatientDTO> patients = List.of(
                new PatientDTO(1L, "John Doe", "john@example.com", new UserDTO(10L, "johnUser")),
                new PatientDTO(2L, "Jane Smith", "jane@example.com", new UserDTO(20L, "janeUser"))
        );

        when(patientService.getAllPatients()).thenReturn(patients);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].user.username").value("johnUser"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"))
                .andExpect(jsonPath("$[1].user.username").value("janeUser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetPatientByIdSuccess() throws Exception {
        PatientDTO patient = new PatientDTO(1L, "John Doe", "john@example.com", new UserDTO(10L, "johnUser"));
        when(patientService.getPatientById(1L)).thenReturn(patient);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.user.username").value("johnUser"));
    }

    @Test
    @WithMockUser(username = "patientUser", roles = {"PATIENT"})
    void testUpdatePatientSuccess() throws Exception {
        Long patientId = 1L;

        User user = new User();
        user.setId(100L);
        user.setUsername("patientUser");

        Patient patient = new Patient(patientId, "John Doe", "john@example.com", user, List.of());

        // Mock repositories
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(userRepository.findByUsername("patientUser")).thenReturn(Optional.of(user));

        // Mock save (returns updated patient)
        patient.setName("John Updated");
        patient.setEmail("john.updated@example.com");
        when(patientRepository.save(patient)).thenReturn(patient);

        // Perform PATCH
        mockMvc.perform(put("/api/patients/{id}", patientId)
                .contentType("application/json")
                .content("{\"name\":\"John Updated\", \"email\":\"john.updated@example.com\"}"));
    }

    @Test
    @WithMockUser(username = "patientUser", roles = {"PATIENT"})
    void testUpdatePatientNotFound() throws Exception {
        Long patientId = 999L;

        User user = new User();
        user.setId(100L);
        user.setUsername("patientUser");

        Patient patient = new Patient(patientId, "John Doe", "john@example.com", user, List.of());

        // Mock repositories
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(userRepository.findByUsername("patientUser")).thenReturn(Optional.of(user));

        // Mock save (returns updated patient)
        patient.setName("John Updated");
        patient.setEmail("john.updated@example.com");
        when(patientRepository.save(patient)).thenReturn(patient);

        mockMvc.perform(put("/api/patients/{id}", patientId))
        .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "patientUser", roles = {"PATIENT"})
    void testPatchPatientSuccess() throws Exception {
        Long patientId = 1L;

        User user = new User();
        user.setId(100L);
        user.setUsername("patientUser");

        Patient patient = new Patient(patientId, "John Doe", "john@example.com", user, List.of());

        // Mock repositories
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(userRepository.findByUsername("patientUser")).thenReturn(Optional.of(user));

        // Mock save (returns updated patient)
        patient.setName("John Updated");
        patient.setEmail("john.updated@example.com");
        when(patientRepository.save(patient)).thenReturn(patient);

        // Perform PATCH
        mockMvc.perform(patch("/api/patients/{id}", patientId)
                .contentType("application/json")
                .content("{\"name\":\"John Updated\", \"email\":\"john.updated@example.com\"}"));
    }


        @Test
        @WithMockUser(roles = {"ADMIN"})
        void testDeletePatientSuccess() throws Exception {
            Long patientId = 1L;

            User user = new User();
            user.setId(100L);
            user.setUsername("john");

            Patient patient = new Patient(patientId, user.getUsername(), "john@example.com", user, List.of());

            // Mock repositories
            when(patientRepository.existsById(patientId)).thenReturn(true);
            doNothing().when(patientRepository).deleteById(patientId);

            mockMvc.perform(delete("/api/patients/{id}", patientId))
                    .andExpect(status().isNoContent());
        }


    @Test
    @WithMockUser(roles = {"PATIENT"})
    void testGetMyRecordsSuccess() throws Exception {
        Long patientId = 1L;

        User user = new User();
        user.setId(100L);
        user.setUsername("patientUser");

        Patient patient = new Patient(patientId, "John Doe", "john@example.com", user, List.of());

        Prescription prescription = new Prescription(1L, "Dr. Emily", 10L, user.getUsername(), List.of("Med1", "Med2"));
        MedicalRecord record = new MedicalRecord("1", 10L, "John Doe", LocalDateTime.now(), List.of(prescription), List.of("Lab report 1"), "Notes");

        // Return the list of MedicalRecords from service
        when(medicalRecordService.getRecordsByPatientId(10L)).thenReturn(List.of(record));

        // Map the MedicalRecord to MedicalRecordResponseDTO
        MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO(
                record.getId(),
                record.getRecordDate(),
                record.getPrescriptions(),
                record.getLabReports(),
                record.getNotes()
        );

        // Mock the EntityMapper
        Mockito.mockStatic(EntityMapper.class).when(() -> EntityMapper.toRecordDTO(record)).thenReturn(dto);

        mockMvc.perform(get("/api/patients/MyRecords")
                        .param("patientId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].notes").value("Notes"))
                .andExpect(jsonPath("$[0].labReports[0]").value("Lab report 1"))
                .andExpect(jsonPath("$[0].prescriptions[0].doctorName").value("Dr. Emily"))
                .andExpect(jsonPath("$[0].prescriptions[0].medicines[0]").value("Med1"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetPatientByIdNotFound() throws Exception {
        Long patientId = 999L;

        User user = new User();
        user.setId(100L);
        user.setUsername("patientUser");

        Patient patient = new Patient(patientId, user.getUsername(), "john@example.com", user, List.of());

        when(patientService.getPatientById(999L)).thenThrow(new RuntimeException("Patient not found"));

        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "patientUser", roles = {"PATIENT"})
    void testGetMyRecordsPatientNotFound() throws Exception {
        Long patientId = 999L;

        // Mock the service to return an empty list when patient has no records
        when(medicalRecordService.getRecordsByPatientId(patientId))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/patients/MyRecords")
                        .param("patientId", String.valueOf(patientId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty()); // Expecting empty JSON array
    }



}

