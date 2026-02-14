# Postman Collections Guide - Direct Services & APIM Gateway

Complete guide for using the enhanced Postman collections with headers and query parameters.

## 📦 Two Collections Provided

### 1. **WSO2_Demo_Complete_Direct.postman_collection.json**
   - Tests **Direct Spring Boot** services
   - **No authentication** required
   - Base URL: `http://localhost:8080`
   - Includes custom headers (X-Client-ID, X-Request-ID, X-API-Version)
   - All query parameters included

### 2. **WSO2_Demo_Complete_APIM.postman_collection.json**
   - Tests services through **WSO2 Gateway**
   - **OAuth2 authentication** required
   - Base URL: `https://localhost:9443`
   - Context path: `/apis/wso2apimdemoapi/1.0.0`
   - All custom headers pass through gateway

## 🚀 Quick Setup

### Step 1: Import Collections into Postman

1. Open **Postman**
2. Click **Import** button (top left)
3. Click **Upload Files**
4. Select both JSON files:
   - `WSO2_Demo_Complete_Direct.postman_collection.json`
   - `WSO2_Demo_Complete_APIM.postman_collection.json`
5. Click **Import**

### Step 2: Configure Direct Services Collection

This collection works immediately - no configuration needed!

**Variables (already set):**
- `direct_base_url` = `http://localhost:8080`

Just ensure Spring Boot is running:
```bash
mvn spring-boot:run
```

### Step 3: Configure APIM Gateway Collection

**Update the access token:**

1. Click on **"WSO2 Demo API - Complete (APIM Gateway)"** collection
2. Go to **Variables** tab
3. Find `access_token` variable
4. Replace `YOUR_ACCESS_TOKEN_HERE` with your actual token

**How to get token:**
1. Go to WSO2 Developer Portal: https://localhost:9443/devportal
2. Login: `admin` / `admin`
3. Go to **Applications** → Your app
4. **Production Keys** tab
5. Click **"GENERATE KEYS"**
6. Copy the **Access Token**
7. Paste in Postman variable

**Variables:**
- `gateway_url` = `https://localhost:9443`
- `access_token` = `<your_token>`

## 📂 Collection Structure

### Direct Services Collection

```
WSO2 Demo API - Complete (Direct Services)
└── Direct Services (Spring Boot)
    ├── Health & Info (2 requests)
    ├── Products (11 requests)
    │   ├── Get All Products (with filters)
    │   ├── Get Product by ID
    │   ├── Get Product by SKU
    │   ├── Get Products by Category
    │   ├── Get Products by Brand
    │   ├── Get All Categories
    │   ├── Get All Brands
    │   ├── Get Low Stock Products
    │   ├── Create Product
    │   ├── Update Product
    │   └── Delete Product
    ├── Users (10 requests)
    │   ├── Get All Users (with filters)
    │   ├── Get User by ID
    │   ├── Get User by Username
    │   ├── Search Users by Name
    │   ├── Get Users by Country
    │   ├── Get Users by Status
    │   ├── Get All Countries
    │   ├── Create User
    │   ├── Update User
    │   └── Delete User
    └── Orders (9 requests)
        ├── Get All Orders
        ├── Get Order by ID
        ├── Get Order by Order Number
        ├── Get Orders by User ID
        ├── Get Orders by Status
        ├── Get All Order Statuses
        ├── Create Order
        ├── Update Order
        └── Delete Order
```

### APIM Gateway Collection

```
WSO2 Demo API - Complete (APIM Gateway)
└── APIM Gateway Services (WSO2)
    ├── Health & Info (2 requests with OAuth2)
    ├── Products (11 requests with OAuth2)
    ├── Users (10 requests with OAuth2)
    ├── Orders (9 requests with OAuth2)
    └── Test Error Scenarios (2 requests)
        ├── Test Missing Token (401)
        └── Test Invalid Token (401)
```

## 🎯 Key Features

### Custom Headers Included

**All requests include:**
- `X-Request-ID`: Auto-generated UUID (using `{{$guid}}`)
- `X-Client-ID`: Client identifier
  - Direct: `postman-client`
  - Gateway: `wso2-gateway-client`
- `X-API-Version`: `1.0`

**APIM requests also include:**
- `Authorization`: Bearer token for OAuth2

### Query Parameters Pre-configured

**Products:**
- `category` (e.g., Electronics, Furniture)
- `brand` (e.g., Apple, Dell)
- `minPrice` (e.g., 1000)
- `maxPrice` (e.g., 5000)
- `search` (product name search)
- `threshold` (for low stock)

**Users:**
- `country` (e.g., USA, UK)
- `city` (e.g., New York, London)
- `status` (ACTIVE, INACTIVE)
- `name` (search by name)

## 🧪 Testing Workflow

### Test Direct Services First

1. **Start Spring Boot**
   ```bash
   mvn spring-boot:run
   ```

2. **Test Health Check**
   - Collection: Direct Services
   - Request: Health & Info → Health Check
   - Click **Send**
   - Should get 200 OK

3. **Test Products with Filters**
   - Request: Products → Get All Products (with filters)
   - Click **Send**
   - Check query params in URL:
     `?category=Electronics&brand=Apple&minPrice=1000&maxPrice=5000`
   - See response headers:
     - `X-Total-Count`
     - `X-Request-ID`
     - `X-API-Version`

4. **Create a Product**
   - Request: Products → Create Product
   - Review body (already filled)
   - Click **Send**
   - Check Location header in response

5. **Test Users with Filters**
   - Request: Users → Get All Users (with filters)
   - Check query params: `?country=USA&status=ACTIVE`
   - Click **Send**

### Test Through WSO2 Gateway

1. **Ensure WSO2 APIM is Running**
   ```bash
   cd wso2am-4.x.x/bin
   ./api-manager.sh
   ```

2. **Update Access Token** (in collection variables)

3. **Test Health Check**
   - Collection: APIM Gateway Services
   - Request: Health & Info → Health Check
   - Click **Send**
   - Note the URL includes context: `/apis/wso2apimdemoapi/1.0.0`

4. **Test Products with Filters**
   - Request: Products → Get All Products (with filters)
   - Check Authorization header is included
   - Check custom headers are included
   - Click **Send**
   - WSO2 passes everything through!

5. **Test Error Scenarios**
   - Request: Test Error Scenarios → Test Missing Token
   - Click **Send**
   - Should get 401 from WSO2

## 💡 Tips and Tricks

### Dynamic Request IDs

All requests use `{{$guid}}` for X-Request-ID:
```
X-Request-ID: {{$guid}}
```

This generates a new UUID for each request, perfect for tracing!

### Disable Query Parameters

In request URL, you can:
- ✅ **Enable/Disable** individual query params
- ✅ **Modify values** easily
- ✅ **Add new params** on the fly

Example:
```
?category=Electronics    ← Enabled
&brand=Apple             ← Enabled
&minPrice=1000           ← Enabled
&maxPrice=5000           ← Can disable by unchecking
```

### Compare Direct vs Gateway

**Run same request in both collections:**

1. **Direct Services → Products → Get All Products**
   - URL: `http://localhost:8080/api/v1/products`
   - No auth
   - Direct response

2. **APIM Gateway → Products → Get All Products**
   - URL: `https://localhost:9443/apis/wso2apimdemoapi/1.0.0/api/v1/products`
   - OAuth2 auth
   - Same response!

**Compare:**
- Response times
- Headers added by WSO2
- Rate limiting behavior

### Use Collection Runner

**Test rate limiting:**

1. Select folder: Products (in Gateway collection)
2. Click **Run** (top right)
3. Set iterations: 30
4. Run collection
5. After exceeding limit → 429 errors

### Environment Variables (Alternative)

Instead of collection variables, create **Environments**:

**Environment: Local Dev**
```
direct_base_url = http://localhost:8080
```

**Environment: WSO2 Gateway**
```
gateway_url = https://localhost:9443
access_token = <your_token>
```

Switch between environments easily!

## 🔧 Customization

### Add More Headers

Edit any request → Headers tab:
```
X-Correlation-ID: {{$guid}}
X-User-Agent: PostmanTesting
X-Custom-Header: YourValue
```

### Modify Query Parameters

Edit URL → Query Params tab:
- Add new params
- Change values
- Enable/disable params

### Create Test Scripts

In **Tests** tab, add:
```javascript
// Check status code
pm.test("Status is 200", function () {
    pm.response.to.have.status(200);
});

// Check response time
pm.test("Response time < 500ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(500);
});

// Check headers
pm.test("Has X-Request-ID header", function () {
    pm.response.to.have.header("X-Request-ID");
});

// Parse response
const response = pm.response.json();
pm.test("Response has data", function () {
    pm.expect(response.success).to.be.true;
    pm.expect(response.data).to.be.an('array');
});
```

## 📊 Expected Responses

### Successful Response (Direct & Gateway)

```json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "MacBook Pro 16\"",
      "description": "Apple M3 Max, 32GB RAM, 1TB SSD",
      "price": 3499.99,
      "category": "Electronics",
      "stock": 15,
      "sku": "APPLE-MBP-001",
      "brand": "Apple",
      "createdAt": "2024-02-14T10:30:00",
      "updatedAt": "2024-02-14T10:30:00"
    }
  ],
  "timestamp": "2024-02-14T18:45:30"
}
```

### Response Headers

**Direct Services:**
```
X-Total-Count: 15
X-API-Version: 1.0
X-Request-ID: 550e8400-e29b-41d4-a716-446655440000
Content-Type: application/json
```

**Gateway Services (additional):**
```
X-Total-Count: 15
X-API-Version: 1.0
X-Request-ID: 550e8400-e29b-41d4-a716-446655440000
X-WSO2-Gateway: true
Content-Type: application/json
```

### Error Response (401 - Gateway Only)

```json
{
  "code": 900901,
  "message": "Invalid Credentials",
  "description": "Access failure for API: /apis/wso2apimdemoapi/1.0.0"
}
```

### Error Response (429 - Rate Limiting)

```json
{
  "code": 900802,
  "message": "Message throttled out",
  "description": "You have exceeded your quota"
}
```

## 🐛 Troubleshooting

### Direct Services Issues

**Cannot connect:**
- ✅ Check Spring Boot is running
- ✅ Verify port 8080 is available
- ✅ Test: `curl http://localhost:8080/api/v1/health`

**Headers not showing in response:**
- ✅ Click on **Headers** tab in response
- ✅ Headers like X-Request-ID are echo'd back

### Gateway Services Issues

**401 Unauthorized:**
- ✅ Check access token is set in variables
- ✅ Token may be expired (regenerate after 1 hour)
- ✅ Verify token format: `Bearer <token>`

**Cannot connect to gateway:**
- ✅ Check WSO2 APIM is running
- ✅ API is deployed and published
- ✅ Disable SSL verification in Postman (Settings → SSL)

**Wrong URL:**
- ✅ Check context path: `/apis/wso2apimdemoapi/1.0.0`
- ✅ Not `/demo` - that's only if you configured it differently

## 📚 Next Steps

1. **Explore all requests** in both collections
2. **Compare responses** between Direct and Gateway
3. **Test rate limiting** using Collection Runner
4. **Create test suites** with assertions
5. **Export and share** with your team

---

**You now have complete Postman collections for testing both Direct Services and APIM Gateway! 🎉**

Use **Direct Services** for development and **APIM Gateway** for testing API management features!
