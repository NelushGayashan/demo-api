# WSO2 APIM Demo - Enhanced Version with Headers & Query Parameters

Complete Spring Boot REST API with MySQL database, custom headers, query parameters, and WSO2 API Manager integration.

## 🎯 What's New in This Version

✅ **Custom Request/Response Headers** - X-Client-ID, X-Request-ID, X-API-Version  
✅ **Advanced Query Parameters** - Filtering by category, brand, price range, search  
✅ **Complete MySQL Database** - 4 tables with relationships and sample data  
✅ **Enhanced APIs** - Products, Users, Orders with full CRUD operations  
✅ **Swagger Documentation** - Complete API documentation with header examples  
✅ **Postman Collections** - Separate collections for direct and gateway calls  

## 📦 Database Schema

### Tables
1. **products** - Product catalog with SKU, brand, category
2. **users** - User management with address, city, country
3. **orders** - Order management with status tracking
4. **order_items** - Order line items

## 🚀 Quick Start

### Step 1: Setup MySQL Database

```bash
# Login to MySQL
mysql -u root -p

# Run the complete setup script
source complete-mysql-setup.sql
```

This creates:
- Database: `wso2_demo_db`
- All 4 tables with proper relationships
- 15 sample products
- 8 sample users
- 8 sample orders with items
- Application user: `wso2demo` / `Wso2Demo@2024`

### Step 2: Configure Application

Update `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 3: Run Application

```bash
mvn spring-boot:run
```

Access:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## 🔌 Enhanced API Endpoints

### Products API (with Headers & Query Parameters)

#### Get All Products with Filters
```bash
GET /api/v1/products?category=Electronics&brand=Apple&minPrice=1000&maxPrice=5000&search=Mac

Headers:
  X-Client-ID: client-123
  X-Request-ID: req-abc-xyz
  X-API-Version: 1.0

Response Headers:
  X-Total-Count: 5
  X-API-Version: 1.0
  X-Request-ID: req-abc-xyz
```

#### Get Product by ID
```bash
GET /api/v1/products/1

Headers:
  X-Request-ID: req-123
```

#### Get Product by SKU
```bash
GET /api/v1/products/sku/APPLE-MBP-001

Headers:
  X-Request-ID: req-456
```

#### Get Low Stock Products
```bash
GET /api/v1/products/low-stock?threshold=20

Headers:
  X-Request-ID: req-789
```

#### Get All Brands
```bash
GET /api/v1/products/brands
```

#### Create Product
```bash
POST /api/v1/products

Headers:
  X-Client-ID: client-123
  X-Request-ID: req-create-001
  Content-Type: application/json

Body:
{
  "name": "iPad Pro",
  "description": "12.9-inch tablet",
  "price": 1099.99,
  "category": "Electronics",
  "stock": 25,
  "sku": "APPLE-IPP-016",
  "brand": "Apple"
}
```

### Users API (with Headers & Query Parameters)

#### Get All Users with Filters
```bash
GET /api/v1/users?country=USA&city=New%20York&status=ACTIVE

Headers:
  X-Request-ID: req-users-001
```

#### Get User by Username
```bash
GET /api/v1/users/username/john.doe

Headers:
  X-Request-ID: req-user-002
```

#### Search Users
```bash
GET /api/v1/users/search?name=John

Headers:
  X-Request-ID: req-search-001
```

### Orders API (with Headers)

#### Get All Orders
```bash
GET /api/v1/orders

Headers:
  X-Request-ID: req-orders-001
```

#### Get Orders by User
```bash
GET /api/v1/orders/user/1

Headers:
  X-Request-ID: req-orders-002
```

#### Get Orders by Status
```bash
GET /api/v1/orders/status/DELIVERED

Headers:
  X-Request-ID: req-orders-003
```

#### Create Order
```bash
POST /api/v1/orders

Headers:
  X-Client-ID: mobile-app
  X-Request-ID: req-create-order-001
  Content-Type: application/json

Body:
{
  "orderNumber": "ORD-2024-009",
  "userId": 1,
  "totalAmount": 1299.99,
  "status": "PENDING",
  "paymentMethod": "CREDIT_CARD",
  "shippingAddress": "123 Main St, New York, USA"
}
```

## 📊 Query Parameter Examples

### Filter Products
```bash
# By category
GET /api/v1/products?category=Electronics

# By brand
GET /api/v1/products?brand=Apple

# By price range
GET /api/v1/products?minPrice=500&maxPrice=2000

# Combined filters
GET /api/v1/products?category=Electronics&brand=Apple&minPrice=1000

# Search by name
GET /api/v1/products?search=laptop
```

### Filter Users
```bash
# By country
GET /api/v1/users?country=USA

# By city
GET /api/v1/users?city=London

# By status
GET /api/v1/users?status=ACTIVE

# Search by name
GET /api/v1/users/search?name=john
```

### Filter Orders
```bash
# By status
GET /api/v1/orders/status/DELIVERED

# By user
GET /api/v1/orders/user/1

# By order number
GET /api/v1/orders/number/ORD-2024-001
```

## 🎨 Custom Headers

### Request Headers

**X-Client-ID**: Identifies the client application
```
X-Client-ID: web-app | mobile-app | external-client
```

**X-Request-ID**: Unique identifier for request tracing
```
X-Request-ID: req-20240214-123456
```

**X-API-Version**: API version being called
```
X-API-Version: 1.0 | 2.0
```

### Response Headers

**X-Total-Count**: Total number of records returned
```
X-Total-Count: 25
```

**X-API-Version**: Confirmed API version
```
X-API-Version: 1.0
```

**X-Request-ID**: Echo of request ID for tracing
```
X-Request-ID: req-20240214-123456
```

**Location**: URL of created resource (POST requests)
```
Location: /api/v1/products/16
```

## 🧪 Testing with cURL

### Test with Headers and Query Parameters
```bash
# Get products with all filters and headers
curl -X GET "http://localhost:8080/api/v1/products?category=Electronics&brand=Apple&minPrice=1000&maxPrice=5000" \
  -H "X-Client-ID: curl-client" \
  -H "X-Request-ID: test-req-001" \
  -H "X-API-Version: 1.0" \
  -v

# Create product with headers
curl -X POST "http://localhost:8080/api/v1/products" \
  -H "Content-Type: application/json" \
  -H "X-Client-ID: curl-client" \
  -H "X-Request-ID: create-req-001" \
  -d '{
    "name": "Test Product",
    "description": "Created via cURL",
    "price": 999.99,
    "category": "Electronics",
    "stock": 50,
    "sku": "TEST-001",
    "brand": "TestBrand"
  }'
```

## 🔐 WSO2 APIM Integration

Headers are automatically passed through WSO2 Gateway:

```bash
# Through WSO2 Gateway
curl -X GET "https://localhost:8243/demo/v1/products?category=Electronics&brand=Apple" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "X-Client-ID: wso2-client" \
  -H "X-Request-ID: wso2-req-001" \
  -k
```

WSO2 APIM can:
- Validate headers
- Add/modify headers
- Use headers for routing
- Track requests via X-Request-ID
- Implement header-based throttling

## 📁 Project Structure

```
demo-api/
├── src/main/java/com/example/wso2demo/
│   ├── controller/
│   │   ├── ProductController.java (Enhanced with headers/queries)
│   │   ├── UserController.java (Enhanced)
│   │   ├── OrderController.java (New)
│   │   └── HealthCheckController.java
│   ├── service/
│   │   ├── ProductService.java
│   │   ├── UserService.java
│   │   └── OrderService.java
│   ├── repository/
│   │   ├── ProductRepository.java
│   │   ├── UserRepository.java
│   │   ├── OrderRepository.java
│   │   └── OrderItemRepository.java
│   ├── model/
│   │   ├── Product.java
│   │   ├── User.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   └── ApiResponse.java
│   └── Wso2ApimDemoApplication.java
├── src/main/resources/
│   ├── application.properties
│   ├── application-dev.properties
│   └── application-prod.properties
├── complete-mysql-setup.sql (Complete DB script)
├── pom.xml
└── README.md
```

## 📋 Available Endpoints Summary

### Products (11 endpoints)
- GET /api/v1/products (with filters)
- GET /api/v1/products/{id}
- GET /api/v1/products/sku/{sku}
- GET /api/v1/products/category/{category}
- GET /api/v1/products/brand/{brand}
- GET /api/v1/products/categories
- GET /api/v1/products/brands
- GET /api/v1/products/low-stock
- POST /api/v1/products
- PUT /api/v1/products/{id}
- DELETE /api/v1/products/{id}

### Users (10 endpoints)
- GET /api/v1/users (with filters)
- GET /api/v1/users/{id}
- GET /api/v1/users/username/{username}
- GET /api/v1/users/search
- GET /api/v1/users/country/{country}
- GET /api/v1/users/status/{status}
- POST /api/v1/users
- PUT /api/v1/users/{id}
- DELETE /api/v1/users/{id}
- GET /api/v1/users/countries

### Orders (8 endpoints)
- GET /api/v1/orders
- GET /api/v1/orders/{id}
- GET /api/v1/orders/number/{orderNumber}
- GET /api/v1/orders/user/{userId}
- GET /api/v1/orders/status/{status}
- POST /api/v1/orders
- PUT /api/v1/orders/{id}
- DELETE /api/v1/orders/{id}

## 📚 Documentation

- **MYSQL_SETUP.md** - Complete MySQL setup guide
- **DOCKER_MYSQL_SETUP.md** - Docker-based MySQL setup
- **WSO2_CONCEPTS.md** - WSO2 APIM concepts and tutorial
- **QUICKSTART.md** - 15-minute quick start guide
- **API_REQUESTS.md** - cURL command examples
- **Swagger UI** - Interactive API documentation

## 🎓 Learning Objectives

After using this demo, you'll understand:

✅ How to implement custom request/response headers in Spring Boot  
✅ How to use query parameters for filtering and searching  
✅ How headers pass through WSO2 API Manager Gateway  
✅ How to design RESTful APIs with proper HTTP semantics  
✅ How to integrate Spring Boot with MySQL database  
✅ How to document APIs with OpenAPI/Swagger  
✅ How to test APIs with Postman and cURL  
✅ How WSO2 APIM manages and secures your APIs  

---

**Ready to explore advanced API management! 🚀**
