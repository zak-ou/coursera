package com.project.code.Model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Customer entity representing a customer in the system.
 * Establishes a one-to-many relationship with OrderDetails.
 */
@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String phone;
    
    /**
     * One-to-many relationship with OrderDetails.
     * Uses EAGER fetching to load all orders when the customer is retrieved.
     * This ensures that order details are immediately available without additional queries.
     */
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderDetails> orders;
    
    // Constructors
    public Customer() {
    }
    
    public Customer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public List<OrderDetails> getOrders() {
        return orders;
    }
    
    public void setOrders(List<OrderDetails> orders) {
        this.orders = orders;
    }
}
