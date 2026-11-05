package com.project.code.Controller;

import com.project.code.Model.Product;
import com.project.code.Model.Inventory;
import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ProductController handles product-related API endpoints.
 * Includes product retrieval, deletion, and management functionality.
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    /**
     * GET endpoint to retrieve a product by ID.
     * Includes error handling and structured response.
     * 
     * @param id The ID of the product to retrieve
     * @return ResponseEntity with product data or error message
     *         - 200 OK if product found
     *         - 404 NOT FOUND if product doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Product> product = productRepository.findById(id);
            
            if (product.isPresent()) {
                response.put("success", true);
                response.put("product", product.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Product not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving product: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * DELETE endpoint to remove a product and its associated inventory.
     * Uses @Transactional to ensure both product and inventory are deleted atomically.
     * Includes error handling for transactional integrity.
     * 
     * @param id The ID of the product to delete
     * @return ResponseEntity with deletion result
     *         - 200 OK if deletion successful
     *         - 404 NOT FOUND if product doesn't exist
     *         - 500 INTERNAL SERVER ERROR if deletion fails
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if product exists
            Optional<Product> product = productRepository.findById(id);
            
            if (!product.isPresent()) {
                response.put("success", false);
                response.put("message", "Product not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // Delete all inventory entries for this product first
            List<Inventory> inventoryList = inventoryRepository.findByProductId(id);
            if (!inventoryList.isEmpty()) {
                inventoryRepository.deleteAll(inventoryList);
                response.put("inventoryDeleted", inventoryList.size());
            }
            
            // Delete the product
            productRepository.deleteById(id);
            
            response.put("success", true);
            response.put("message", "Product and associated inventory deleted successfully");
            response.put("deletedProductId", id);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting product: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * GET endpoint to retrieve all products.
     * 
     * @return ResponseEntity with list of all products
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findAll());
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST endpoint to create a new product.
     * 
     * @param product The product to create
     * @return ResponseEntity with created product
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Product savedProduct = productRepository.save(product);
            response.put("success", true);
            response.put("product", savedProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating product: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
