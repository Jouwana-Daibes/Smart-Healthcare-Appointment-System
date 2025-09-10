# Smart Healthcare Appointment System

A Spring Boot 3 application to manage patients, doctors, appointments, prescriptions, and medical records. Integrates Spring concepts (IoC, DI, AOP), security, relational + NoSQL databases, caching and unit testing.

## 1. Features

- User authentication with JWT (login/register)
- Role-based access: ADMIN, DOCTOR, PATIENT
- Doctor management (CRUD) by ADMIN
- GET endpoints for doctors accessible by DOCTOR and PATIENT
- Secure API endpoints with JWT filter
- Passwords stored with BCrypt hashing
- Request validation and global exception handling

---

## 2. Technologies Used

- Java 17+
- Spring Boot 3
- Spring Security
- Spring Data JPA
- MySQL 
- JWT for authentication
- Maven

---

## 3. Project Structure
```
com.smarthealthcare.appointment
├── config # Security, JWT filter
├── controller # REST controllers
├── model # Entities (User, Doctor, etc.)
├── repository # Spring Data JPA repositories
├── service # Business logic services
├── exception # Custom exceptions and global handler
└── SmartHealthcareApplication.java
```

---

## 4. User Roles

- **ADMIN:** Can manage doctors (add, update, delete), view all data
- **DOCTOR:** Can view doctor information, manage personal appointments
- **PATIENT:** Can view doctor information, book appointments

---

## 5. Authentication

- Registration: `/api/auth/register` (PUBLIC)
- Login: `/api/auth/login` (PUBLIC)
- JWT tokens required for all other endpoints
- Include JWT in `Authorization` header:
### API Endpoints
| Method | Endpoint             | Roles Allowed | Description                 |
|--------|--------------------|---------------|-----------------------------|
| POST   | /api/auth/register   | PUBLIC        | Register a new user         |
| POST   | /api/auth/login      | PUBLIC        | Login and get JWT token     |

---
## 6. Security

- JWT filter intercepts each request, validates the token, and sets the authentication in `SecurityContextHolder`.
- `@PreAuthorize` checks user roles against method access.
- Spring Security’s filter chain ensures authentication and authorization on all protected endpoints.

---

**Notes:**
- All requests except `/api/auth/**` require JWT authentication.
- Only `ADMIN` can modify doctor data, while `DOCTOR` and `PATIENT` can view doctor information.

---

## Doctor Management

- **Entity:** `Doctor`
- **Repository, Service, Controller** implemented
- CRUD operations:
  - **POST /api/doctors** → ADMIN only
  - **PUT /api/doctors/{id}** → ADMIN only
  - **DELETE /api/doctors/{id}** → ADMIN only
  - **GET /api/doctors** → ADMIN, DOCTOR, PATIENT

---
  ## Patient Management

The Patient Management module of the Smart Healthcare Appointment System includes the following functionalities:

### a. Admin Registers New Patients
- Only users with the `ADMIN` role can create/register new patients.
- Admin provides patient details: name, email.
- The system stores patient data in the database.

### b. Patients Update Their Personal Details
- Patients can update their own profile information.
- Security checks ensure that a patient can only update their own details, by checking if the authenticated logged in user id is the same as patient id using AuthenticationManager.  
- Fields that can be updated include:
  - `name`
  - `email`
- All updates are saved to the database.
---

## Appointment Management

The **Appointment Management** module handles all operations related to doctor-patient appointments, ensuring smooth scheduling and avoiding conflicts.  

### Features
- **Book Appointments:** Patients can request appointments with a specific doctor at a chosen date and time.  
- **Prevent Double-Booking:** The system checks doctor availability and prevents scheduling conflicts.  
- **View Appointments:** Doctors can see their appointments for today or its all apointments.  
- **Role-Based Access:** Only authorized users (patients and doctors) can perform appointment actions based on their authenticated user id to nly access their data.  

### Implementation
1. **Database**  
   - **MySQL** is used to store appointment records.  
   - Each appointment includes: `appointmentId`, `doctorId`, `patientId`, `startTime`, `endTime`, `availableDays`, and `status`.

2. **Service Layer**  
   - Validates appointment requests to **ensure no overlapping time slots**.
   - Validates appointments is within the doctor scheduele time.  
   - Handles business logic for retrieval of appointments.  

3. **Controller Layer**  
   - REST APIs allow patients to **book and view appointments**.  
   - Doctors can **retrieve their schedule** and update the status of appointments.  

4. **Validation & Error Handling**  
   - Prevents double-booking with clear error messages.  
   - Returns meaningful responses when doctors or patients are not available.  

5. **Testing**  
   - Unit tests cover scenarios like double-booking.  

### Example API Endpoints
- `POST /appointments` – Book a new appointment  
- `GET /appointments/doctor/{id}` – Get all appointments for a doctor  
- `PUT /appointments/{id}/status` – Update appointment status  

### Benefits
- Ensures doctors’ time slots are efficiently managed.  
- Improves patient experience by preventing scheduling conflicts.  
- Provides real-time updates for both doctors and patients.  
---
## Prescription Management

The **Prescription Management** module allows doctors to create prescriptions for patients and enables patients to access them securely.  

### Features
- **Create Prescriptions:** Doctors can add prescriptions during appointments, including medicines.  
- **View Prescriptions:** Patients can securely view their prescriptions in their portal.  
- **Link to Appointments:** Each prescription is associated with a specific appointment, doctor, and patient.  
- **Role-Based Access:** Only authorized doctors can create prescriptions, and patients can only view their own prescriptions.  

### Implementation
1. **Database**  
   - **MongoDB** is used to store prescription documents for flexible and scalable storage.  
   - Each prescription contains: `prescriptionId`, `appointmentId`, `doctorId`, `patientId`, `medicines`, and `instructions`.

2. **Service Layer**  
   - Handles validation to ensure prescriptions are only created for valid appointments.  
   - Manages retrieval of prescriptions for patients and doctors.

3. **Controller Layer**  
   - REST APIs allow doctors to create prescriptions and patients to view them.  
   - Secure endpoints are protected using JWT authentication.

4. **Validation & Error Handling**  
   - Prevents creating prescriptions for non-existent appointments.  
   - Returns meaningful messages when unauthorized access is attempted.  

5. **Testing**  
   - Unit tests ensure prescriptions are correctly created, retrieved, and linked to appointments.  

### Example API Endpoints
- `POST /prescriptions` – Create a new prescription  
- `GET /prescriptions/patient/{id}` – Get all prescriptions for a patient  
- `GET /prescriptions/appointment/{id}` – Get prescription for a specific appointment  

### Benefits
- Centralized storage ensures prescriptions are consistent and easily accessible.  
- Patients can securely access their prescriptions online.  
- Doctors can efficiently manage and track patient medications.
---

## Unit Testing
- Test endpoints using Postman.
- Key points:

  - **Mocked dependencies**: `DoctorService` is mocked using `@MockBean` to isolate the controller logic.  
  - **HTTP simulation**: `MockMvc` is used to simulate HTTP requests and verify responses.  
  - **JSON handling**: `ObjectMapper` is used to serialize request bodies and deserialize response bodies.  
  - **Security**: Endpoints can be secured with roles (e.g., `ADMIN`) and tested using `@WithMockUser`.

## DoctorControllerTest

 - This module contains unit tests for the `DoctorController` REST API endpoints in the Smart Healthcare Appointment System.
 - The `DoctorControllerTest` class tests the **web layer** (controller) using `@WebMvcTest` without starting the full Spring Boot application context.  

### Tested Operations

The following CRUD operations are covered:

#### CREATE
- **Success**: Verifies that a doctor can be created and response status is `200 OK`.  
- **Duplicate Email**: Verifies that creating a doctor with an existing email returns `400 Bad Request`.  

#### READ
- **Get All Doctors - Success**: Returns a list of doctors with response status `200 OK`.  
- **Get All Doctors - Empty List**: Returns an empty list with response status `200 OK`.  
- **Get Doctor By ID - Success**: Returns the doctor details for a valid ID with response status `200 OK`.  
- **Get Doctor By ID - Not Found**: Returns `404 Not Found` for a non-existent doctor ID.  

#### UPDATE
- **Update Doctor - Success**: Updates an existing doctor and returns the updated data with `200 OK`.  
- **Update Doctor - Not Found**: Returns `404 Not Found` when updating a non-existent doctor.  

#### DELETE
- **Delete Doctor - Success**: Deletes a doctor with response status `204 No Content`.  
- **Delete Doctor - Not Found**: Returns `404 Not Found` when deleting a non-existent doctor.

### Security Testing

- Endpoints can be restricted to certain roles using Spring Security annotations (e.g., `@PreAuthorize("hasRole('ADMIN')")`).  
- Role-based access is tested using the `@WithMockUser(roles = {"ADMIN"})` annotation in tests.  
- Mocked `JwtUtil` and `CustomUserDetailsService` are used to bypass real authentication while testing role restrictions.

### Notes

- These tests focus on **controller behavior** and **HTTP responses**.  
- Business logic should be tested separately in service-layer unit tests.  
- Mocked dependencies ensure isolation and reproducibility of tests.

## 1. PatientController Testing

### Endpoints Tested
- `PUT /api/patients/{id}` → Update patient info
- `PATCH /api/patients/{id}` → Partial update
- `GET /api/patients/{id}` → Get patient by ID
- `GET /api/patients` → Get all patients
- `DELETE /api/patients/{id}` → Delete patient
- `GET /api/patients/MyRecords` → Get patient medical records

### Key Test Cases
1. **Update Patient Info (PUT)**
   - Update own profile as `PATIENT` → **Success**
   - Update another patient's profile → **403 Forbidden**
   - Admin updates any patient → **Success**

2. **Patch Patient (PATCH)**
   - Patch own profile → **Success**
   - Patch another patient → **403 Forbidden**
   - Mock `EntityMapper.toPatientDTO()` to avoid `NullPointerException`.

3. **Get Patient By ID**
   - Existing patient → **200 OK**
   - Non-existent patient → **500 Internal Server Error**

4. **Delete Patient**
   - Deletion by `ADMIN` → **204 No Content**
   - Deletion by `PATIENT` → **403 Forbidden**
   - Deletes associated prescriptions, appointments, and medical records.

5. **Get My Records**
   - Existing patient → **200 OK**
   - Non-existent patient → **500 Internal Server Error**

### Common Issues & Fixes
- **403 Forbidden**: Ensure `@WithMockUser` role and username match the mocked user.
---

## 2. MedicalRecordController Testing

### Endpoints Tested
- `POST /api/doctors/records/{doctorId}` → Add medical record

### Key Test Cases
- Success → **200 OK**, validate JSON fields
- Doctor not found → **500 Internal Server Error**
- Invalid input → **400 Bad Request**
- Empty prescriptions/lab reports → **200 OK**, validate empty lists

### Fixes
- Use `any(MedicalRecordRequestDTO.class)` in `when()` for `medicalRecordService.addRecord()`.
- Match `doctorId` using `eq()` to avoid Mockito issues.
- Use `@WithMockUser(roles = {"DOCTOR"})` for authentication.

---

## 3. AppointmentService Testing (Double Booking)

### Scenario
Prevent overlapping appointments for the same doctor.

### Key Test Case
- Booking an appointment overlapping an existing one → **IllegalArgumentException: "Oops! Time slot already taken"**

### Common Pitfalls
- `"Oops! Doctor is not available at this time"` occurs if appointment times do not match doctor's start/end time.
- Ensure **appointment time is within the doctor's schedule**.

```java
doctor.setStartTime(LocalDateTime.of(2025, 9, 11, 9, 0));
doctor.setEndTime(LocalDateTime.of(2025, 9, 11, 17, 0));
doctor.setAvailableDays("MON"); // Day of the week
---
## Running the Project

1. Clone the repo:  
```bash
git clone https://github.com/Jouwana-Daibes/Smart-Healthcare-Appointment-System.git
```
---
 - Configure application.properties with your database credentials.

- Build & run:

   - mvn spring-boot:run
- Test endpoints using Postman.

