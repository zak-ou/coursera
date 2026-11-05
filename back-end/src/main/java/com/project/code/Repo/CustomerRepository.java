package com.project.code.Repo;

import com.project.code.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CustomerRepository provides data access methods for Customer entity.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Find customer by email address.
     * 
     * @param email The customer's email
     * @return Optional containing the customer if found
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Check if a customer exists by email.
     * 
     * @param email The customer's email
     * @return true if customer exists, false otherwise
     */
    boolean existsByEmail(String email);
}
