package com.project.code.Repo;

import com.project.code.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ReviewRepository provides data access methods for Review entity.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    /**
     * Find all reviews for a specific product.
     * 
     * @param productId The product ID
     * @return List of reviews for the product
     */
    List<Review> findByProductId(Long productId);
    
    /**
     * Find all reviews at a specific store.
     * 
     * @param storeId The store ID
     * @return List of reviews at the store
     */
    List<Review> findByStoreId(Long storeId);
    
    /**
     * Find all reviews by a specific customer.
     * 
     * @param customerId The customer ID
     * @return List of reviews by the customer
     */
    List<Review> findByCustomerId(Long customerId);
    
    /**
     * Find reviews by store and product.
     * 
     * @param storeId The store ID
     * @param productId The product ID
     * @return List of reviews matching the criteria
     */
    @Query("SELECT r FROM Review r WHERE r.store.id = :storeId AND r.product.id = :productId")
    List<Review> findByStoreIdAndProductId(@Param("storeId") Long storeId, @Param("productId") Long productId);
}
