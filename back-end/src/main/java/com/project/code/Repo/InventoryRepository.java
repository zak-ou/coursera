package com.project.code.Repo;

import com.project.code.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * InventoryRepository provides data access methods for Inventory entity.
 * Includes custom queries for retrieving inventory by product and store IDs.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    /**
     * Custom query to retrieve inventory by product ID and store ID.
     * Uses @Query annotation to define a custom JPQL query.
     * 
     * @param productId The ID of the product
     * @param storeId The ID of the store
     * @return The Inventory entity matching the criteria
     */
    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.store.id = :storeId")
    Inventory findByProductIdAndStoreId(@Param("productId") Long productId, @Param("storeId") Long storeId);
    
    /**
     * Method to retrieve all inventory entries for a given store ID.
     * Spring Data JPA automatically generates the query based on method name.
     * 
     * @param storeId The ID of the store
     * @return List of Inventory entities for the specified store
     */
    List<Inventory> findByStoreId(Long storeId);
    
    /**
     * Method to retrieve all inventory entries for a given product ID.
     * 
     * @param productId The ID of the product
     * @return List of Inventory entities for the specified product
     */
    List<Inventory> findByProductId(Long productId);
    
    /**
     * Custom query to find inventory with quantity greater than a specified value.
     * Useful for filtering out out-of-stock items.
     * 
     * @param quantity The minimum quantity threshold
     * @return List of Inventory entities with quantity greater than the threshold
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantity > :quantity")
    List<Inventory> findByQuantityGreaterThan(@Param("quantity") Integer quantity);
    
    /**
     * Custom query to find inventory for a specific product at stores with available stock.
     * 
     * @param productId The ID of the product
     * @return List of Inventory entities with quantity greater than 0
     */
    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.quantity > 0")
    List<Inventory> findAvailableInventoryByProductId(@Param("productId") Long productId);
}
