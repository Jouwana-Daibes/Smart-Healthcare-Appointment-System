package com.smarthealthcare.appointment.smarthealthcare_appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class SmarthealthcareAppointmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmarthealthcareAppointmentApplication.class, args);
	}

}
