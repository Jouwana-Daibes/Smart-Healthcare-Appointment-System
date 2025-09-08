//package com.smarthealthcare.appointment.smarthealthcare_appointment;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.config.SecurityConfig;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.controller.DoctorController;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserAlreadyExistsException;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.service.CustomUserDetailsService;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.service.DoctorService;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.utils.JwtUtil;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.*;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
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
//@WebMvcTest(DoctorController.class)
//@Import(SecurityConfig.class)
//public class DoctorControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private DoctorService doctorService;
//
//    @MockitoBean
//    private JwtUtil jwtUtil;
//
//    @MockitoBean
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    /**
//     * Test case: Successfully create a doctor by an ADMIN role.
//     * Verifies that the response status is 200 OK and the returned doctor JSON has the correct name.
//     */
//    @Test
//    @DisplayName("Create Doctor - Success")
//    @WithMockUser(roles = {"ADMIN"})  // to simulate an authenticated user
//    void addDoctor_success() throws Exception {
//        Doctor doctor = new Doctor(1l, "john@example.com", "Dr. John", "Cardiology", "Mon-Fri 9AM-5PM");
//        Mockito.when(doctorService.addDoctor(any(Doctor.class))).thenReturn(doctor);
//
//        mockMvc.perform(post("/api/doctors")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(doctor)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Dr. John"));
//    }
//
//    /**
//     * Test case: create a doctor by forbidden role.
//     * Verifies that the response status is 403 forbidden.
//     */
//    @Test
//    @DisplayName("Create Doctor - Role Access forbidden")
//    @WithMockUser(roles = {"PATIENT"})
//    void addDoctor_RoleForbidden() throws Exception {
//        Doctor doctor = new Doctor(1l, "john@example.com", "Dr. John", "Cardiology", "Mon-Fri 9AM-5PM");
//        Mockito.when(doctorService.addDoctor(any(Doctor.class))).thenReturn(doctor);
//
//        mockMvc.perform(post("/api/doctors")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(doctor)))
//                .andExpect(status().isForbidden());
//    }
//
//    /**
//     * Test case: Attempt to create a doctor with a duplicate email by an ADMIN role.
//     * Verifies that the response status is 400 Bad Request.
//     */
//    @Test
//    @DisplayName("Create Doctor - Duplicate Email")
//    @WithMockUser(roles = {"ADMIN"})
//    void addDoctor_DuplicateEmail() throws Exception {
//        Doctor doctor = new Doctor(null, "john@example.com", "Dr. John", "Cardiology", "Mon-Fri 9AM-5PM");
//        Mockito.when(doctorService.addDoctor(any(Doctor.class)))
//                .thenThrow(new UserAlreadyExistsException("Doctor with email already exists"));
//
//        mockMvc.perform(post("/api/doctors")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(doctor)))
//                .andExpect(status().isBadRequest());
//    }
//
//    /**
//     * Test case: Retrieve all doctors successfully by all roles.
//     * Verifies that the response status is 200 OK and the returned JSON list has the expected length.
//     */
//    @Test
//    @DisplayName("Get All Doctors - Success")
//    @WithMockUser(roles = {"ADMIN", "PATIENT", "DOCTOR"})
//    void getAllDoctors_Success() throws Exception {
//        Doctor d1 = new Doctor(1L,  "a@example.com", "Dr. A", "Dermatology", "Mon-Fri 9AM-5PM");
//        Doctor d2 = new Doctor(2L, "b@example.com", "Dr. B", "Pediatrics",  "Mon-Fri 9AM-5PM");
//        Mockito.when(doctorService.getAllDoctors()).thenReturn(Arrays.asList(d1, d2));
//
//        mockMvc.perform(get("/api/doctors"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//
//    /**
//     * Test case: Retrieve all doctors when the list is empty by all roles.
//     * Verifies that the response status is 200 OK and the returned JSON is an empty array.
//     */
//    @Test
//    @DisplayName("Get All Doctors - Empty List")
//    @WithMockUser(roles = {"ADMIN", "PATIENT", "DOCTOR"})
//    void getAllDoctors_Empty() throws Exception {
//        Mockito.when(doctorService.getAllDoctors()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/api/doctors"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("[]"));
//    }
//
//    /**
//     * Test case: Retrieve a doctor by a valid ID by all roles.
//     * Verifies that the response status is 200 OK and the returned doctor JSON contains the expected email.
//     */
//    @Test
//    @DisplayName("Get Doctor By ID - Success")
//    @WithMockUser(roles = {"ADMIN", "PATIENT", "DOCTOR"})
//    void getDoctorById_Success() throws Exception {
//        Doctor doctor = new Doctor(1L, "a@example.com", "Dr. A", "Neurology", "Mon-Fri 9AM-5PM");
//        Mockito.when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(doctor));
//
//        mockMvc.perform(get("/api/doctors/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("a@example.com"));
//    }
//
//    /**
//     * Test case: Retrieve a doctor by a non-existent ID by all roles.
//     * Verifies that the response status is 404 Not Found.
//     */
//    @Test
//    @DisplayName("Get Doctor By ID - Not Found")
//    @WithMockUser(roles = {"ADMIN", "PATIENT", "DOCTOR"})
//    void getDoctorById_NotFound() throws Exception {
//        Mockito.when(doctorService.getDoctorById(99L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/api/doctors/99"))
//                .andExpect(status().isNotFound());
//    }
//
//    /**
//     * Test case: Successfully update a doctor by ADMIN role.
//     * Verifies that the response status is 200 OK and the returned JSON reflects the updated doctor.
//     */
//    @Test
//    @DisplayName("Update Doctor - Success")
//    @WithMockUser(roles = {"ADMIN"})
//    void updateDoctor_Success() throws Exception {
//        Doctor updatedDoctor = new Doctor(1L, "a@example.com", "Dr. A", "Neurology", "Mon-Fri 9AM-5PM");
//        Mockito.when(doctorService.updateDoctor(eq(1L), any(Doctor.class))).thenReturn(Optional.of(updatedDoctor));
//
//        mockMvc.perform(put("/api/doctors/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedDoctor)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Dr. A"));
//    }
//
//    /**
//     * Test case: Try to update a doctor by a forbidden role.
//     * Verifies that the response status is 403 - forbidden.
//     */
//    @Test
//    @DisplayName("Update Doctor - Role access forbidden")
//    @WithMockUser(roles = {"PATIENT"})
//    void updateDoctor_AccessForbidden() throws Exception {
//        Doctor updatedDoctor = new Doctor(1L, "a@example.com", "Dr. A", "Neurology", "Mon-Fri 9AM-5PM");
//        Mockito.when(doctorService.updateDoctor(eq(1L), any(Doctor.class))).thenReturn(Optional.of(updatedDoctor));
//
//        mockMvc.perform(put("/api/doctors/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedDoctor)))
//                .andExpect(status().isForbidden());
//    }
//
//    /**
//     * Test case: Attempt to update a doctor with a non-existent ID by an ADMIN role.
//     * Verifies that the response status is 404 Not Found.
//     */
//    @Test
//    @DisplayName("Update Doctor - Not Found")
//    @WithMockUser(roles = {"ADMIN"})
//    void updateDoctor_NotFound() throws Exception {
//        Doctor updatedDoctor = new Doctor(null, "a@example.com", "Dr. A", "Neurology", "Mon-Fri 9AM-5PM");
//        Mockito.when(doctorService.updateDoctor(eq(999L), any(Doctor.class))).thenReturn(Optional.empty());
//
//        mockMvc.perform(put("/api/doctors/999")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedDoctor)))
//                .andExpect(status().isNotFound());
//    }
//
//    /**
//     * Test case: Attempt to update a doctor with a non-existent ID by forbidden role.
//     * Verifies that the response status is 403 forbidden.
//     */
//    @Test
//    @DisplayName("Update Doctor - Not Found - ACCESS FORBIDDER")
//    @WithMockUser(roles = {"PATIENT"})
//    void updateDoctor_NotFound_AccessForbidden() throws Exception {
//        Doctor updatedDoctor = new Doctor(null, "a@example.com", "Dr. A", "Neurology", "Mon-Fri 9AM-5PM");
//        Mockito.when(doctorService.updateDoctor(eq(999L), any(Doctor.class))).thenReturn(Optional.empty());
//
//        mockMvc.perform(put("/api/doctors/999")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedDoctor)))
//                .andExpect(status().isForbidden());
//    }
//
//    /**
//     * Test case: Successfully delete a doctor by an ADMIN role.
//     * Verifies that the response status is 204 No Content.
//     */
//    //DELETE
//    @Test
//    @DisplayName("Delete Doctor - Success")
//    @WithMockUser(roles = {"ADMIN"})
//    void deleteDoctor_Success() throws Exception {
//        Mockito.doNothing().when(doctorService).deleteDoctor(1L);
//
//        mockMvc.perform(delete("/api/doctors/1"))
//                .andExpect(status().isNoContent());
//    }
//
//    /**
//     * Test case: Successfully delete a doctor by a forbidden role.
//     * Verifies that the response status is 403 forbidden.
//     */
//    @Test
//    @DisplayName("Delete Doctor - Access Forbidden")
//    @WithMockUser(roles = {"PATIENT"})
//    void deleteDoctor_AccessForbidden() throws Exception {
//        Mockito.doNothing().when(doctorService).deleteDoctor(1L);
//
//        mockMvc.perform(delete("/api/doctors/1"))
//                .andExpect(status().isForbidden());
//    }
//
//    /**
//     * Test case: Attempt to delete a doctor with a non-existent ID by an ADMIN Role.
//     * Verifies that the response status is 404 Not Found.
//     */
//    @Test
//    @DisplayName("Delete Doctor - Not Found")
//    @WithMockUser(roles = {"ADMIN"})
//    void deleteDoctor_NotFound() throws Exception {
//        Mockito.doThrow(new UserNotFoundException("Doctor with ID 999L not found"))
//                .when(doctorService).deleteDoctor(999L);
//
//        mockMvc.perform(delete("/api/doctors/999"))
//                .andExpect(status().isNotFound());
//    }
//
//    /**
//     * Test case: Attempt to delete a doctor with a non-existent ID by a forbidden role.
//     * Verifies that the response status is 403 forbidden.
//     */
//    @Test
//    @DisplayName("Delete Doctor - Not Found - AccessForbidden")
//    @WithMockUser(roles = {"Patient"})
//    void deleteDoctor_NotFound_AccessForbidden() throws Exception {
//        Mockito.doThrow(new UserNotFoundException("Doctor with ID 999L not found"))
//                .when(doctorService).deleteDoctor(999L);
//
//        mockMvc.perform(delete("/api/doctors/999"))
//                .andExpect(status().isForbidden());
//    }
//
//
//    /**
//     * Test case: Attempt to update a partial doctor info ID by an ADMIN role.
//     * Verifies that the response status is 200 oK.
//     */
//    @Test
//    @DisplayName("Patch Doctor - Success")
//    @WithMockUser(roles = {"ADMIN"})
//    void patchDoctor_Success() throws Exception {
//        // Partial update data
//        Map<String, Object> updates = new HashMap<>();
//        updates.put("specialization", "Dermatology");
//
//        // Original doctor
//        Doctor originalDoctor = new Doctor(1L,  "john@example.com", "Dr. John", "Cardiology", "Mon-Fri 9AM-5PM");
//
//        // Updated doctor after patch
//        Doctor patchedDoctor = new Doctor(1L, "john@example.com", "Dr. John", "Dermatology", "Mon-Fri 9AM-5PM");
//
//        Mockito.when(doctorService.patchDoctor(eq(1L), any(Map.class))).thenReturn(Optional.of(patchedDoctor));
//
//        mockMvc.perform(patch("/api/doctors/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updates)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.speciality").value("Dermatology"))
//                .andExpect(jsonPath("$.name").value("Dr. John"));
//    }
//
//    /**
//     * Test case: Attempt to update a partial doctor info ID by a forbidden role.
//     * Verifies that the response status is 403 forbidden.
//     */
//    @Test
//    @DisplayName("Patch Doctor - AccessForbidden")
//    @WithMockUser(roles = {"PATIENT"})
//    void patchDoctor_aCCESSfORBIDDEN() throws Exception {
//        // Partial update data
//        Map<String, Object> updates = new HashMap<>();
//        updates.put("specialization", "Dermatology");
//
//        // Original doctor
//        Doctor originalDoctor = new Doctor(1L,  "john@example.com", "Dr. John", "Cardiology", "Mon-Fri 9AM-5PM");
//
//        // Updated doctor after patch
//        Doctor patchedDoctor = new Doctor(1L, "john@example.com", "Dr. John", "Dermatology", "Mon-Fri 9AM-5PM");
//
//        Mockito.when(doctorService.patchDoctor(eq(1L), any(Map.class))).thenReturn(Optional.of(patchedDoctor));
//
//        mockMvc.perform(patch("/api/doctors/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updates)))
//                .andExpect(status().isForbidden());
//    }
//
//
//    /**
//     * Test case: Attempt to update a partial doctor for a non existing doctor ID by an ADMIN role.
//     * Verifies that the response status is 404 not found.
//     */
//    @Test
//    @DisplayName("Patch Doctor - Not Found")
//    @WithMockUser(roles = {"ADMIN"})
//    void patchDoctor_NotFound() throws Exception {
//        Map<String, Object> updates = new HashMap<>();
//        updates.put("specialization", "Dermatology");
//
//        Mockito.when(doctorService.patchDoctor(eq(999L), any(Map.class))).thenReturn(Optional.empty());
//
//        mockMvc.perform(patch("/api/doctors/999")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updates)))
//                .andExpect(status().isNotFound());
//    }
//
//    /**
//     * Test case: Attempt to update a partial doctor for a non existing doctor ID by a forbidden role.
//     * Verifies that the response status is 403 forbidden.
//     */
//    @Test
//    @DisplayName("Patch Doctor - Not Found - Access Forbidden")
//    @WithMockUser(roles = {"PATIENT"})
//    void patchDoctor_NotFound_AccessForbidden() throws Exception {
//        Map<String, Object> updates = new HashMap<>();
//        updates.put("specialization", "Dermatology");
//
//        Mockito.when(doctorService.patchDoctor(eq(999L), any(Map.class))).thenReturn(Optional.empty());
//
//        mockMvc.perform(patch("/api/doctors/999")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updates)))
//                .andExpect(status().isForbidden());
//    }
//
//
//
//}
