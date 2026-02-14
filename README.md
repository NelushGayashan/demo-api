# WSO2 API Manager Demo - Spring Boot Application

A simple Spring Boot REST API application designed to demonstrate API exposure and management through WSO2 API Manager (APIM).

## 📋 Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [WSO2 APIM Integration](#wso2-apim-integration)
- [Testing the APIs](#testing-the-apis)

## ✨ Features

- **RESTful APIs** for Product and User management
- **OpenAPI/Swagger documentation** for easy API exploration
- **In-memory data storage** for quick demo setup
- **Health check endpoints** for monitoring
- **CORS enabled** for cross-origin requests
- **Validation** using Jakarta Bean Validation
- **Well-structured responses** with standardized format

## 📦 Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **WSO2 API Manager** (version 4.x recommended)
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## 🚀 Getting Started

### 1. Build the Application

```bash
# Navigate to the project directory
cd demo-api

# Build using Maven
mvn clean install

# Or build without running tests
mvn clean install -DskipTests
```

### 2. Run the Application

```bash
# Run using Maven
mvn spring-boot:run

# Or run the JAR file
java -jar target/wso2-apim-demo-1.0.0.jar
```

The application will start on **http://localhost:8080**

### 3. Verify the Application

Open your browser and navigate to:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (OpenAPI JSON)**: http://localhost:8080/api-docs
- **Health Check**: http://localhost:8080/api/v1/health

## 🔌 API Endpoints

### Health & Info
- `GET /api/v1/health` - Check API health status
- `GET /api/v1/info` - Get API information

### Products API
- `GET /api/v1/products` - Get all products
- `GET /api/v1/products/{id}` - Get product by ID
- `GET /api/v1/products/category/{category}` - Get products by category
- `GET /api/v1/products/categories` - Get all categories
- `POST /api/v1/products` - Create a new product
- `PUT /api/v1/products/{id}` - Update a product
- `DELETE /api/v1/products/{id}` - Delete a product

### Users API
- `GET /api/v1/users` - Get all users
- `GET /api/v1/users/{id}` - Get user by ID
- `GET /api/v1/users/username/{username}` - Get user by username
- `POST /api/v1/users` - Create a new user
- `PUT /api/v1/users/{id}` - Update a user
- `DELETE /api/v1/users/{id}` - Delete a user

## 🔧 WSO2 APIM Integration

### Overview
WSO2 API Manager helps you:
- Secure your APIs with authentication/authorization
- Apply rate limiting and throttling
- Monitor API usage and analytics
- Version your APIs
- Create API developer portals
- Monetize your APIs

### Step-by-Step Integration Guide

#### Step 1: Install and Start WSO2 APIM

1. Download WSO2 API Manager from [WSO2 official site](https://wso2.com/api-manager/)
2. Extract and navigate to the bin directory
3. Start the server:
   ```bash
   # Linux/Mac
   ./api-manager.sh
   
   # Windows
   api-manager.bat
   ```
4. Access the portals:
   - **Publisher Portal**: https://localhost:9443/publisher
   - **Developer Portal**: https://localhost:9443/devportal
   - **Admin Portal**: https://localhost:9443/admin
   - Default credentials: `admin / admin`

#### Step 2: Create an API in WSO2 APIM

**Using the Publisher Portal:**

1. Log in to the Publisher Portal (https://localhost:9443/publisher)

2. Click **"CREATE API"** → **"Import Open API"**

3. Choose **"OpenAPI URL"** and enter:
   ```
   http://localhost:8080/api-docs
   ```

4. Click **"NEXT"** and configure:
   - **Name**: WSO2 Demo API
   - **Context**: /demo
   - **Version**: v1
   - **Endpoint**: http://localhost:8080
   - **Business Plans**: Unlimited (for demo)

5. Click **"CREATE"** to create the API

#### Step 3: Configure API Resources

1. In the API overview, go to **"API Configurations"** → **"Resources"**

2. Review the auto-imported resources from your OpenAPI spec

3. You can customize:
   - **Rate limiting** per resource
   - **Authentication** requirements
   - **CORS settings**

#### Step 4: Deploy the API

1. Go to **"Deployments"**

2. Click **"Deploy"** to deploy to the Gateway

3. Select the **Gateway Environment** (usually "Default")

4. Click **"Deploy"**

#### Step 5: Publish the API

1. Go to **"Lifecycle"**

2. Click **"PUBLISH"** to make the API available in the Developer Portal

#### Step 6: Subscribe to the API (Developer Portal)

1. Log in to the Developer Portal (https://localhost:9443/devportal)

2. Find your **"WSO2 Demo API"**

3. Click **"SUBSCRIBE"**

4. Select or create an **Application** (e.g., "DemoApp")

5. Choose a **Throttling Tier** (e.g., "Unlimited")

6. Click **"SUBSCRIBE"**

#### Step 7: Generate Access Tokens

1. Go to **"Applications"** in the Developer Portal

2. Select your application (e.g., "DemoApp")

3. Go to the **"Production Keys"** tab

4. Click **"GENERATE KEYS"** to generate:
   - Consumer Key (Client ID)
   - Consumer Secret (Client Secret)
   - Access Token

5. Copy the **Access Token** for API calls

#### Step 8: Test the API through WSO2 Gateway

Now you can call your API through WSO2 APIM Gateway:

```bash
# Without WSO2 (direct to Spring Boot)
curl http://localhost:8080/api/v1/products

# With WSO2 APIM Gateway (secured)
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
     https://localhost:8243/demo/v1/products
```

**Note**: The gateway URL uses:
- **HTTPS**: 8243 (default secure port)
- **HTTP**: 8280 (default non-secure port)
- **Context**: /demo (as configured)
- **Version**: /v1

### Key Concepts Demonstrated

#### 1. **API Gateway Pattern**
WSO2 APIM acts as a gateway between clients and your backend service, providing:
- Single entry point
- Request routing
- Protocol translation

#### 2. **Security**
- **OAuth 2.0** authentication
- **API Keys** for application identification
- **JWT tokens** for stateless authentication

#### 3. **Rate Limiting / Throttling**
- Protect backend from overload
- Implement usage tiers (free, premium, enterprise)
- Per-application or per-user limits

#### 4. **API Versioning**
- Support multiple API versions simultaneously
- Gradual migration for consumers
- Backward compatibility

#### 5. **Analytics & Monitoring**
- Track API usage
- Monitor performance
- Identify trends

#### 6. **API Lifecycle Management**
- **CREATED** → **PUBLISHED** → **DEPRECATED** → **RETIRED**
- Control API visibility and availability

### Testing Different Scenarios

#### Scenario 1: Rate Limiting
1. Apply a throttling policy (e.g., 10 requests/minute)
2. Make multiple requests quickly
3. Observe throttling response

#### Scenario 2: API Versioning
1. Create v2 of your API with changes
2. Deploy both v1 and v2
3. Test both versions simultaneously

#### Scenario 3: Application-based Access
1. Create multiple applications
2. Subscribe each to different tiers
3. Test different rate limits per application

#### Scenario 4: API Monetization
1. Create paid subscription tiers
2. Configure billing plans
3. Monitor usage for billing

## 🧪 Testing the APIs

### Using cURL

```bash
# Get all products
curl http://localhost:8080/api/v1/products

# Get product by ID
curl http://localhost:8080/api/v1/products/1

# Create a new product
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Wireless Mouse",
    "description": "Ergonomic wireless mouse",
    "price": 29.99,
    "category": "Electronics",
    "stock": 50
  }'

# Update a product
curl -X PUT http://localhost:8080/api/v1/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gaming Laptop",
    "description": "High-performance gaming laptop",
    "price": 1499.99,
    "category": "Electronics",
    "stock": 10
  }'

# Delete a product
curl -X DELETE http://localhost:8080/api/v1/products/1
```

### Using Postman

1. Import the OpenAPI spec: http://localhost:8080/api-docs
2. Postman will auto-generate all requests
3. Test each endpoint with sample data

### Through WSO2 Gateway (with token)

```bash
# Get OAuth2 token first (from WSO2 Developer Portal)
TOKEN="your_access_token_here"

# Call through gateway
curl -H "Authorization: Bearer $TOKEN" \
     https://localhost:8243/demo/v1/products
```

## 📚 Additional Resources

- [WSO2 API Manager Documentation](https://apim.docs.wso2.com/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [OpenAPI Specification](https://swagger.io/specification/)

## 🎯 Learning Objectives Achieved

After completing this demo, you will understand:

✅ How to create RESTful APIs with Spring Boot  
✅ How to document APIs using OpenAPI/Swagger  
✅ How to integrate backend services with WSO2 APIM  
✅ API Gateway concepts and benefits  
✅ OAuth 2.0 authentication flow  
✅ Rate limiting and throttling  
✅ API versioning strategies  
✅ API lifecycle management  
✅ Developer portal usage  

## 📝 License

This project is created for educational and demonstration purposes.

## 🤝 Contributing

Feel free to fork, modify, and use this project for learning WSO2 API Manager concepts!
