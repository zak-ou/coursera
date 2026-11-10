package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * PatientRepository provides data access methods for Patient entity.
 * 
 * This repository extends JpaRepository to provide standard CRUD operations
 * and defines custom query methods for patient retrieval.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    /**
     * Retrieves a patient by their email address using a derived query method.
     * Spring Data JPA automatically generates the implementation based on method name.
     * 
     * @param email The patient's email address
     * @return Optional containing the patient if found, empty otherwise
     */
    Optional<Patient> findByEmail(String email);
    
    /**
     * Retrieves a patient using either email or phone number.
     * This custom query allows flexible patient lookup by multiple contact methods.
     * Uses @Query annotation with JPQL to define custom search logic.
     * 
     * @param email The patient's email address
     * @param phone The patient's phone number
     * @return Optional containing the patient if found by either email or phone
     */
    @Query("SELECT p FROM Patient p WHERE p.email = :email OR p.phone = :phone")
    Optional<Patient> findByEmailOrPhone(@Param("email") String email, @Param("phone") String phone);
    
    /**
     * Alternative derived query method to find patient by phone number.
     * 
     * @param phone The patient's phone number
     * @return Optional containing the patient if found
     */
    Optional<Patient> findByPhone(String phone);
    
    /**
     * Checks if a patient exists with the given email.
     * Useful for validation during registration.
     * 
     * @param email The email to check
     * @return true if patient exists with this email, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Checks if a patient exists with the given phone number.
     * 
     * @param phone The phone number to check
     * @return true if patient exists with this phone, false otherwise
     */
    boolean existsByPhone(String phone);
}
