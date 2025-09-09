package com.smarthealthcare.appointment.smarthealthcare_appointment.aop;

import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.PatientRepository;
import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.UserRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class AppointmentLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentLoggingAspect.class);
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public AppointmentLoggingAspect(UserRepository userRepository, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    // pointcut on booking method
    @AfterReturning(pointcut = "execution(* com.smarthealthcare..AppointmentService.bookAppointment(..))", returning = "appointment")
    public void logSuccessfulBooking(JoinPoint jp, Object appointment) {
        logger.info("Appointment booked: args={} result={}", jp.getArgs(), appointment);
    }

    // log exceptions (double booking)
    @AfterThrowing(pointcut = "execution(* com.smarthealthcare..AppointmentService.bookAppointment(..))", throwing = "ex")
    public void logBookingFailure(JoinPoint jp, Throwable ex) {
        // If it is double-booking we expect IllegalStateException from service
        if (ex instanceof IllegalStateException) {
            Object[] args = jp.getArgs();
            logger.warn("Double booking attempt prevented. args={}, message={}", args, ex.getMessage());
        } else {
            logger.error("Booking failed", ex);
        }
    }

    // Log when appointment is cancelled
    @After("execution(* com.smarthealthcare.appointment.smarthealthcare_appointment.service.AppointmentService.cancelAppointment(..))")
    public void logAfterAppointmentCancelled(JoinPoint joinPoint) {
        Long appointmentId = (Long) joinPoint.getArgs()[0];
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Cancel appointment: authentication = " + authentication);

        String username = authentication.getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Patient with userID " + userId + " not found!"));

        Long patientId = patient.getId();
        logger.info(" Appointment ID {} cancelled by Patient ID {}", appointmentId, patientId);
    }
}
