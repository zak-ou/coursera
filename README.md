# Java Database Final Project

This is a complete Java Spring Boot application that demonstrates various aspects of backend development including:
- Entity relationships (One-to-Many, Many-to-One)
- RESTful API endpoints
- Exception handling
- Database operations
- Repository patterns
- Service layer implementation

## Project Structure

```
back-end/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── project/
│       │           └── code/
│       │               ├── Application.java
│       │               ├── Controller/
│       │               │   ├── InventoryController.java
│       │               │   ├── ProductController.java
│       │               │   ├── ReviewController.java
│       │               │   └── StoreController.java
│       │               ├── Model/
│       │               │   ├── Customer.java
│       │               │   ├── Inventory.java
│       │               │   ├── OrderDetails.java
│       │               │   ├── Product.java
│       │               │   ├── Review.java
│       │               │   └── Store.java
│       │               ├── Repo/
│       │               │   ├── CustomerRepository.java
│       │               │   ├── InventoryRepository.java
│       │               │   ├── OrderDetailsRepository.java
│       │               │   ├── ProductRepository.java
│       │               │   ├── ReviewRepository.java
│       │               │   └── StoreRepository.java
│       │               └── Service/
│       │                   ├── OrderService.java
│       │                   └── ServiceClass.java
│       └── resources/
│           └── application.properties
└── pom.xml
```

## Key Features by Question

### Question 1: Customer.java
- ✅ One-to-many relationship with OrderDetails
- ✅ Uses `@OneToMany` with `FetchType.EAGER`
- ✅ Properly configured collection type

### Question 2: OrderDetails.java
- ✅ Store field annotated with `@ManyToOne` and `@JoinColumn(name = "store_id")`
- ✅ orderDate field uses `LocalDateTime`
- ✅ Proper annotations and documentation

### Question 3: StoreController.java
- ✅ GET endpoint for store validation with proper HTTP status codes
- ✅ placeOrder method with try-catch exception handling
- ✅ Returns appropriate error responses

### Question 4: ProductController.java
- ✅ GET `/product/{id}` endpoint with error handling
- ✅ DELETE `/product/{id}` with transactional integrity
- ✅ Deletes both product and inventory

### Question 5: InventoryController.java
- ✅ GET `/filter/{category}/{name}/{storeId}` with conditional logic
- ✅ GET `/validate/{quantity}/{storeId}/{productId}` with validation
- ✅ Input validation and clear responses

### Question 6: ReviewController.java
- ✅ GET `/reviews/{storeId}/{productId}` retrieves customer names dynamically
- ✅ Uses `reviewRepository.findAll()` with structured response
- ✅ Returns structured review data

### Question 7: OrderService.java
- ✅ Saves OrderDetails using `orderDetailsRepository.save()`
- ✅ Uses the saved result correctly
- ✅ Reduces inventory stock and saves updated inventory

### Question 8: ServiceClass.java
- ✅ `validateInventory` checks inventory existence using product and store IDs
- ✅ `getInventoryId` queries and returns correct inventory record

### Question 9: InventoryRepository.java
- ✅ Custom query to retrieve inventory by product and store IDs
- ✅ Method to retrieve all inventory for a store ID

### Question 10: ProductRepository.java
- ✅ Method to filter products by category (derived query)
- ✅ Custom query to retrieve products by store ID and category

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Database Setup
1. Install MySQL
2. Create database:
```sql
CREATE DATABASE store_database;
```
3. Update `application.properties` with your MySQL credentials

### Running the Application
```bash
cd back-end
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Store Endpoints
- `GET /api/store/validate/{storeId}` - Validate store existence
- `POST /api/store/placeOrder` - Place a new order
- `GET /api/store/all` - Get all stores

### Product Endpoints
- `GET /api/product/{id}` - Get product by ID
- `DELETE /api/product/{id}` - Delete product and inventory
- `GET /api/product/all` - Get all products
- `POST /api/product/create` - Create new product

### Inventory Endpoints
- `GET /api/inventory/filter/{category}/{name}/{storeId}` - Filter inventory
- `GET /api/inventory/validate/{quantity}/{storeId}/{productId}` - Validate quantity
- `GET /api/inventory/all` - Get all inventory

### Review Endpoints
- `GET /api/reviews/{storeId}/{productId}` - Get reviews for product at store
- `GET /api/reviews/all` - Get all reviews
- `POST /api/reviews/create` - Create new review

## Technologies Used
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL
- Maven
- Java 17

## Notes for GitHub Submission

When you push this project to GitHub, the URLs for submission will follow this pattern:

```
https://github.com/YOUR_USERNAME/java-database-final/blob/main/back-end/src/main/java/com/project/code/Model/Customer.java
https://github.com/YOUR_USERNAME/java-database-final/blob/main/back-end/src/main/java/com/project/code/Model/OrderDetails.java
https://github.com/YOUR_USERNAME/java-database-final/blob/main/back-end/src/main/java/com/project/code/Controller/StoreController.java
... and so on
```

## License
This project is for educational purposes as part of a Coursera final project.
