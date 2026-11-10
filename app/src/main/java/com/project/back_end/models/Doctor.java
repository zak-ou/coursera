package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Doctor entity representing medical professionals in the Smart Clinic Management System.
 * 
 * This JPA entity maps to the 'doctor' table in the database and contains
 * doctor profile information, credentials, and availability data.
 */
@Entity
@Table(name = "doctor")
public class Doctor {
    
    /**
     * Primary key identifier for the doctor.
     * Auto-generated using IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Doctor's full name.
     * Required field with maximum length of 100 characters.
     */
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    /**
     * Doctor's email address for authentication and communication.
     * Must be unique across all doctors and follow valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;
    
    /**
     * Hashed password for authentication.
     * Stored as bcrypt hash, never in plain text.
     */
    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false)
    private String password;
    
    /**
     * Doctor's contact phone number.
     */
    @Column(name = "phone", length = 20)
    private String phone;
    
    /**
     * Medical specialization of the doctor.
     * Examples: "Cardiology", "Pediatrics", "Dermatology", "Orthopedics"
     */
    @NotBlank(message = "Specialization is required")
    @Column(name = "specialization", nullable = false, length = 100)
    private String specialization;
    
    /**
     * Available time slots for appointments.
     * Stored as TEXT field containing JSON string with schedule information.
     * 
     * Format example:
     * {
     *   "Monday": ["09:00-12:00", "14:00-17:00"],
     *   "Tuesday": ["08:00-12:00"],
     *   "Wednesday": ["09:00-12:00", "13:00-16:00"]
     * }
     * 
     * This field uses @Lob annotation to support large text storage
     * and @Column with columnDefinition = "TEXT" to ensure proper database mapping.
     */
    @Lob
    @Column(name = "available_times", columnDefinition = "TEXT")
    private String availableTimes;
    
    /**
     * Timestamp when the doctor record was created.
     * Automatically set on entity creation.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the doctor record was last updated.
     * Automatically updated on entity modification.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    
    /**
     * Default no-argument constructor required by JPA.
     */
    public Doctor() {
    }
    
    /**
     * Constructor with essential fields for creating a new doctor.
     * 
     * @param name Doctor's full name
     * @param email Doctor's email address
     * @param password Hashed password
     * @param specialization Medical specialization
     */
    public Doctor(String name, String email, String password, String specialization) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.specialization = specialization;
    }
    
    // Lifecycle Callbacks
    
    /**
     * Sets the creation timestamp before persisting to database.
     * Called automatically by JPA before INSERT operation.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Updates the modification timestamp before updating database.
     * Called automatically by JPA before UPDATE operation.
     */
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getAvailableTimes() {
        return availableTimes;
    }
    
    public void setAvailableTimes(String availableTimes) {
        this.availableTimes = availableTimes;
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
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
