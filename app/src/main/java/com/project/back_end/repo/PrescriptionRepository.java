package com.project.back_end.repo;

import com.project.back_end.models.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PrescriptionRepository provides data access methods for Prescription entity.
 */
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    
    List<Prescription> findByDoctorId(Long doctorId);
    
    List<Prescription> findByPatientId(Long patientId);
    
    List<Prescription> findByAppointmentId(Long appointmentId);
}
