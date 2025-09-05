package com.smarthealthcare.appointment.smarthealthcare_appointment.exception;

/**
 * UserAlreadyExistsException is thrown when attempting to register a user
 * with a username that already exists in the database.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
