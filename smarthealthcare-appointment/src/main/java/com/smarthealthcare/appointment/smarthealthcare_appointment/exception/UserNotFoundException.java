package com.smarthealthcare.appointment.smarthealthcare_appointment.exception;

/**
 * UserNotFoundException is thrown when a user cannot be found in the database.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
