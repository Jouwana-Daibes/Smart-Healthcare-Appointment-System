package com.smarthealthcare.appointment.smarthealthcare_appointment.model;

/**
 * Enum representing the roles in the system which defines fixed roles used for role-based access control (RBAC).
 * - ADMIN: Can manage doctors and patients.
 * - DOCTOR: Can manage appointments & prescriptions.
 * - PATIENT: Can book/cancel appointments and view records.
 */
public enum Role {
    ADMIN,
    DOCTOR,
    PATIENT
}
