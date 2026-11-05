package com.project.code.Repo;

import com.project.code.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProductRepository provides data access methods for Product entity.
 * Includes custom queries for filtering products by category and store.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Method to filter products by category using derived query method.
     * Spring Data JPA automatically generates the query based on method name.
     * 
     * @param category The product category to filter by
     * @return List of Product entities matching the category
     */
    List<Product> findByCategory(String category);
    
    /**
     * Custom query to retrieve products by store ID and category.
     * Uses @Query annotation with JOIN to fetch products available at a specific store.
     * 
     * @param storeId The ID of the store
     * @param category The product category
     * @return List of Product entities matching both store and category criteria
     */
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN Inventory i ON p.id = i.product.id " +
           "WHERE i.store.id = :storeId AND p.category = :category")
    List<Product> findByStoreIdAndCategory(@Param("storeId") Long storeId, @Param("category") String category);
    
    /**
     * Derived query method to find products by name containing a substring.
     * Case-insensitive search for partial matches.
     * 
     * @param name The product name or partial name to search for
     * @return List of Product entities with names containing the search term
     */
    List<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Custom query to find products by store ID.
     * Returns all products available at a specific store.
     * 
     * @param storeId The ID of the store
     * @return List of Product entities available at the store
     */
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN Inventory i ON p.id = i.product.id " +
           "WHERE i.store.id = :storeId")
    List<Product> findByStoreId(@Param("storeId") Long storeId);
    
    /**
     * Custom query to find products by category and name.
     * 
     * @param category The product category
     * @param name The product name (partial match)
     * @return List of Product entities matching both criteria
     */
    @Query("SELECT p FROM Product p WHERE p.category = :category AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByCategoryAndNameContaining(@Param("category") String category, @Param("name") String name);
    
    /**
     * Custom query to find products within a price range.
     * 
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of Product entities within the price range
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
}
