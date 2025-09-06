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
Authorization: Bearer <your-jwt-token>
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
---
 - Configure application.properties with your database credentials.

- Build & run:

   - mvn spring-boot:run

- Test endpoints using Postman or any REST client.


