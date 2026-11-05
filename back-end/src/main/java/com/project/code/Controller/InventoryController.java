package com.project.code.Controller;

import com.project.code.Model.Inventory;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * InventoryController handles inventory-related API endpoints.
 * Includes filtering, validation, and inventory management functionality.
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private ServiceClass serviceClass;
    
    /**
     * GET endpoint to filter inventory by category, product name, and store.
     * Uses conditional logic to filter results based on provided parameters.
     * Includes input validation to ensure parameters are not empty.
     * 
     * @param category The product category to filter by
     * @param name The product name to filter by (can be partial match)
     * @param storeId The store ID to filter by
     * @return ResponseEntity with filtered inventory list
     *         - 200 OK with filtered results
     *         - 400 BAD REQUEST if parameters are invalid
     */
    @GetMapping("/filter/{category}/{name}/{storeId}")
    public ResponseEntity<Map<String, Object>> filterInventory(
            @PathVariable String category,
            @PathVariable String name,
            @PathVariable Long storeId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate input parameters
            if (category == null || category.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Category parameter is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            if (name == null || name.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Name parameter is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            if (storeId == null || storeId <= 0) {
                response.put("success", false);
                response.put("message", "Valid store ID is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Get all inventory for the specified store
            List<Inventory> inventoryList = inventoryRepository.findByStoreId(storeId);
            
            // Apply conditional filtering based on category and name
            List<Inventory> filteredInventory = inventoryList.stream()
                    .filter(inv -> {
                        boolean categoryMatch = inv.getProduct().getCategory() != null &&
                                inv.getProduct().getCategory().equalsIgnoreCase(category);
                        boolean nameMatch = inv.getProduct().getName() != null &&
                                inv.getProduct().getName().toLowerCase().contains(name.toLowerCase());
                        return categoryMatch && nameMatch;
                    })
                    .collect(Collectors.toList());
            
            response.put("success", true);
            response.put("count", filteredInventory.size());
            response.put("inventory", filteredInventory);
            response.put("filters", Map.of(
                    "category", category,
                    "name", name,
                    "storeId", storeId
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error filtering inventory: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * GET endpoint to validate if sufficient quantity exists in inventory.
     * Validates the requested quantity against available inventory.
     * Returns clear responses indicating whether the quantity is available.
     * 
     * @param quantity The quantity to validate
     * @param storeId The store ID to check
     * @param productId The product ID to check
     * @return ResponseEntity with validation result
     *         - 200 OK if quantity is available
     *         - 400 BAD REQUEST if quantity is insufficient or invalid parameters
     *         - 404 NOT FOUND if inventory entry doesn't exist
     */
    @GetMapping("/validate/{quantity}/{storeId}/{productId}")
    public ResponseEntity<Map<String, Object>> validateInventoryQuantity(
            @PathVariable Integer quantity,
            @PathVariable Long storeId,
            @PathVariable Long productId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate input parameters
            if (quantity == null || quantity <= 0) {
                response.put("valid", false);
                response.put("message", "Quantity must be greater than zero");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            if (storeId == null || storeId <= 0) {
                response.put("valid", false);
                response.put("message", "Valid store ID is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            if (productId == null || productId <= 0) {
                response.put("valid", false);
                response.put("message", "Valid product ID is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Check if inventory exists
            boolean inventoryExists = serviceClass.validateInventory(productId, storeId);
            
            if (!inventoryExists) {
                response.put("valid", false);
                response.put("message", "Inventory not found for the specified product and store");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // Get the inventory entry
            Inventory inventory = serviceClass.getInventoryId(productId, storeId);
            
            // Validate quantity against available stock
            if (inventory.getQuantity() >= quantity) {
                response.put("valid", true);
                response.put("message", "Sufficient quantity available");
                response.put("requestedQuantity", quantity);
                response.put("availableQuantity", inventory.getQuantity());
                return ResponseEntity.ok(response);
            } else {
                response.put("valid", false);
                response.put("message", "Insufficient quantity in inventory");
                response.put("requestedQuantity", quantity);
                response.put("availableQuantity", inventory.getQuantity());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", "Error validating inventory: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * GET endpoint to retrieve all inventory items.
     * 
     * @return ResponseEntity with list of all inventory
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllInventory() {
        Map<String, Object> response = new HashMap<>();
        response.put("inventory", inventoryRepository.findAll());
        return ResponseEntity.ok(response);
    }
}
