# WSO2 API Manager - Key Concepts & Tutorial

## 📚 Core Concepts

### 1. API Gateway
An API Gateway is a server that acts as an intermediary between clients and backend services.

**Benefits:**
- **Single Entry Point**: All API requests go through one gateway
- **Security**: Centralized authentication and authorization
- **Rate Limiting**: Protect backend from overload
- **Monitoring**: Track all API usage
- **Transformation**: Modify requests/responses as needed
- **Caching**: Improve performance

**How it works:**
```
Client → WSO2 Gateway → Your Spring Boot API
         (Port 8243)    (Port 8080)
```

### 2. API Lifecycle States

APIs in WSO2 APIM go through different states:

1. **CREATED** - API is created but not ready for use
2. **PROTOTYPED** - Early version for testing (no subscriptions needed)
3. **PUBLISHED** - Available in Developer Portal for subscriptions
4. **DEPRECATED** - Still available but marked for retirement
5. **RETIRED** - No longer available for new subscriptions
6. **BLOCKED** - Temporarily disabled

### 3. OAuth 2.0 Authentication Flow

WSO2 APIM uses OAuth 2.0 for API security:

```
1. Developer registers application in Developer Portal
2. Application gets Client ID and Client Secret
3. Developer requests Access Token using credentials
4. WSO2 APIM validates and issues Access Token
5. Developer uses Access Token in API calls
6. Gateway validates token before forwarding to backend
```

**Token Types:**
- **Access Token**: Short-lived (1 hour default), used for API calls
- **Refresh Token**: Long-lived, used to get new access tokens

### 4. Throttling (Rate Limiting)

Control how many requests can be made in a time period.

**Throttling Tiers:**
- **Unlimited**: No limits (for testing)
- **Gold**: 20,000 requests/minute
- **Silver**: 5,000 requests/minute  
- **Bronze**: 1,000 requests/minute
- **Custom**: Define your own limits

**Throttling Levels:**
- **Application Level**: Limit per application
- **Subscription Level**: Limit per API subscription
- **Resource Level**: Limit per API endpoint
- **User Level**: Limit per user

### 5. Applications

An **Application** is a logical collection of APIs that a developer uses. Each application gets its own credentials.

**Use Case:**
- Web App uses "WebApplication" with one set of keys
- Mobile App uses "MobileApplication" with different keys
- This allows separate rate limits and analytics

### 6. Subscriptions

A **Subscription** connects an Application to an API with a specific throttling tier.

```
Application → Subscription (with tier) → API
```

### 7. API Resources

Individual endpoints in your API:
- `GET /api/v1/products`
- `POST /api/v1/products`
- `GET /api/v1/users/{id}`

Each resource can have its own:
- Authentication requirements
- Rate limits
- Request/response transformations

## 🎓 Step-by-Step Tutorial

### Phase 1: Setup and Basic API Creation

#### Step 1.1: Start Spring Boot Application
```bash
cd demo-api
mvn spring-boot:run
```
Verify at: http://localhost:8080/swagger-ui.html

#### Step 1.2: Start WSO2 APIM
```bash
cd wso2am-4.x.x/bin
./api-manager.sh  # or api-manager.bat on Windows
```

Wait for "WSO2 Carbon started" message (takes 2-3 minutes)

#### Step 1.3: Access WSO2 Portals
- **Publisher**: https://localhost:9443/publisher (create APIs)
- **Developer**: https://localhost:9443/devportal (consume APIs)
- **Admin**: https://localhost:9443/admin (manage system)

Login: `admin` / `admin`

#### Step 1.4: Create API from OpenAPI Spec

1. Go to Publisher Portal
2. Click **"CREATE API"**
3. Select **"Import Open API"**
4. Choose **"OpenAPI URL"**
5. Enter: `http://localhost:8080/api-docs`
6. Click **"NEXT"**

7. Fill in API details:
   - **Name**: Product User API
   - **Context**: /demo
   - **Version**: v1
   - **Endpoint**: http://localhost:8080
   
8. Click **"CREATE"**

#### Step 1.5: Configure API

In the API Overview:

**Endpoint Configuration:**
1. Go to **"Develop"** → **"Endpoints"**
2. Verify Production Endpoint: `http://localhost:8080`
3. Click **"Save"**

**Business Plans:**
1. Go to **"Develop"** → **"API Configurations"** → **"Business Plans"**
2. Select throttling tiers: Unlimited, Gold, Silver
3. Click **"Save"**

#### Step 1.6: Deploy API

1. Click **"Deployments"** in left menu
2. Click **"Deploy New Revision"**
3. Description: "Initial deployment"
4. Select **"Default"** gateway
5. Click **"Deploy"**

#### Step 1.7: Publish API

1. Click **"Lifecycle"** in left menu
2. Current state shows: CREATED
3. Click **"PUBLISH"**
4. API is now available in Developer Portal

### Phase 2: Subscribe and Test

#### Step 2.1: Create Application (Developer Portal)

1. Go to Developer Portal: https://localhost:9443/devportal
2. Login: `admin` / `admin`
3. Click **"Applications"** in top menu
4. Click **"ADD NEW APPLICATION"**
5. Fill in:
   - **Name**: MyDemoApp
   - **Throttling Tier**: Unlimited
   - **Description**: Demo application
6. Click **"SAVE"**

#### Step 2.2: Subscribe to API

1. In Developer Portal, click **"APIs"**
2. Find **"Product User API"**
3. Click on it to view details
4. Click **"SUBSCRIBE"** button
5. Select:
   - **Application**: MyDemoApp
   - **Throttling Tier**: Unlimited
6. Click **"SUBSCRIBE"**

#### Step 2.3: Generate Keys

1. Go to **"Applications"** → **"MyDemoApp"**
2. Click **"Production Keys"** tab
3. Select **"OAuth2 Tokens"**
4. Click **"GENERATE KEYS"**

You'll see:
- **Consumer Key** (Client ID)
- **Consumer Secret** (Client Secret)  
- **Access Token** (copy this!)

#### Step 2.4: Test API Calls

**Direct to Spring Boot (no auth):**
```bash
curl http://localhost:8080/api/v1/products
```

**Through WSO2 Gateway (with auth):**
```bash
# Replace YOUR_TOKEN with actual token
curl -k -H "Authorization: Bearer YOUR_TOKEN" \
     https://localhost:8243/demo/v1/products
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Laptop",
      "price": 1299.99,
      ...
    }
  ],
  "timestamp": "2026-02-14T..."
}
```

### Phase 3: Advanced Features

#### Step 3.1: Add API Documentation

1. In Publisher, go to your API
2. Click **"Develop"** → **"API Definition"**
3. Add descriptions to operations
4. Add request/response examples
5. Click **"Save"**

Developers will see this in the Developer Portal

#### Step 3.2: Configure Resource-Level Throttling

1. Go to **"Develop"** → **"Resources"**
2. Click on a resource (e.g., POST /products)
3. Expand **"Operation Level"** section
4. Set **"Rate Limiting Level"**: API & Resource Level
5. Choose throttling tier
6. Click **"Save"**

#### Step 3.3: Add Request/Response Mediation

Add custom logic before/after API calls:

1. Go to **"Develop"** → **"Message Mediation"**
2. **Request Mediation**: Transform incoming requests
3. **Response Mediation**: Transform outgoing responses

Example: Add custom header
```xml
<sequence xmlns="http://ws.apache.org/ns/synapse" name="custom-header">
    <property name="Custom-Header" value="MyValue" scope="transport"/>
</sequence>
```

#### Step 3.4: Enable Response Caching

1. Go to **"Develop"** → **"Resources"**
2. Select a GET resource
3. Expand **"Response Caching"** section
4. Enable caching
5. Set timeout (e.g., 300 seconds)
6. Click **"Save"**

This caches GET responses to improve performance

#### Step 3.5: Test Rate Limiting

Create a script to test throttling:

```bash
#!/bin/bash
TOKEN="your_token_here"

for i in {1..30}
do
   echo "Request #$i"
   curl -k -H "Authorization: Bearer $TOKEN" \
        https://localhost:8243/demo/v1/products \
        -w " - HTTP %{http_code}\n"
   sleep 0.1
done
```

If you set a low limit (e.g., 10/min), you'll see:
- First 10 requests: HTTP 200
- Next requests: HTTP 429 (Too Many Requests)

### Phase 4: API Versioning

#### Step 4.1: Create New API Version

1. In Publisher, go to your API
2. Click **"Create New Version"**
3. Enter new version: `v2`
4. New context: `/demo/v2`
5. Click **"CREATE"**

#### Step 4.2: Modify v2 API

1. Open the v2 API
2. Make changes (e.g., add new resources, modify responses)
3. Deploy and Publish v2

#### Step 4.3: Test Both Versions

```bash
# v1 API call
curl -k -H "Authorization: Bearer $TOKEN" \
     https://localhost:8243/demo/v1/products

# v2 API call  
curl -k -H "Authorization: Bearer $TOKEN" \
     https://localhost:8243/demo/v2/products
```

Both versions work simultaneously!

#### Step 4.4: Deprecate Old Version

1. Go to v1 API
2. Click **"Lifecycle"**
3. Click **"DEPRECATE"**
4. Existing subscriptions still work
5. New subscriptions are disabled

### Phase 5: Analytics and Monitoring

#### Step 5.1: View API Analytics

1. Go to Publisher Portal
2. Select your API
3. Click **"Analytics"** in left menu

View:
- Total API calls
- Success vs Error rate
- Response times
- Top users
- Usage over time

#### Step 5.2: View Application Analytics

1. Go to Developer Portal
2. Go to **"Applications"** → **"MyDemoApp"**
3. Click **"Statistics"** tab

View:
- API usage per application
- Request patterns
- Error statistics

#### Step 5.3: Real-time Monitoring

1. Go to Admin Portal: https://localhost:9443/admin
2. Click **"Settings"** → **"Monitoring"**
3. View real-time API statistics

### Phase 6: Security Advanced

#### Step 6.1: Scope Management (Fine-grained Access)

Define what each token can access:

1. In Publisher, go to API
2. Click **"Develop"** → **"Resources"**
3. For each resource, add scopes:
   - `product:read` for GET operations
   - `product:write` for POST/PUT operations
   - `product:delete` for DELETE operations

4. In Developer Portal, when generating keys:
   - Request specific scopes
   - Token will only work for those operations

#### Step 6.2: IP Whitelisting

Restrict API access by IP:

1. Go to Admin Portal
2. **Advanced** → **"Throttling Policies"**
3. Create IP-based policy
4. Apply to API or application

#### Step 6.3: Mutual SSL

Require client certificates:

1. In Publisher, go to API
2. **"Develop"** → **"Runtime"** → **"Transport Level Security"**
3. Enable **"Mutual SSL"**
4. Upload client certificates
5. Only clients with valid certs can access

## 🎯 Common Use Cases

### Use Case 1: Public API with Rate Tiers

**Scenario**: Offer free, basic, and premium API access

**Implementation:**
1. Create throttling tiers:
   - Free: 100 req/day
   - Basic: 10,000 req/day ($10/month)
   - Premium: Unlimited ($100/month)

2. Assign tiers to API in Publisher
3. Developers choose tier when subscribing
4. WSO2 enforces limits automatically

### Use Case 2: Internal Microservices Gateway

**Scenario**: Secure internal APIs between services

**Implementation:**
1. Create APIs for each microservice
2. Use JWT tokens for authentication
3. Service-to-service calls go through gateway
4. Enable mutual SSL for extra security

### Use Case 3: Partner API Integration

**Scenario**: Expose APIs to business partners

**Implementation:**
1. Create separate applications per partner
2. Assign high throttling limits
3. Enable analytics to track partner usage
4. Use scopes to limit access per partner

### Use Case 4: API Monetization

**Scenario**: Charge for API usage

**Implementation:**
1. Create billing plans in Admin Portal
2. Assign prices to throttling tiers
3. Enable billing for applications
4. WSO2 tracks usage for invoicing

## 🔍 Troubleshooting

### Problem: Cannot connect to backend

**Solution:**
- Check Spring Boot is running on port 8080
- Verify endpoint URL in WSO2 Publisher
- Check firewall/network connectivity
- Test direct call: `curl http://localhost:8080/api/v1/health`

### Problem: 401 Unauthorized through gateway

**Solution:**
- Verify token is not expired (default 1 hour)
- Check Authorization header format: `Bearer YOUR_TOKEN`
- Regenerate keys if needed
- Ensure API is subscribed to your application

### Problem: 429 Rate Limit Exceeded

**Solution:**
- This is expected if you exceed tier limits
- Upgrade to higher tier
- Wait for throttle window to reset
- Check analytics to see actual usage

### Problem: SSL Certificate errors

**Solution:**
- For testing, use `-k` flag with curl
- In production, import WSO2 certificate to trust store
- Or configure proper SSL certificates in WSO2

## 📊 Best Practices

1. **Use API Versioning**: Always version your APIs (/v1, /v2)
2. **Document Everything**: Add descriptions, examples in Publisher
3. **Start with Unlimited Tier**: For testing, use unlimited tier first
4. **Monitor Analytics**: Regularly check API usage patterns
5. **Use Scopes**: Implement fine-grained access control
6. **Enable Caching**: Cache GET responses when possible
7. **Test Throttling**: Verify rate limits work as expected
8. **Secure Endpoints**: Use HTTPS in production
9. **Rotate Keys**: Periodically regenerate application keys
10. **Plan for Deprecation**: Have migration strategy for old versions

## 🎓 Next Steps

1. **Try Advanced Policies**: Custom mediation sequences
2. **Integrate with Identity Providers**: LDAP, Active Directory
3. **Set up Multi-Gateway**: Deploy multiple gateway instances
4. **API Monetization**: Configure billing plans
5. **Custom Throttling**: Create complex throttling rules
6. **API Debugging**: Use trace logs to debug issues
7. **Performance Tuning**: Optimize gateway for high throughput

## 📚 Additional Resources

- [WSO2 APIM Quick Start Guide](https://apim.docs.wso2.com/en/latest/get-started/quick-start-guide/)
- [API Security Best Practices](https://apim.docs.wso2.com/en/latest/design/api-security/)
- [Throttling Guide](https://apim.docs.wso2.com/en/latest/design/rate-limiting/)
- [WSO2 Video Tutorials](https://www.youtube.com/wso2)

---

**Happy Learning! 🚀**

You now have a solid foundation for working with WSO2 API Manager!
