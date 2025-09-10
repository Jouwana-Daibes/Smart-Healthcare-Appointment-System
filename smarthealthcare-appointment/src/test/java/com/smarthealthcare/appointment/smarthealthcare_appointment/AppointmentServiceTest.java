package com.smarthealthcare.appointment.smarthealthcare_appointment;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.User;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.AppointmentRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.DoctorRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.PatientRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentService appointmentService; // let Mockito inject mocks

    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // initialize mocks

        // Doctor setup
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Emily");
        doctor.setStartTime(LocalDateTime.of(2025, 9, 11, 9, 0));
        doctor.setEndTime(LocalDateTime.of(2025, 9, 11, 17, 0));

        doctor.setAvailableDays("MON-FRI");

        // Patient setup
        patient = new Patient();
        patient.setId(1L);
        var user = new User();
        user.setId(100L);
        user.setUsername("patientUser");
        patient.setUser(user);
    }

    @Test
    void testDoubleBookingThrowsException() {
        LocalDateTime requestedStart = LocalDateTime.of(2025, 9, 11, 10, 0);
        LocalDateTime requestedEnd = LocalDateTime.of(2025, 9, 11, 11, 0);
        String day = DayOfWeek.MONDAY.toString();

        // Mocks
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.existsByDoctorIdAndTimeOverlapAndDay(1L, requestedStart, requestedEnd, day))
                .thenReturn(true);

        // Execute & assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.bookAppointment(1L, 1L, requestedStart, requestedEnd, day);
        });

        assertEquals("Oops! Time slot already taken", ex.getMessage());

        // Ensure no appointment is saved
        verify(appointmentRepository, never()).save(any());
    }
}
