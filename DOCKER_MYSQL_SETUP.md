# Docker MySQL Setup (Alternative to Local Installation)

If you prefer to run MySQL in Docker instead of installing it locally, follow this guide.

## 🐳 Prerequisites

- Docker Desktop installed
- Docker Compose installed (included with Docker Desktop)

## 🚀 Quick Start with Docker

### Step 1: Navigate to Project Directory

```bash
cd demo-api
```

### Step 2: Start MySQL Container

```bash
docker-compose up -d
```

This will start:
- **MySQL 8.0** on port 3306
- **phpMyAdmin** (web interface) on port 8081

### Step 3: Verify Containers Running

```bash
docker-compose ps
```

You should see:
```
NAME                   STATUS    PORTS
wso2-demo-mysql        Up        0.0.0.0:3306->3306/tcp
wso2-demo-phpmyadmin   Up        0.0.0.0:8081->80/tcp
```

### Step 4: Access Database

**Option 1: Using phpMyAdmin (Web Interface)**
1. Open browser: http://localhost:8081
2. Login:
   - Server: mysql
   - Username: root
   - Password: root
3. Select database: wso2_demo_db

**Option 2: Using MySQL Command Line**
```bash
docker exec -it wso2-demo-mysql mysql -u root -proot wso2_demo_db
```

### Step 5: Run Spring Boot Application

```bash
mvn spring-boot:run
```

## 🔧 Docker Configuration

### Database Credentials

The Docker setup creates:

**Root User:**
- Username: `root`
- Password: `root`

**Application User:**
- Username: `wso2demo`
- Password: `wso2demo123`

### Connection Details

```properties
# Use in application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/wso2_demo_db
spring.datasource.username=root
spring.datasource.password=root
```

## 🛠️ Docker Commands

### Start MySQL

```bash
docker-compose up -d
```

### Stop MySQL

```bash
docker-compose down
```

### Stop and Remove Data (Fresh Start)

```bash
docker-compose down -v
```

### View Logs

```bash
# MySQL logs
docker-compose logs mysql

# Follow logs in real-time
docker-compose logs -f mysql
```

### Restart MySQL

```bash
docker-compose restart mysql
```

### Check MySQL Status

```bash
docker-compose exec mysql mysqladmin -u root -proot status
```

## 📊 Managing Data

### Backup Database

```bash
docker-compose exec mysql mysqldump -u root -proot wso2_demo_db > backup.sql
```

### Restore Database

```bash
docker-compose exec -T mysql mysql -u root -proot wso2_demo_db < backup.sql
```

### Access MySQL Shell

```bash
docker-compose exec mysql mysql -u root -proot
```

Then run SQL commands:
```sql
USE wso2_demo_db;
SHOW TABLES;
SELECT * FROM products;
```

## 🔍 Troubleshooting Docker Setup

### Issue 1: Port 3306 Already in Use

**Error:** `Bind for 0.0.0.0:3306 failed: port is already allocated`

**Solution:**
1. Stop local MySQL service
2. Or change port in docker-compose.yml:
```yaml
ports:
  - "3307:3306"  # Use port 3307 instead
```

Then update application.properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:3307/wso2_demo_db
```

### Issue 2: Container Won't Start

**Solution:**
```bash
# Check logs
docker-compose logs mysql

# Remove and recreate
docker-compose down
docker-compose up -d --force-recreate
```

### Issue 3: Can't Connect from Spring Boot

**Solution:**
1. Check container is running: `docker-compose ps`
2. Check MySQL is ready: `docker-compose logs mysql | grep "ready for connections"`
3. Wait 30 seconds after `docker-compose up` for MySQL to fully initialize

### Issue 4: Lost Data After Restart

**Solution:**
- Data is persisted in Docker volume `mysql-data`
- To completely reset:
```bash
docker-compose down -v  # Removes volumes
docker-compose up -d    # Fresh start
```

## 🎯 Environment-Specific Configuration

### Development (Default)

```yaml
# docker-compose.yml
environment:
  MYSQL_ROOT_PASSWORD: root
  MYSQL_DATABASE: wso2_demo_db
```

### Production (Example)

Create `docker-compose.prod.yml`:
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
      MYSQL_DATABASE: wso2_demo_db
      MYSQL_USER: ${DB_USER}
      MYSQL_PASSWORD: ${DB_PASSWORD}
    volumes:
      - /path/to/persistent/storage:/var/lib/mysql
```

Run with:
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## 🔐 Security Considerations

1. **Change Default Passwords**
   - Don't use `root/root` in production
   - Use strong passwords

2. **Don't Expose phpMyAdmin in Production**
   - Remove or comment out phpMyAdmin service
   - Or restrict access using firewall

3. **Use Environment Variables**
   ```bash
   # .env file
   MYSQL_ROOT_PASSWORD=StrongPassword123!
   MYSQL_PASSWORD=AppPassword456!
   ```

4. **Network Isolation**
   - MySQL is only accessible from Spring Boot app
   - Not exposed to internet

## 📈 Performance Tuning

Edit `docker-compose.yml` to add MySQL configuration:

```yaml
mysql:
  image: mysql:8.0
  command: >
    --max-connections=200
    --innodb-buffer-pool-size=1G
    --query-cache-size=32M
  # ... rest of config
```

## 🧪 Testing with Docker

Run integration tests with Docker MySQL:

```bash
# Start test database
docker-compose up -d

# Run tests
mvn test

# Stop test database
docker-compose down
```

## 📚 Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [MySQL Docker Image](https://hub.docker.com/_/mysql)
- [Docker Compose Documentation](https://docs.docker.com/compose/)

## ✅ Checklist

- [ ] Docker Desktop installed and running
- [ ] `docker-compose up -d` executed successfully
- [ ] MySQL container running (check with `docker-compose ps`)
- [ ] phpMyAdmin accessible at http://localhost:8081
- [ ] Spring Boot application connects successfully
- [ ] Sample data visible in database

## 🎉 Benefits of Docker Approach

✅ **No Local Installation** - MySQL runs in container  
✅ **Consistent Environment** - Same setup across team  
✅ **Easy Cleanup** - `docker-compose down -v` removes everything  
✅ **Version Control** - Pin exact MySQL version  
✅ **Isolation** - Doesn't interfere with other MySQL installations  
✅ **phpMyAdmin Included** - Web-based database management  

---

**Happy Dockerizing! 🐳**

You now have MySQL running in Docker for your WSO2 Demo API!
