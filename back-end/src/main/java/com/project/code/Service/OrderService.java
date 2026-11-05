package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderService handles business logic for order processing.
 * Manages order creation, inventory updates, and related operations.
 */
@Service
public class OrderService {
    
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private StoreRepository storeRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private ServiceClass serviceClass;
    
    /**
     * Places an order by creating OrderDetails and updating inventory.
     * Saves OrderDetails using orderDetailsRepository.save() and uses the saved result.
     * Reduces the inventory stock level and saves the updated inventory.
     * 
     * @param customerId The ID of the customer placing the order
     * @param productId The ID of the product being ordered
     * @param storeId The ID of the store where the order is placed
     * @param quantity The quantity of the product being ordered
     * @return The saved OrderDetails entity
     * @throws IllegalArgumentException if validation fails or entities are not found
     */
    @Transactional
    public OrderDetails placeOrder(Long customerId, Long productId, Long storeId, Integer quantity) {
        // Validate customer exists
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
        
        // Validate product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
        
        // Validate store exists
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));
        
        // Validate inventory exists
        boolean inventoryExists = serviceClass.validateInventory(productId, storeId);
        if (!inventoryExists) {
            throw new IllegalArgumentException("Inventory not found for product ID " + productId + 
                    " at store ID " + storeId);
        }
        
        // Get inventory entry
        Inventory inventory = serviceClass.getInventoryId(productId, storeId);
        
        // Validate sufficient quantity
        if (inventory.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient inventory. Available: " + 
                    inventory.getQuantity() + ", Requested: " + quantity);
        }
        
        // Calculate total price
        Double totalPrice = product.getPrice() * quantity;
        
        // Create new order details
        OrderDetails orderDetails = new OrderDetails(store, customer, product, quantity, totalPrice);
        
        // Save OrderDetails using orderDetailsRepository.save() and use the saved result
        OrderDetails savedOrderDetails = orderDetailsRepository.save(orderDetails);
        
        // Reduce the inventory stock level
        int newQuantity = inventory.getQuantity() - quantity;
        inventory.setQuantity(newQuantity);
        
        // Save the updated inventory
        inventoryRepository.save(inventory);
        
        // Return the saved OrderDetails
        return savedOrderDetails;
    }
    
    /**
     * Retrieves order details by order ID.
     * 
     * @param orderId The ID of the order
     * @return The OrderDetails entity
     * @throws IllegalArgumentException if order not found
     */
    public OrderDetails getOrderById(Long orderId) {
        return orderDetailsRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
    }
    
    /**
     * Cancels an order and restores inventory.
     * 
     * @param orderId The ID of the order to cancel
     * @throws IllegalArgumentException if order not found
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        OrderDetails order = orderDetailsRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        
        // Restore inventory
        Inventory inventory = serviceClass.getInventoryId(
                order.getProduct().getId(), 
                order.getStore().getId());
        
        inventory.setQuantity(inventory.getQuantity() + order.getQuantity());
        inventoryRepository.save(inventory);
        
        // Delete the order
        orderDetailsRepository.delete(order);
    }
}
