package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Repo.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ServiceClass provides utility methods for inventory operations.
 * Contains methods for inventory validation and retrieval.
 */
@Service
public class ServiceClass {
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    /**
     * Validates if an inventory entry exists for the given product and store IDs.
     * Checks the existence of an inventory entry using product ID and store ID.
     * 
     * @param productId The ID of the product
     * @param storeId The ID of the store
     * @return true if inventory entry exists, false otherwise
     */
    public boolean validateInventory(Long productId, Long storeId) {
        try {
            // Query the inventory using product ID and store ID
            Inventory inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId);
            
            // Return true if inventory exists, false otherwise
            return inventory != null;
            
        } catch (Exception e) {
            // If any error occurs, return false
            return false;
        }
    }
    
    /**
     * Retrieves the inventory entry for the given product and store IDs.
     * Queries inventory using product ID and store ID and returns the correct record.
     * 
     * @param productId The ID of the product
     * @param storeId The ID of the store
     * @return The Inventory entity
     * @throws IllegalArgumentException if inventory not found
     */
    public Inventory getInventoryId(Long productId, Long storeId) {
        // Query the inventory using product ID and store ID
        Inventory inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId);
        
        // Validate that inventory exists
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory not found for product ID " + 
                    productId + " at store ID " + storeId);
        }
        
        // Return the correct inventory record
        return inventory;
    }
    
    /**
     * Checks if sufficient quantity exists in inventory.
     * 
     * @param productId The ID of the product
     * @param storeId The ID of the store
     * @param quantity The quantity to check
     * @return true if sufficient quantity available, false otherwise
     */
    public boolean checkInventoryQuantity(Long productId, Long storeId, Integer quantity) {
        try {
            Inventory inventory = getInventoryId(productId, storeId);
            return inventory.getQuantity() >= quantity;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets the available quantity for a product at a store.
     * 
     * @param productId The ID of the product
     * @param storeId The ID of the store
     * @return The available quantity, or 0 if not found
     */
    public Integer getAvailableQuantity(Long productId, Long storeId) {
        try {
            Inventory inventory = getInventoryId(productId, storeId);
            return inventory.getQuantity();
            
        } catch (Exception e) {
            return 0;
        }
    }
}
