package com.smarthealthcare.appointment.smarthealthcare_appointment.aop;
import com.smarthealthcare.appointment.smarthealthcare_appointment.exception.UserNotFoundException;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Patient;
import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Prescription;
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
public class PrescriptionLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionLoggingAspect.class);

    // Match only updatePrescription(..) and patchPrescription(..)
    @Pointcut("execution(* com.smarthealthcare.appointment.smarthealthcare_appointment.service.PrescriptionService.updatePrescription(..)) || " +
            "execution(* com.smarthealthcare.appointment.smarthealthcare_appointment.service.PrescriptionService.patchPrescription(..))")
    public void prescriptionMethods() {}

    // Log after a prescription is added or updated
    @AfterReturning(
            pointcut = "prescriptionMethods()",
            returning = "result")
    public void logPrescriptionUpdate(JoinPoint joinPoint, Object result) {
        if (result instanceof Prescription prescription) {
            logger.info("Prescription updated: PatientId={}, Medicines={}, Notes={}",
                    prescription.getPatientId(),
                    prescription.getMedicines());
        }
    }

    // log exceptions during prescription operations
    @AfterThrowing(pointcut = "prescriptionMethods()", throwing = "ex")
    public void logPrescriptionErrors(JoinPoint joinPoint, Throwable ex) {
        logger.error("Error in method {}: {}", joinPoint.getSignature().getName(), ex.getMessage());
    }
}
