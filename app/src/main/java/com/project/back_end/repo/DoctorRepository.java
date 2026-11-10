package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DoctorRepository provides data access methods for Doctor entity.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    Optional<Doctor> findByEmail(String email);
    
    List<Doctor> findBySpecialization(String specialization);
    
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Doctor> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT d FROM Doctor d WHERE d.specialization = :specialization AND d.availableTimes IS NOT NULL")
    List<Doctor> findBySpecializationAndAvailable(@Param("specialization") String specialization);
    
    boolean existsByEmail(String email);
}
