package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Appointment entity representing scheduled appointments between doctors and patients.
 * 
 * This entity manages the booking system, linking doctors and patients with
 * specific appointment times and tracking the status of each appointment.
 */
@Entity
@Table(name = "appointment")
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Many-to-One relationship with Doctor.
     * Multiple appointments can belong to one doctor.
     * Uses LAZY fetching to optimize performance.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "Doctor is required")
    private Doctor doctor;
    
    /**
     * Many-to-One relationship with Patient.
     * Multiple appointments can belong to one patient.
     * Uses LAZY fetching to optimize performance.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient is required")
    private Patient patient;
    
    /**
     * Appointment date and time.
     * Uses LocalDateTime for precise timestamp representation.
     * Validated to ensure appointments are scheduled in the future.
     * This field has validation annotations:
     * - @NotNull: Ensures appointment time is always set
     * - @Future: Ensures appointment is scheduled for a future time (not in the past)
     */
    @Column(name = "appointment_time", nullable = false)
    @NotNull(message = "Appointment time is required")
    @Future(message = "Appointment must be scheduled for a future date and time")
    private LocalDateTime appointmentTime;
    
    /**
     * Status of the appointment.
     * Possible values: SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
     * Defaults to SCHEDULED when appointment is created.
     */
    @Column(name = "status", length = 20)
    private String status = "SCHEDULED";
    
    /**
     * Patient's reason for the appointment.
     * Optional field that helps the doctor prepare for the visit.
     */
    @Lob
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
    
    /**
     * Timestamp when the appointment was created (booked).
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the appointment was last updated.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    
    /**
     * Default no-argument constructor required by JPA.
     */
    public Appointment() {
    }
    
    /**
     * Constructor for creating a new appointment with essential fields.
     * 
     * @param doctor The doctor for this appointment
     * @param patient The patient for this appointment
     * @param appointmentTime The scheduled date and time
     */
    public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentTime) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.status = "SCHEDULED";
    }
    
    // Lifecycle Callbacks
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "SCHEDULED";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }
    
    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", appointmentTime=" + appointmentTime +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
