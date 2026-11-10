package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.PrescriptionRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PrescriptionController handles prescription-related API endpoints.
 * Manages creation, retrieval, and management of prescriptions.
 */
@RestController
@RequestMapping("/api/prescription")
@CrossOrigin(origins = "*")
public class PrescriptionController {
    
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private TokenService tokenService;
    
    /**
     * POST endpoint that saves a prescription with token validation and request body validation.
     * Returns structured success or error messages using ResponseEntity.
     * 
     * @param prescriptionRequest Map containing prescription details
     * @param token Authorization token from header
     * @return ResponseEntity with created prescription or error message
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPrescription(
            @Valid @RequestBody Map<String, Object> prescriptionRequest,
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
        
        // Validate request body
        if (!prescriptionRequest.containsKey("doctorId") || 
            !prescriptionRequest.containsKey("patientId") ||
            !prescriptionRequest.containsKey("medication") ||
            !prescriptionRequest.containsKey("dosage") ||
            !prescriptionRequest.containsKey("frequency") ||
            !prescriptionRequest.containsKey("duration")) {
            
            response.put("success", false);
            response.put("message", "Missing required fields: doctorId, patientId, medication, dosage, frequency, duration");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        try {
            // Extract prescription details
            Long doctorId = Long.valueOf(prescriptionRequest.get("doctorId").toString());
            Long patientId = Long.valueOf(prescriptionRequest.get("patientId").toString());
            String medication = prescriptionRequest.get("medication").toString();
            String dosage = prescriptionRequest.get("dosage").toString();
            String frequency = prescriptionRequest.get("frequency").toString();
            String duration = prescriptionRequest.get("duration").toString();
            String instructions = prescriptionRequest.containsKey("instructions") ? 
                    prescriptionRequest.get("instructions").toString() : null;
            
            // Validate doctor exists
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));
            
            // Validate patient exists
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));
            
            // Create and save prescription
            Prescription prescription = new Prescription(doctor, patient, medication, dosage, frequency, duration);
            prescription.setInstructions(instructions);
            
            Prescription saved = prescriptionRepository.save(prescription);
            
            response.put("success", true);
            response.put("message", "Prescription created successfully");
            response.put("prescriptionId", saved.getId());
            response.put("prescription", saved);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating prescription: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * GET endpoint to retrieve all prescriptions for a specific doctor.
     * 
     * @param doctorId Doctor ID
     * @return ResponseEntity with list of prescriptions
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Map<String, Object>> getPrescriptionsByDoctor(@PathVariable Long doctorId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByDoctorId(doctorId);
            
            response.put("success", true);
            response.put("count", prescriptions.size());
            response.put("prescriptions", prescriptions);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving prescriptions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * GET endpoint to retrieve all prescriptions for a specific patient.
     * 
     * @param patientId Patient ID
     * @return ResponseEntity with list of prescriptions
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getPrescriptionsByPatient(@PathVariable Long patientId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByPatientId(patientId);
            
            response.put("success", true);
            response.put("count", prescriptions.size());
            response.put("prescriptions", prescriptions);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving prescriptions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * GET endpoint to retrieve a specific prescription by ID.
     * 
     * @param id Prescription ID
     * @return ResponseEntity with prescription details
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPrescriptionById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        return prescriptionRepository.findById(id)
                .map(prescription -> {
                    response.put("success", true);
                    response.put("prescription", prescription);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("message", "Prescription not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
    
    /**
     * GET endpoint to retrieve all prescriptions.
     * 
     * @return ResponseEntity with all prescriptions
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllPrescriptions() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Prescription> prescriptions = prescriptionRepository.findAll();
            
            response.put("success", true);
            response.put("count", prescriptions.size());
            response.put("prescriptions", prescriptions);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving prescriptions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
