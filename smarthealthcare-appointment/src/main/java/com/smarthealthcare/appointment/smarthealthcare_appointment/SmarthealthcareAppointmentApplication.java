package com.smarthealthcare.appointment.smarthealthcare_appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/* TODO:
    - 1. Add validation information before saving to the database
    - 2. When inserting a user, insert it to the user table and its role table
 */
@SpringBootApplication
public class SmarthealthcareAppointmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmarthealthcareAppointmentApplication.class, args);
	}

}
