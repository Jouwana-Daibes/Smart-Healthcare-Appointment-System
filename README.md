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

---

## 6. Doctor Management

- **Entity:** `Doctor`
- **Repository, Service, Controller** implemented
- CRUD operations:
  - **POST /api/doctors** → ADMIN only
  - **PUT /api/doctors/{id}** → ADMIN only
  - **DELETE /api/doctors/{id}** → ADMIN only
  - **GET /api/doctors** → ADMIN, DOCTOR, PATIENT

---

## 7. API Endpoints

#### Authentication
| Method | Endpoint             | Roles Allowed | Description                 |
|--------|--------------------|---------------|-----------------------------|
| POST   | /api/auth/register   | PUBLIC        | Register a new user         |
| POST   | /api/auth/login      | PUBLIC        | Login and get JWT token     |

#### Doctor Management
| Method | Endpoint                | Roles Allowed          | Description                          |
|--------|------------------------|-----------------------|--------------------------------------|
| GET    | /api/doctors            | ADMIN, DOCTOR, PATIENT | Retrieve all doctors                 |
| POST   | /api/doctors            | ADMIN                 | Create a new doctor                  |
| PUT    | /api/doctors/{id}       | ADMIN                 | Update an existing doctor            |
| DELETE | /api/doctors/{id}       | ADMIN                 | Delete a doctor                       |

**Notes:**
- All requests except `/api/auth/**` require JWT authentication.
- Only `ADMIN` can modify doctor data, while `DOCTOR` and `PATIENT` can view doctor information.

---

## 8. Security

- JWT filter intercepts each request, validates the token, and sets the authentication in `SecurityContextHolder`.
- `@PreAuthorize` checks user roles against method access.
- Spring Security’s filter chain ensures authentication and authorization on all protected endpoints.

---

## 9. Future Improvements

- Patient appointment management
- Prescription management
- Logging & monitoring
- Unit and integration tests

---
## 10. Running the Project

1. Clone the repo:  
```bash
git clone https://github.com/Jouwana-Daibes/Smart-Healthcare-Appointment-System.git
```
---
 - Configure application.properties with your database credentials.

- Build & run:

   - mvn spring-boot:run

- Test endpoints using Postman.

## DoctorControllerTest

 - This module contains unit tests for the `DoctorController` REST API endpoints in the Smart Healthcare Appointment System.
 - The `DoctorControllerTest` class tests the **web layer** (controller) using `@WebMvcTest` without starting the full Spring Boot application context.  

- Key points:

  - **Mocked dependencies**: `DoctorService` is mocked using `@MockBean` to isolate the controller logic.  
  - **HTTP simulation**: `MockMvc` is used to simulate HTTP requests and verify responses.  
  - **JSON handling**: `ObjectMapper` is used to serialize request bodies and deserialize response bodies.  
  - **Security**: Endpoints can be secured with roles (e.g., `ADMIN`) and tested using `@WithMockUser`.

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

  ## Patient Management

The Patient Management module of the Smart Healthcare Appointment System includes the following functionalities:

### a. Admin Registers New Patients
- Only users with the `ADMIN` role can create/register new patients.
- Admin provides patient details: name, email.
- The system stores patient data in the database.

### b. Patients Update Their Personal Details
- Patients can update their own profile information.
- Security checks ensure that a patient can only update their own details, not other patients' data.
- Fields that can be updated include:
  - `name`
  - `email`
- All updates are saved to the database.




