# Sample API Test Requests

## Direct API Calls (to Spring Boot - no WSO2)

### Health Check
```bash
curl http://localhost:8080/api/v1/health
```

### Get API Info
```bash
curl http://localhost:8080/api/v1/info
```

## Products API

### Get All Products
```bash
curl http://localhost:8080/api/v1/products
```

### Get Product by ID
```bash
curl http://localhost:8080/api/v1/products/1
```

### Get Products by Category
```bash
curl http://localhost:8080/api/v1/products/category/Electronics
```

### Get All Categories
```bash
curl http://localhost:8080/api/v1/products/categories
```

### Create New Product
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Wireless Keyboard",
    "description": "Mechanical wireless keyboard",
    "price": 89.99,
    "category": "Electronics",
    "stock": 25
  }'
```

### Update Product
```bash
curl -X PUT http://localhost:8080/api/v1/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gaming Laptop Pro",
    "description": "Ultimate gaming laptop with RTX 4090",
    "price": 2499.99,
    "category": "Electronics",
    "stock": 5
  }'
```

### Delete Product
```bash
curl -X DELETE http://localhost:8080/api/v1/products/5
```

## Users API

### Get All Users
```bash
curl http://localhost:8080/api/v1/users
```

### Get User by ID
```bash
curl http://localhost:8080/api/v1/users/1
```

### Get User by Username
```bash
curl http://localhost:8080/api/v1/users/username/john.doe
```

### Create New User
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice.wonder",
    "email": "alice@example.com",
    "fullName": "Alice Wonderland",
    "phone": "+1234567893"
  }'
```

### Update User
```bash
curl -X PUT http://localhost:8080/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "email": "john.doe@newmail.com",
    "fullName": "John Doe Jr.",
    "phone": "+1234567899"
  }'
```

### Delete User
```bash
curl -X DELETE http://localhost:8080/api/v1/users/3
```

---

## API Calls Through WSO2 APIM Gateway

**Prerequisites:**
1. WSO2 APIM is running
2. API is published in WSO2 APIM
3. You have subscribed to the API and generated an access token

### Set Your Access Token
```bash
# Replace with your actual token from WSO2 Developer Portal
export TOKEN="your_access_token_here"
```

### Gateway Endpoints (HTTPS - Port 8243)

#### Get All Products through Gateway
```bash
curl -k -H "Authorization: Bearer $TOKEN" \
     https://localhost:8243/demo/v1/products
```

#### Get Product by ID through Gateway
```bash
curl -k -H "Authorization: Bearer $TOKEN" \
     https://localhost:8243/demo/v1/products/1
```

#### Create Product through Gateway
```bash
curl -k -X POST https://localhost:8243/demo/v1/products \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smart Watch",
    "description": "Fitness tracking smartwatch",
    "price": 199.99,
    "category": "Electronics",
    "stock": 40
  }'
```

#### Get All Users through Gateway
```bash
curl -k -H "Authorization: Bearer $TOKEN" \
     https://localhost:8243/demo/v1/users
```

### Gateway Endpoints (HTTP - Port 8280)

If you've configured HTTP gateway endpoint:

```bash
curl -H "Authorization: Bearer $TOKEN" \
     http://localhost:8280/demo/v1/products
```

---

## Testing Rate Limiting

### Test Throttling Limits
Run this script to make multiple rapid requests:

```bash
#!/bin/bash
# Test rate limiting by making 20 requests quickly

for i in {1..20}
do
   echo "Request #$i"
   curl -k -H "Authorization: Bearer $TOKEN" \
        https://localhost:8243/demo/v1/products
   echo ""
done
```

You should see throttling errors after exceeding your tier limit.

---

## Testing with Different Applications

### Application 1 (Free Tier - 10 req/min)
```bash
TOKEN_APP1="token_for_app1"
curl -k -H "Authorization: Bearer $TOKEN_APP1" \
     https://localhost:8243/demo/v1/products
```

### Application 2 (Premium Tier - 100 req/min)
```bash
TOKEN_APP2="token_for_app2"
curl -k -H "Authorization: Bearer $TOKEN_APP2" \
     https://localhost:8243/demo/v1/products
```

---

## Error Scenarios to Test

### 1. Missing Authentication Token
```bash
curl -k https://localhost:8243/demo/v1/products
# Expected: 401 Unauthorized
```

### 2. Invalid Token
```bash
curl -k -H "Authorization: Bearer invalid_token_12345" \
     https://localhost:8243/demo/v1/products
# Expected: 401 Unauthorized
```

### 3. Invalid Product ID
```bash
curl http://localhost:8080/api/v1/products/9999
# Expected: 404 Not Found
```

### 4. Invalid Request Body
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "price": -10
  }'
# Expected: 400 Bad Request with validation errors
```

---

## Using HTTPie (Alternative to cURL)

If you have HTTPie installed:

```bash
# Get products
http http://localhost:8080/api/v1/products

# Create product
http POST http://localhost:8080/api/v1/products \
  name="Tablet" \
  description="10-inch tablet" \
  price:=299.99 \
  category="Electronics" \
  stock:=30

# Through WSO2 Gateway
http https://localhost:8243/demo/v1/products \
  "Authorization:Bearer $TOKEN" --verify=no
```

---

## Postman Collection

You can also import the OpenAPI spec into Postman:

1. Open Postman
2. Click Import
3. Enter URL: `http://localhost:8080/api-docs`
4. Postman will create all requests automatically

For WSO2 Gateway calls:
1. Update the base URL to `https://localhost:8243/demo/v1`
2. Add Authorization header with Bearer token
3. Disable SSL verification for testing

---

## Notes

- **-k flag**: Used with curl to skip SSL certificate verification (for self-signed certs in dev)
- **$TOKEN**: Environment variable holding your OAuth2 access token
- **Gateway ports**: 
  - 8243 for HTTPS
  - 8280 for HTTP
- **Context path**: /demo (as configured in WSO2 APIM)
- **Version**: /v1 (as configured in WSO2 APIM)
