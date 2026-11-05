package com.project.code.Controller;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import com.project.code.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * StoreController handles store-related API endpoints.
 * Includes store validation and order placement functionality.
 */
@RestController
@RequestMapping("/api/store")
public class StoreController {
    
    @Autowired
    private StoreRepository storeRepository;
    
    @Autowired
    private OrderService orderService;
    
    /**
     * GET endpoint for validating store existence.
     * Checks if a store exists by ID and returns appropriate HTTP status codes.
     * 
     * @param storeId The ID of the store to validate
     * @return ResponseEntity with validation result
     *         - 200 OK if store exists
     *         - 404 NOT FOUND if store doesn't exist
     */
    @GetMapping("/validate/{storeId}")
    public ResponseEntity<Map<String, Object>> validateStore(@PathVariable Long storeId) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<Store> store = storeRepository.findById(storeId);
        
        if (store.isPresent()) {
            response.put("valid", true);
            response.put("message", "Store exists");
            response.put("store", store.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("valid", false);
            response.put("message", "Store not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * POST endpoint for placing an order.
     * Handles exceptions with try-catch block and returns error responses.
     * 
     * @param orderRequest Map containing order details (customerId, productId, storeId, quantity)
     * @return ResponseEntity with order result or error message
     *         - 201 CREATED if order placed successfully
     *         - 400 BAD REQUEST if validation fails
     *         - 500 INTERNAL SERVER ERROR if an unexpected error occurs
     */
    @PostMapping("/placeOrder")
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody Map<String, Object> orderRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Extract order details from request
            Long customerId = Long.valueOf(orderRequest.get("customerId").toString());
            Long productId = Long.valueOf(orderRequest.get("productId").toString());
            Long storeId = Long.valueOf(orderRequest.get("storeId").toString());
            Integer quantity = Integer.valueOf(orderRequest.get("quantity").toString());
            
            // Validate input parameters
            if (customerId == null || productId == null || storeId == null || quantity == null || quantity <= 0) {
                response.put("success", false);
                response.put("message", "Invalid order parameters");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Place the order using OrderService
            OrderDetails order = orderService.placeOrder(customerId, productId, storeId, quantity);
            
            response.put("success", true);
            response.put("message", "Order placed successfully");
            response.put("orderId", order.getId());
            response.put("totalPrice", order.getTotalPrice());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            // Handle validation errors (e.g., insufficient inventory, entity not found)
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            
        } catch (Exception e) {
            // Handle unexpected errors
            response.put("success", false);
            response.put("message", "An error occurred while placing the order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * GET endpoint to retrieve all stores.
     * 
     * @return ResponseEntity with list of all stores
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllStores() {
        Map<String, Object> response = new HashMap<>();
        response.put("stores", storeRepository.findAll());
        return ResponseEntity.ok(response);
    }
}
