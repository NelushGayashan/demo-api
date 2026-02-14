# Quick Start Guide - 15 Minutes to Running API

## Prerequisites Check
- [ ] Java 17+ installed: `java -version`
- [ ] Maven 3.6+ installed: `mvn -version`
- [ ] WSO2 APIM 4.x downloaded and extracted

## Step 1: Start Spring Boot App (2 minutes)

```bash
cd demo-api
mvn spring-boot:run
```

Wait for: "Started Wso2ApimDemoApplication"

**Verify:** Open http://localhost:8080/swagger-ui.html

## Step 2: Start WSO2 APIM (3 minutes)

```bash
cd wso2am-4.x.x/bin
./api-manager.sh    # Linux/Mac
# OR
api-manager.bat     # Windows
```

Wait for: "WSO2 Carbon started"

**Verify:** Open https://localhost:9443/publisher

## Step 3: Create API in WSO2 (5 minutes)

### 3.1 Import OpenAPI Spec
1. Login to Publisher: https://localhost:9443/publisher
   - Username: `admin`
   - Password: `admin`

2. Click **"CREATE API"** → **"Import Open API"**

3. Choose **"OpenAPI URL"** and enter:
   ```
   http://localhost:8080/api-docs
   ```

4. Click **"NEXT"**

### 3.2 Configure API
- **Name**: Demo API
- **Context**: /demo
- **Version**: v1
- **Endpoint**: http://localhost:8080
- **Business Plans**: Select "Unlimited"

Click **"CREATE"**

### 3.3 Deploy
1. Click **"Deployments"** (left menu)
2. Click **"Deploy New Revision"**
3. Select **"Default"** gateway
4. Click **"Deploy"**

### 3.4 Publish
1. Click **"Lifecycle"** (left menu)
2. Click **"PUBLISH"**

## Step 4: Subscribe & Get Token (3 minutes)

### 4.1 Create Application
1. Go to Developer Portal: https://localhost:9443/devportal
2. Login: `admin` / `admin`
3. Click **"Applications"** → **"ADD NEW APPLICATION"**
4. Name: **TestApp**, Tier: **Unlimited**
5. Click **"SAVE"**

### 4.2 Subscribe
1. Click **"APIs"** → Select **"Demo API"**
2. Click **"SUBSCRIBE"**
3. Application: **TestApp**, Tier: **Unlimited**
4. Click **"SUBSCRIBE"**

### 4.3 Generate Token
1. Go to **"Applications"** → **"TestApp"**
2. Click **"Production Keys"** tab
3. Click **"GENERATE KEYS"**
4. **Copy the Access Token** (you'll need this!)

## Step 5: Test API Calls (2 minutes)

### Test 1: Direct Call (No Auth)
```bash
curl http://localhost:8080/api/v1/products
```

### Test 2: Through WSO2 Gateway (With Auth)
```bash
# Replace YOUR_TOKEN with the token you copied
curl -k -H "Authorization: Bearer YOUR_TOKEN" \
     https://localhost:8243/demo/v1/products
```

### Test 3: Create Product
```bash
curl -k -X POST https://localhost:8243/demo/v1/products \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Product",
    "description": "Created through WSO2 Gateway",
    "price": 99.99,
    "category": "Test",
    "stock": 10
  }'
```

## ✅ Success Checklist

- [ ] Spring Boot running on port 8080
- [ ] Swagger UI accessible
- [ ] WSO2 APIM running on port 9443
- [ ] API created and published in WSO2
- [ ] Application created and subscribed
- [ ] Access token generated
- [ ] API calls through gateway working

## 🎉 You're Done!

You now have:
- A working Spring Boot REST API
- WSO2 APIM managing and securing your API
- OAuth2 authentication in place
- Rate limiting configured

## Next Steps

1. **Explore Swagger UI**: http://localhost:8080/swagger-ui.html
2. **View Analytics**: Publisher Portal → Your API → Analytics
3. **Try Rate Limiting**: Make 20+ rapid requests to trigger throttling
4. **Read Full Tutorial**: See `WSO2_CONCEPTS.md` for in-depth guide

## Common Issues

**Port already in use:**
```bash
# Check what's using port 8080
lsof -i :8080  # Mac/Linux
netstat -ano | findstr :8080  # Windows

# Kill the process or change Spring Boot port in application.properties
```

**WSO2 not starting:**
- Ensure Java 11+ is installed
- Check JAVA_HOME is set correctly
- Look at logs: `wso2am-4.x.x/repository/logs/wso2carbon.log`

**Token not working:**
- Verify token is not expired (default 1 hour)
- Check Authorization header format: `Bearer YOUR_TOKEN`
- Try regenerating the token

## Need Help?

Check these files:
- `README.md` - Complete documentation
- `WSO2_CONCEPTS.md` - Detailed concepts and tutorials
- `API_REQUESTS.md` - More example API calls

Happy API Managing! 🚀
