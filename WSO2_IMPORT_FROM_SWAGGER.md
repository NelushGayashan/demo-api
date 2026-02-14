# Creating APIs in WSO2 APIM from Swagger/OpenAPI Specification

This guide shows you multiple ways to create APIs in WSO2 API Manager using Swagger/OpenAPI definitions.

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Method 1: Import from URL](#method-1-import-from-url)
3. [Method 2: Import from File](#method-2-import-from-file)
4. [Method 3: Import from OpenAPI Archive](#method-3-import-from-openapi-archive)
5. [Method 4: Using REST API](#method-4-using-rest-api)
6. [Post-Import Configuration](#post-import-configuration)
7. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### 1. Get Your OpenAPI Specification

Your Spring Boot app exposes OpenAPI spec at:
```
http://localhost:8080/api-docs
```

Or download it:
```bash
curl http://localhost:8080/api-docs > openapi.json
```

### 2. WSO2 APIM Running

Ensure WSO2 API Manager is running:
```bash
cd wso2am-4.x.x/bin
./api-manager.sh  # Linux/Mac
api-manager.bat   # Windows
```

### 3. Access Publisher Portal

Open: https://localhost:9443/publisher
Login: `admin` / `admin`

---

## Method 1: Import from URL (Easiest & Recommended)

This is the simplest method - WSO2 APIM directly fetches your OpenAPI spec from your running Spring Boot application.

### Step-by-Step:

#### Step 1: Ensure Spring Boot is Running
```bash
cd demo-api
mvn spring-boot:run
```

Verify OpenAPI is accessible:
```bash
curl http://localhost:8080/api-docs
```

#### Step 2: Login to WSO2 Publisher Portal
- URL: https://localhost:9443/publisher
- Username: `admin`
- Password: `admin`

#### Step 3: Import OpenAPI from URL

1. Click **"CREATE API"** button (top right)

2. Select **"Import Open API"**

3. Choose **"OpenAPI URL"**

4. Enter your OpenAPI URL:
   ```
   http://localhost:8080/api-docs
   ```

5. Click **"NEXT"**

#### Step 4: Configure API Details

WSO2 will auto-populate many fields from your OpenAPI spec:

**Basic Information:**
- **Name**: `WSO2 APIM Demo API` (auto-filled from OpenAPI)
- **Context**: `/demo` (customize this)
- **Version**: `v1` or `1.0.0`
- **Business Plans**: Select `Unlimited` (for testing)

**Endpoint Configuration:**
- **Endpoint Type**: `HTTP/REST Endpoint`
- **Production Endpoint**: `http://localhost:8080`
- **Sandbox Endpoint**: (optional) `http://localhost:8080`

Click **"CREATE"**

#### Step 5: Review Imported Resources

After creation, you'll see:
- All your endpoints automatically imported
- Path parameters detected
- Query parameters detected
- Request/response schemas imported

Navigate to **"API Configurations"** → **"Resources"** to verify:
```
GET    /api/v1/products
GET    /api/v1/products/{id}
POST   /api/v1/products
PUT    /api/v1/products/{id}
DELETE /api/v1/products/{id}
GET    /api/v1/users
... (all endpoints)
```

#### Step 6: Deploy the API

1. Click **"Deployments"** in left menu
2. Click **"Deploy New Revision"**
3. Add description: "Initial deployment"
4. Select **"Default"** gateway
5. Click **"Deploy"**

#### Step 7: Publish the API

1. Click **"Lifecycle"** in left menu
2. Click **"PUBLISH"** button
3. API is now live!

---

## Method 2: Import from File

Use this when you want to import a downloaded OpenAPI file.

### Step 1: Download OpenAPI Spec

```bash
# Download as JSON
curl http://localhost:8080/api-docs > openapi.json

# Or download as YAML (if your app supports it)
curl http://localhost:8080/api-docs.yaml > openapi.yaml
```

### Step 2: Import in WSO2 APIM

1. Go to Publisher Portal: https://localhost:9443/publisher

2. Click **"CREATE API"** → **"Import Open API"**

3. Select **"OpenAPI File/Archive"**

4. Click **"Browse File to Upload"**

5. Select your `openapi.json` or `openapi.yaml` file

6. Click **"NEXT"**

7. Configure API details (same as Method 1, Step 4)

8. Click **"CREATE"**

9. Deploy and Publish (same as Method 1, Steps 6-7)

---

## Method 3: Import from OpenAPI Archive (Advanced)

This method is useful for complex APIs with multiple files.

### Step 1: Create OpenAPI Archive

Create a ZIP file with your OpenAPI spec and related files:

```
api-archive.zip
├── openapi.json (or openapi.yaml)
├── schemas/
│   ├── Product.json
│   ├── User.json
│   └── Order.json
└── examples/
    ├── product-example.json
    └── user-example.json
```

### Step 2: Import Archive

1. Go to Publisher Portal

2. Click **"CREATE API"** → **"Import Open API"**

3. Select **"OpenAPI File/Archive"**

4. Upload your `api-archive.zip`

5. Configure and Create (same as previous methods)

---

## Method 4: Using REST API (Programmatic)

For automation and CI/CD pipelines.

### Step 1: Get Access Token

```bash
# Get client credentials (one-time setup)
curl -k -X POST https://localhost:9443/client-registration/v0.17/register \
  -H "Authorization: Basic YWRtaW46YWRtaW4=" \
  -H "Content-Type: application/json" \
  -d '{
    "clientName": "api_importer",
    "owner": "admin",
    "grantType": "password refresh_token",
    "saasApp": true
  }'

# Note down clientId and clientSecret from response

# Get access token
curl -k -X POST https://localhost:9443/oauth2/token \
  -H "Authorization: Basic <BASE64(clientId:clientSecret)>" \
  -d "grant_type=password&username=admin&password=admin&scope=apim:api_create apim:api_publish"

# Note down the access_token
```

### Step 2: Import API via REST API

```bash
# Set your access token
TOKEN="your_access_token_here"

# Import from URL
curl -k -X POST https://localhost:9443/api/am/publisher/v4/apis/import-openapi \
  -H "Authorization: Bearer $TOKEN" \
  -F "url=http://localhost:8080/api-docs" \
  -F "additionalProperties={
    \"name\": \"Demo API\",
    \"version\": \"1.0.0\",
    \"context\": \"/demo\",
    \"endpoint_config\": {
      \"endpoint_type\": \"http\",
      \"production_endpoints\": {
        \"url\": \"http://localhost:8080\"
      }
    }
  }"

# Or import from file
curl -k -X POST https://localhost:9443/api/am/publisher/v4/apis/import-openapi \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@openapi.json" \
  -F "additionalProperties={
    \"name\": \"Demo API\",
    \"version\": \"1.0.0\",
    \"context\": \"/demo\",
    \"endpoint_config\": {
      \"endpoint_type\": \"http\",
      \"production_endpoints\": {
        \"url\": \"http://localhost:8080\"
      }
    }
  }"
```

### Step 3: Publish via REST API

```bash
# Get the API ID from previous response
API_ID="api-id-from-import-response"

# Deploy to gateway
curl -k -X POST "https://localhost:9443/api/am/publisher/v4/apis/$API_ID/deploy-revision" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '[{
    "name": "Default",
    "vhost": "localhost",
    "displayOnDevportal": true
  }]'

# Change lifecycle to PUBLISHED
curl -k -X POST "https://localhost:9443/api/am/publisher/v4/apis/$API_ID/change-lifecycle?action=Publish" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Post-Import Configuration

After importing, you may want to configure additional settings:

### 1. Configure Rate Limiting

**Via UI:**
1. Go to your API → **"API Configurations"** → **"Business Plans"**
2. Select throttling tiers:
   - ☑ Unlimited (for testing)
   - ☑ Gold (20k requests/min)
   - ☑ Silver (5k requests/min)
   - ☑ Bronze (1k requests/min)
3. Click **"Save"**

**Via REST API:**
```bash
curl -k -X PUT "https://localhost:9443/api/am/publisher/v4/apis/$API_ID" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "policies": ["Unlimited", "Gold", "Silver"]
  }'
```

### 2. Add/Modify Headers

WSO2 can pass your custom headers (X-Client-ID, X-Request-ID) through.

**Via UI:**
1. Go to **"Develop"** → **"API Configurations"** → **"Runtime"**
2. Under **"Request"** section
3. Enable **"Pass through existing headers"**
4. Or add custom header transformations

### 3. Configure CORS

**Via UI:**
1. Go to **"Develop"** → **"API Configurations"** → **"Runtime"**
2. Scroll to **"CORS Configuration"**
3. Enable CORS
4. Configure:
   - Access Control Allow Origins: `*` (or specific domains)
   - Access Control Allow Methods: `GET, POST, PUT, DELETE`
   - Access Control Allow Headers: `Authorization, Content-Type, X-Client-ID, X-Request-ID`
5. Click **"Save"**

### 4. Add API Documentation

**Via UI:**
1. Go to **"API Configurations"** → **"Documents"**
2. Click **"ADD NEW DOCUMENT"**
3. Choose document type:
   - How To
   - Samples & SDK
   - Public Forum
   - Support Forum
   - API Message Format
   - Other
4. Add content (Markdown supported)
5. Click **"Save"**

### 5. Configure Response Caching

**Via UI:**
1. Go to **"Develop"** → **"Resources"**
2. Select a GET endpoint
3. Expand the endpoint
4. Enable **"Response Caching"**
5. Set cache timeout (seconds)
6. Click **"Save"**

### 6. Add API Mediation (Advanced)

Add custom mediation sequences for request/response transformation:

**Via UI:**
1. Go to **"Develop"** → **"Message Mediation"**
2. Choose mediation type:
   - In Flow (Request)
   - Out Flow (Response)
   - Fault Flow (Error)
3. Upload or create mediation sequence (XML)

**Example - Add Custom Header:**
```xml
<sequence xmlns="http://ws.apache.org/ns/synapse" name="add-custom-header">
    <property name="X-API-Gateway" value="WSO2-APIM" scope="transport"/>
    <property name="X-Processed-Time" expression="get-property('SYSTEM_TIME')" scope="transport"/>
</sequence>
```

---

## Verifying the Import

### 1. Check in Publisher Portal

**Verify Endpoints:**
- Go to **"API Configurations"** → **"Resources"**
- Ensure all endpoints are imported
- Check HTTP methods are correct
- Verify path parameters (shown as `{id}`)
- Check query parameters are detected

**Verify Schemas:**
- Go to **"API Configurations"** → **"API Definition"**
- Click **"View OpenAPI"**
- Verify request/response schemas are present

### 2. Check in Developer Portal

1. Go to Developer Portal: https://localhost:9443/devportal
2. Find your API
3. Click **"Try Out"** tab
4. Test endpoints directly from UI

### 3. Test via API Call

```bash
# Subscribe to API first (via Developer Portal)
# Generate access token
# Then test:

curl -k -H "Authorization: Bearer YOUR_TOKEN" \
     https://localhost:8243/demo/v1/products
```

---

## Troubleshooting

### Issue 1: "Invalid OpenAPI Definition"

**Cause:** OpenAPI spec has validation errors

**Solutions:**
1. Validate your OpenAPI spec:
   ```bash
   # Use online validator
   # https://editor.swagger.io/
   
   # Or use swagger-cli
   npm install -g @apidevtools/swagger-cli
   swagger-cli validate openapi.json
   ```

2. Common OpenAPI issues:
   - Missing `servers` section
   - Invalid schema references
   - Missing required fields in schemas
   - Invalid data types

3. Fix and re-import

### Issue 2: "Cannot Connect to Endpoint"

**Cause:** WSO2 cannot reach your backend

**Solutions:**
1. Ensure Spring Boot is running:
   ```bash
   curl http://localhost:8080/api-docs
   ```

2. Check if WSO2 can reach localhost:
   - If WSO2 is in Docker, use `host.docker.internal` instead of `localhost`
   - Or use your machine's IP address

3. Update endpoint URL in WSO2:
   - Go to **"Develop"** → **"Endpoints"**
   - Update Production URL
   - Click **"Save"**

### Issue 3: "Endpoints Not Imported"

**Cause:** OpenAPI spec format issues

**Solution:**
Ensure your OpenAPI has proper structure:
```json
{
  "openapi": "3.0.1",
  "info": {
    "title": "API Title",
    "version": "1.0.0"
  },
  "servers": [
    {
      "url": "http://localhost:8080"
    }
  ],
  "paths": {
    "/api/v1/products": {
      "get": {
        ...
      }
    }
  }
}
```

### Issue 4: Headers Not Passing Through

**Cause:** WSO2 default behavior filters headers

**Solution:**
1. Go to **"Runtime"** configuration
2. Enable **"Pass through existing headers"**
3. Or explicitly whitelist headers:
   - X-Client-ID
   - X-Request-ID
   - X-API-Version

### Issue 5: Query Parameters Not Working

**Cause:** Query params not in OpenAPI definition

**Solution:**
Ensure your OpenAPI spec includes query parameters:
```json
{
  "paths": {
    "/api/v1/products": {
      "get": {
        "parameters": [
          {
            "name": "category",
            "in": "query",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ]
      }
    }
  }
}
```

---

## Best Practices

### 1. Use Descriptive API Names

```
Good: "Product Catalog API v1"
Bad:  "API" or "Test"
```

### 2. Version Your APIs

```
Context: /products/v1
Context: /products/v2
```

### 3. Use Proper HTTP Methods

Ensure OpenAPI spec uses correct methods:
- GET - Retrieve resources
- POST - Create resources
- PUT - Update resources
- DELETE - Remove resources
- PATCH - Partial updates

### 4. Include Comprehensive Descriptions

In your OpenAPI spec:
```yaml
paths:
  /api/v1/products:
    get:
      summary: Get all products
      description: |
        Retrieve a paginated list of products with optional filtering.
        Supports filtering by category, brand, and price range.
      parameters:
        - name: category
          in: query
          description: Filter products by category (Electronics, Furniture, etc.)
```

### 5. Use Tags for Organization

```yaml
paths:
  /api/v1/products:
    get:
      tags:
        - Products
```

### 6. Define Response Schemas

```yaml
responses:
  '200':
    description: Successful response
    content:
      application/json:
        schema:
          $ref: '#/components/schemas/ApiResponse'
```

### 7. Keep OpenAPI Spec Updated

Whenever you modify your API:
1. Update OpenAPI spec
2. Re-import in WSO2 (overwrites existing)
3. Redeploy

---

## Updating Existing API from Swagger

To update an already imported API:

### Method 1: Via UI

1. Go to your API in Publisher Portal
2. Click **"API Configurations"** → **"API Definition"**
3. Click **"Edit"**
4. Choose:
   - **"Import URL"** - Enter new OpenAPI URL
   - **"Import File"** - Upload new file
5. Click **"Update"**
6. Review changes
7. Click **"Save"**
8. Redeploy the API

### Method 2: Via REST API

```bash
curl -k -X PUT "https://localhost:9443/api/am/publisher/v4/apis/$API_ID/swagger" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: multipart/form-data" \
  -F "url=http://localhost:8080/api-docs"
```

---

## Complete Example: Start to Finish

Here's a complete workflow:

```bash
# 1. Start Spring Boot
cd demo-api
mvn spring-boot:run

# 2. Verify OpenAPI is accessible
curl http://localhost:8080/api-docs | jq

# 3. Start WSO2 APIM (in another terminal)
cd wso2am-4.x.x/bin
./api-manager.sh

# Wait for "WSO2 Carbon started" message

# 4. Open browser and import
# https://localhost:9443/publisher
# CREATE API → Import Open API → OpenAPI URL
# Enter: http://localhost:8080/api-docs
# Configure: Context=/demo, Version=v1
# CREATE

# 5. Deploy
# Click "Deployments" → "Deploy New Revision"

# 6. Publish
# Click "Lifecycle" → "PUBLISH"

# 7. Subscribe (Developer Portal)
# https://localhost:9443/devportal
# Find your API → SUBSCRIBE
# Create Application → Generate Keys

# 8. Test
curl -k -H "Authorization: Bearer YOUR_TOKEN" \
     https://localhost:8243/demo/v1/products?category=Electronics
```

---

## Additional Resources

- [WSO2 APIM Documentation](https://apim.docs.wso2.com/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [WSO2 API Manager REST APIs](https://apim.docs.wso2.com/en/latest/reference/product-apis/overview/)
- [API Import/Export Tool](https://apim.docs.wso2.com/en/latest/install-and-setup/setup/api-controller/getting-started-with-wso2-api-controller/)

---

**You're now ready to create APIs in WSO2 APIM from Swagger/OpenAPI! 🚀**

The easiest method is **Method 1 (Import from URL)** - just make sure your Spring Boot app is running and point WSO2 to `http://localhost:8080/api-docs`.
