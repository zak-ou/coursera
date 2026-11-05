package com.project.code.Repo;

import com.project.code.Model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * StoreRepository provides data access methods for Store entity.
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    
    /**
     * Find stores by location.
     * 
     * @param location The store location
     * @return List of stores at the specified location
     */
    List<Store> findByLocation(String location);
    
    /**
     * Find store by name.
     * 
     * @param name The store name
     * @return List of stores with the specified name
     */
    List<Store> findByName(String name);
}
