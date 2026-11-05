package com.project.code.Controller;

import com.project.code.Model.Review;
import com.project.code.Repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ReviewController handles review-related API endpoints.
 * Includes functionality to retrieve and manage product reviews.
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    /**
     * GET endpoint to retrieve reviews for a specific product at a specific store.
     * Dynamically retrieves customer names from the Customer entity.
     * Returns structured review data including customer information, ratings, and comments.
     * 
     * @param storeId The ID of the store
     * @param productId The ID of the product
     * @return ResponseEntity with list of reviews
     *         - 200 OK with review data
     *         - 404 NOT FOUND if no reviews exist
     */
    @GetMapping("/{storeId}/{productId}")
    public ResponseEntity<Map<String, Object>> getReviewsByStoreAndProduct(
            @PathVariable Long storeId,
            @PathVariable Long productId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Retrieve all reviews using findAll() and filter by store and product
            List<Review> allReviews = reviewRepository.findAll();
            
            // Filter reviews for the specified store and product
            List<Review> filteredReviews = allReviews.stream()
                    .filter(review -> review.getStore().getId().equals(storeId) &&
                            review.getProduct().getId().equals(productId))
                    .collect(Collectors.toList());
            
            if (filteredReviews.isEmpty()) {
                response.put("success", false);
                response.put("message", "No reviews found for this product at this store");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // Build structured response with customer names retrieved dynamically
            List<Map<String, Object>> reviewData = filteredReviews.stream()
                    .map(review -> {
                        Map<String, Object> reviewMap = new HashMap<>();
                        reviewMap.put("reviewId", review.getId());
                        reviewMap.put("customerName", review.getCustomer().getName()); // Dynamically retrieved
                        reviewMap.put("customerId", review.getCustomer().getId());
                        reviewMap.put("rating", review.getRating());
                        reviewMap.put("comment", review.getComment());
                        return reviewMap;
                    })
                    .collect(Collectors.toList());
            
            // Calculate average rating
            double averageRating = filteredReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            
            response.put("success", true);
            response.put("storeId", storeId);
            response.put("productId", productId);
            response.put("totalReviews", filteredReviews.size());
            response.put("averageRating", averageRating);
            response.put("reviews", reviewData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving reviews: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * GET endpoint to retrieve all reviews.
     * Uses reviewRepository.findAll() and wraps results in structured response.
     * 
     * @return ResponseEntity with all reviews
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllReviews() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Review> allReviews = reviewRepository.findAll();
            
            // Wrap results in structured response
            List<Map<String, Object>> reviewData = allReviews.stream()
                    .map(review -> {
                        Map<String, Object> reviewMap = new HashMap<>();
                        reviewMap.put("reviewId", review.getId());
                        reviewMap.put("customerName", review.getCustomer().getName());
                        reviewMap.put("productName", review.getProduct().getName());
                        reviewMap.put("storeName", review.getStore().getName());
                        reviewMap.put("rating", review.getRating());
                        reviewMap.put("comment", review.getComment());
                        return reviewMap;
                    })
                    .collect(Collectors.toList());
            
            response.put("success", true);
            response.put("totalReviews", allReviews.size());
            response.put("reviews", reviewData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving all reviews: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * POST endpoint to create a new review.
     * 
     * @param reviewRequest Map containing review details
     * @return ResponseEntity with created review
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createReview(@RequestBody Map<String, Object> reviewRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Implementation for creating a review would go here
            response.put("success", true);
            response.put("message", "Review created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating review: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
