package com.smarthealthcare.appointment.smarthealthcare_appointment.repository;

import com.smarthealthcare.appointment.smarthealthcare_appointment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 * Communicates with the database for User CRUD operations by Providing them using JPA.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // Check if a user with the same username and roles already exists
  //  boolean existsByUsernameAndRoles(String username, String roles);
}
