package com.project.code.Repo;

import com.project.code.Model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * OrderDetailsRepository provides data access methods for OrderDetails entity.
 */
@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    
    /**
     * Find all orders for a specific customer.
     * 
     * @param customerId The customer ID
     * @return List of orders for the customer
     */
    List<OrderDetails> findByCustomerId(Long customerId);
    
    /**
     * Find all orders for a specific store.
     * 
     * @param storeId The store ID
     * @return List of orders at the store
     */
    List<OrderDetails> findByStoreId(Long storeId);
    
    /**
     * Find orders within a date range.
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return List of orders within the date range
     */
    @Query("SELECT o FROM OrderDetails o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<OrderDetails> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);
}
