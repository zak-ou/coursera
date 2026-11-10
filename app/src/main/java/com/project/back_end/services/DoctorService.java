package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DoctorService handles business logic for doctor-related operations.
 */
@Service
public class DoctorService {
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    /**
     * Returns available time slots for a doctor on a given date.
     * Filters out already-booked slots and returns only available times.
     * 
     * @param doctorId The doctor's ID
     * @param date The date to check availability
     * @return List of available time slots as strings (e.g., "09:00", "10:00")
     */
    public List<String> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        
        if (!doctorOpt.isPresent()) {
            return Collections.emptyList();
        }
        
        Doctor doctor = doctorOpt.get();
        String availableTimes = doctor.getAvailableTimes();
        
        if (availableTimes == null || availableTimes.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Get day of week from date
        String dayOfWeek = date.getDayOfWeek().toString();
        
        // Parse available times (simplified - assumes format like "09:00-17:00")
        List<String> allSlots = generateTimeSlots("09:00", "17:00");
        
        // Get already booked appointments for this date
        List<Appointment> bookedAppointments = appointmentRepository.findByDoctorIdAndDate(doctorId, date);
        
        Set<String> bookedTimes = bookedAppointments.stream()
                .map(apt -> apt.getAppointmentTime().toLocalTime().toString().substring(0, 5))
                .collect(Collectors.toSet());
        
        // Filter out booked times
        return allSlots.stream()
                .filter(slot -> !bookedTimes.contains(slot))
                .collect(Collectors.toList());
    }
    
    /**
     * Generates hourly time slots between start and end times.
     * 
     * @param startTime Start time (e.g., "09:00")
     * @param endTime End time (e.g., "17:00")
     * @return List of time slots
     */
    private List<String> generateTimeSlots(String startTime, String endTime) {
        List<String> slots = new ArrayList<>();
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        
        LocalTime current = start;
        while (current.isBefore(end)) {
            slots.add(current.toString());
            current = current.plusHours(1);
        }
        
        return slots;
    }
    
    /**
     * Validates doctor login credentials and returns a structured response.
     * Checks if doctor exists and password matches.
     * 
     * @param email Doctor's email
     * @param password Doctor's password
     * @return Map containing validation result and doctor data if successful
     */
    public Map<String, Object> validateLogin(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        
        if (!doctorOpt.isPresent()) {
            response.put("success", false);
            response.put("message", "Doctor not found with email: " + email);
            return response;
        }
        
        Doctor doctor = doctorOpt.get();
        
        // Check password (assuming passwords are hashed with BCrypt)
        if (passwordEncoder.matches(password, doctor.getPassword())) {
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("doctorId", doctor.getId());
            response.put("name", doctor.getName());
            response.put("email", doctor.getEmail());
            response.put("specialization", doctor.getSpecialization());
        } else {
            response.put("success", false);
            response.put("message", "Invalid password");
        }
        
        return response;
    }
    
    /**
     * Retrieves all doctors by specialization.
     * 
     * @param specialization Medical specialization
     * @return List of doctors with the specified specialization
     */
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }
    
    /**
     * Searches doctors by name (partial match).
     * 
     * @param name Doctor name or partial name
     * @return List of matching doctors
     */
    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepository.findByNameContaining(name);
    }
    
    /**
     * Retrieves a doctor by ID.
     * 
     * @param id Doctor ID
     * @return Optional containing doctor if found
     */
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }
    
    /**
     * Creates a new doctor account.
     * 
     * @param doctor Doctor entity to create
     * @return Saved doctor entity
     */
    public Doctor createDoctor(Doctor doctor) {
        // Hash password before saving
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);
    }
    
    /**
     * Updates doctor information.
     * 
     * @param id Doctor ID
     * @param doctor Updated doctor information
     * @return Updated doctor entity
     */
    public Doctor updateDoctor(Long id, Doctor doctor) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        existing.setName(doctor.getName());
        existing.setPhone(doctor.getPhone());
        existing.setSpecialization(doctor.getSpecialization());
        existing.setAvailableTimes(doctor.getAvailableTimes());
        
        // Only update password if provided
        if (doctor.getPassword() != null && !doctor.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(doctor.getPassword()));
        }
        
        return doctorRepository.save(existing);
    }
    
    /**
     * Deletes a doctor by ID.
     * 
     * @param id Doctor ID
     */
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
    
    /**
     * Retrieves all doctors.
     * 
     * @return List of all doctors
     */
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}
