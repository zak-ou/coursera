package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * AppointmentService handles business logic for appointment operations.
 * Provides methods for booking, retrieving, and managing appointments.
 */
@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    /**
     * Implements a booking method that saves an appointment.
     * Validates that doctor and patient exist before creating the appointment.
     * Checks if the time slot is available.
     * 
     * @param doctorId ID of the doctor
     * @param patientId ID of the patient
     * @param appointmentTime Date and time of the appointment
     * @param reason Reason for visit (optional)
     * @return Saved appointment entity
     * @throws RuntimeException if doctor/patient not found or slot not available
     */
    @Transactional
    public Appointment bookAppointment(Long doctorId, Long patientId, LocalDateTime appointmentTime, String reason) {
        // Validate doctor exists
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));
        
        // Validate patient exists
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));
        
        // Check if slot is available
        List<Appointment> existingAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentTime(doctorId, appointmentTime);
        
        if (!existingAppointments.isEmpty()) {
            throw new RuntimeException("This time slot is already booked");
        }
        
        // Create and save appointment
        Appointment appointment = new Appointment(doctor, patient, appointmentTime);
        appointment.setReason(reason);
        appointment.setStatus("SCHEDULED");
        
        return appointmentRepository.save(appointment);
    }
    
    /**
     * Defines a method to retrieve appointments for a specific doctor on a specific date.
     * Returns all appointments for the doctor on the given date, ordered by time.
     * 
     * @param doctorId The doctor's ID
     * @param date The date to retrieve appointments for
     * @return List of appointments for the doctor on the specified date
     */
    public List<Appointment> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndDate(doctorId, date);
    }
    
    /**
     * Retrieves all appointments for a specific doctor.
     * 
     * @param doctorId The doctor's ID
     * @return List of all appointments for the doctor
     */
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
    
    /**
     * Retrieves all appointments for a specific patient.
     * 
     * @param patientId The patient's ID
     * @return List of all appointments for the patient
     */
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentTimeDesc(patientId);
    }
    
    /**
     * Retrieves a single appointment by ID.
     * 
     * @param id Appointment ID
     * @return Optional containing appointment if found
     */
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }
    
    /**
     * Cancels an appointment by updating its status.
     * 
     * @param appointmentId The appointment ID to cancel
     * @return Updated appointment entity
     * @throws RuntimeException if appointment not found
     */
    @Transactional
    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));
        
        appointment.setStatus("CANCELLED");
        return appointmentRepository.save(appointment);
    }
    
    /**
     * Updates appointment status.
     * 
     * @param appointmentId The appointment ID
     * @param status New status (SCHEDULED, COMPLETED, CANCELLED, NO_SHOW)
     * @return Updated appointment entity
     */
    @Transactional
    public Appointment updateAppointmentStatus(Long appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));
        
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
    
    /**
     * Retrieves all appointments with a specific status.
     * 
     * @param status The status to filter by
     * @return List of appointments with the specified status
     */
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }
    
    /**
     * Deletes an appointment.
     * 
     * @param appointmentId The appointment ID to delete
     */
    @Transactional
    public void deleteAppointment(Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }
}
