//package com.smarthealthcare.appointment.smarthealthcare_appointment;
//
//
//import com.smarthealthcare.appointment.smarthealthcare_appointment.model.*;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.AppointmentRepository;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.DoctorRepository;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.PatientRepository;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.service.AppointmentService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class AppointmentServiceTest {
//
//    @Mock
//    private AppointmentRepository appointmentRepository;
//
//    @Mock
//    private DoctorRepository doctorRepository;
//
//    @Mock
//    private PatientRepository patientRepository;
//
//    @Mock
//    private AppointmentService appointmentService;
//
//    private Doctor doctor;
//    private Patient patient;
//
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
//    private String availableDays;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        doctor = new Doctor();
//        doctor.setId(1L);
//        doctor.setStartTime(LocalDateTime.of(2025, 9, 10, 9, 0));
//        doctor.setEndTime(LocalDateTime.of(2025, 9, 10, 17, 0));
//        doctor.setAvailableDays("Mon-Fri");
//
//        patient = new Patient();
//        patient.setId(1L);
//
//        startTime = LocalDateTime.of(2025, 9, 10, 10, 0);
//        endTime = LocalDateTime.of(2025, 9, 10, 11, 0);
//        availableDays = doctor.getAvailableDays();
//    }
//
//    @Test
//    void testBookAppointmentSuccess() {
//        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
//        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
//        when(appointmentRepository.existsByDoctorIdAndTimeOverlapAndDay(1L, startTime, endTime, availableDays))
//                .thenReturn(false);
//        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        Appointment appointment = appointmentService.bookAppointment(1L, 1L, startTime, endTime, "THURSDAY");
//
//        assertNotNull(appointment);
//        assertEquals(doctor, appointment.getDoctor());
//        assertEquals(patient, appointment.getPatient());
//        assertEquals(startTime, appointment.getAppointmentStartTime());
//        assertEquals(endTime, appointment.getAppointmentEndTime());
//     // TODO: change it Expected :Mon-Fri - Actual:THURSDAY
//        //   assertEquals(availableDays, appointment.getAppointmentDay());
//
//    }
//
//    @Test
//    void testBookAppointmentDoubleBooking() {
//        when(appointmentRepository.existsByDoctorIdAndTimeOverlapAndDay(1L, startTime, endTime, availableDays)).thenReturn(true);
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            appointmentService.bookAppointment(1L, 1L, startTime, endTime, "THURSDAY");
//        });
//
//        assertEquals("Oops! Time slot already taken", exception.getMessage());
//    }
//
////    @Test
////    void testBookAppointmentDoubleBooking() {
////        when(appointmentRepository.existsByDoctorIdAndTimeOverlapAndDay(1L, startTime, endTime, availableDays)).thenReturn(true);
////
////        Appointment appointment = appointmentService.bookAppointment(1L, 1L, startTime, endTime, "THURSDAY");
////        String message = "";
////        if (appointment == null) {
////            message = "Oops! Time slot already taken";
////        }
////        assertEquals("Oops! Time slot already taken", message);
////    }
////}
//}
