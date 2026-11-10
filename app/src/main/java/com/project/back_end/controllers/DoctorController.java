package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DoctorController handles doctor-related API endpoints.
 * Provides endpoints for doctor management, authentication, and availability checking.
 */
@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "*")
public class DoctorController {
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private TokenService tokenService;
    
    /**
     * GET endpoint for retrieving doctor availability using dynamic parameters.
     * Validates the token and returns a structured response using ResponseEntity.
     * 
     * @param doctorId The doctor's ID
     * @param date The date to check availability (format: yyyy-MM-dd)
     * @param token Authorization token from header
     * @return ResponseEntity with available time slots or error message
     */
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable Long doctorId,
            @RequestParam String date,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Validate token
        if (token == null || !token.startsWith("Bearer ")) {
            response.put("success", false);
            response.put("message", "Authorization token is required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        String jwtToken = token.substring(7);
        if (!tokenService.validateToken(jwtToken)) {
            response.put("success", false);
            response.put("message", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        try {
            LocalDate queryDate = LocalDate.parse(date);
            List<String> availableSlots = doctorService.getAvailableTimeSlots(doctorId, queryDate);
            
            response.put("success", true);
            response.put("doctorId", doctorId);
            response.put("date", date);
            response.put("availableSlots", availableSlots);
            response.put("totalSlotsAvailable", availableSlots.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving availability: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * POST endpoint for doctor login.
     * 
     * @param loginRequest Map containing email and password
     * @return ResponseEntity with login result and JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        
        Map<String, Object> response = doctorService.validateLogin(email, password);
        
        if ((Boolean) response.get("success")) {
            String token = tokenService.generateToken(email, "DOCTOR");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    /**
     * GET endpoint to retrieve all doctors.
     * 
     * @return ResponseEntity with list of all doctors
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllDoctors() {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorService.getAllDoctors();
        
        response.put("success", true);
        response.put("count", doctors.size());
        response.put("doctors", doctors);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET endpoint to search doctors by name.
     * 
     * @param name Doctor name or partial name
     * @return ResponseEntity with matching doctors
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchDoctors(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorService.searchDoctorsByName(name);
        
        response.put("success", true);
        response.put("count", doctors.size());
        response.put("doctors", doctors);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET endpoint to filter doctors by specialization and optional time availability.
     * 
     * @param specialization Medical specialization
     * @param time Optional time parameter
     * @return ResponseEntity with filtered doctors
     */
    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterDoctors(
            @RequestParam String specialization,
            @RequestParam(required = false) String time) {
        
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
        
        response.put("success", true);
        response.put("specialization", specialization);
        response.put("count", doctors.size());
        response.put("doctors", doctors);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET endpoint to retrieve a single doctor by ID.
     * 
     * @param id Doctor ID
     * @return ResponseEntity with doctor details
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDoctorById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        return doctorService.getDoctorById(id)
                .map(doctor -> {
                    response.put("success", true);
                    response.put("doctor", doctor);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("message", "Doctor not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
    
    /**
     * POST endpoint to create a new doctor.
     * 
     * @param doctor Doctor entity
     * @return ResponseEntity with created doctor
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createDoctor(@RequestBody Doctor doctor) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Doctor created = doctorService.createDoctor(doctor);
            response.put("success", true);
            response.put("message", "Doctor created successfully");
            response.put("doctor", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating doctor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * PUT endpoint to update doctor information.
     * 
     * @param id Doctor ID
     * @param doctor Updated doctor information
     * @return ResponseEntity with updated doctor
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Doctor updated = doctorService.updateDoctor(id, doctor);
            response.put("success", true);
            response.put("message", "Doctor updated successfully");
            response.put("doctor", updated);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * DELETE endpoint to remove a doctor.
     * 
     * @param id Doctor ID
     * @return ResponseEntity with deletion result
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDoctor(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            doctorService.deleteDoctor(id);
            response.put("success", true);
            response.put("message", "Doctor deleted successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting doctor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
