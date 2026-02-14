# MySQL Database Setup Guide

This guide will help you set up MySQL database for the WSO2 Demo API application.

## 📋 Prerequisites

- MySQL Server 8.0 or higher installed
- MySQL client or workbench (optional, for GUI management)

## 🚀 Installation

### Windows

1. Download MySQL Installer from [MySQL Official Site](https://dev.mysql.com/downloads/installer/)
2. Run the installer and choose "Developer Default"
3. Set root password during installation (remember this!)
4. Complete the installation

### macOS

```bash
# Using Homebrew
brew install mysql

# Start MySQL service
brew services start mysql

# Secure installation (set root password)
mysql_secure_installation
```

### Linux (Ubuntu/Debian)

```bash
# Update package index
sudo apt update

# Install MySQL Server
sudo apt install mysql-server

# Start MySQL service
sudo systemctl start mysql

# Secure installation
sudo mysql_secure_installation
```

### Linux (RedHat/CentOS)

```bash
# Install MySQL
sudo yum install mysql-server

# Start MySQL
sudo systemctl start mysqld

# Get temporary root password
sudo grep 'temporary password' /var/log/mysqld.log

# Secure installation
sudo mysql_secure_installation
```

## 🔧 Database Configuration

### Option 1: Auto-Create Database (Recommended)

The application is configured to automatically create the database if it doesn't exist.

**No manual database creation needed!**

Just update the credentials in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wso2_demo_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Option 2: Manual Database Creation

If you prefer to create the database manually:

#### Step 1: Connect to MySQL

```bash
# Connect as root
mysql -u root -p
```

#### Step 2: Create Database

```sql
-- Create database
CREATE DATABASE wso2_demo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Verify database created
SHOW DATABASES;

-- Use the database
USE wso2_demo_db;
```

#### Step 3: Create User (Optional - for better security)

```sql
-- Create a dedicated user
CREATE USER 'wso2demo'@'localhost' IDENTIFIED BY 'StrongPassword123!';

-- Grant privileges
GRANT ALL PRIVILEGES ON wso2_demo_db.* TO 'wso2demo'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;

-- Exit MySQL
EXIT;
```

If you create a dedicated user, update `application.properties`:

```properties
spring.datasource.username=wso2demo
spring.datasource.password=StrongPassword123!
```

## ⚙️ Application Configuration

### Default Configuration

The `application.properties` file is pre-configured with:

```properties
# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/wso2_demo_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### Configuration Options Explained

**`spring.jpa.hibernate.ddl-auto` options:**

- `update` (Recommended for development) - Updates schema, preserves data
- `create` - Drops and recreates schema on every startup
- `create-drop` - Creates schema on startup, drops on shutdown
- `validate` - Validates schema, no changes
- `none` - No schema management

**For Production:**
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

### Change Database Credentials

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Change Database Name

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/YOUR_DB_NAME?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
```

### Remote MySQL Server

If MySQL is on a different server:

```properties
spring.datasource.url=jdbc:mysql://192.168.1.100:3306/wso2_demo_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
```

## 🏃 Running the Application

### Step 1: Start MySQL Service

**Windows:**
- MySQL should auto-start
- Or open Services and start "MySQL80"

**macOS:**
```bash
brew services start mysql
```

**Linux:**
```bash
sudo systemctl start mysql
# or
sudo service mysql start
```

### Step 2: Verify MySQL is Running

```bash
mysql -u root -p -e "SELECT VERSION();"
```

### Step 3: Run Spring Boot Application

```bash
cd demo-api
mvn spring-boot:run
```

### Step 4: Check Database Tables Created

```bash
mysql -u root -p

USE wso2_demo_db;

SHOW TABLES;
```

You should see:
```
+-------------------------+
| Tables_in_wso2_demo_db  |
+-------------------------+
| products                |
| users                   |
+-------------------------+
```

### Step 5: View Sample Data

```sql
SELECT * FROM products;
SELECT * FROM users;
```

## 🔍 Database Schema

### Products Table

```sql
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    price DOUBLE NOT NULL,
    category VARCHAR(255),
    stock INT
);
```

### Users Table

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255),
    phone VARCHAR(255)
);
```

## 🛠️ Useful MySQL Commands

### View Database Information

```sql
-- Show all databases
SHOW DATABASES;

-- Select database
USE wso2_demo_db;

-- Show all tables
SHOW TABLES;

-- Describe table structure
DESCRIBE products;
DESCRIBE users;

-- Count records
SELECT COUNT(*) FROM products;
SELECT COUNT(*) FROM users;
```

### Query Data

```sql
-- Get all products
SELECT * FROM products;

-- Get products by category
SELECT * FROM products WHERE category = 'Electronics';

-- Get products sorted by price
SELECT * FROM products ORDER BY price DESC;

-- Search products by name
SELECT * FROM products WHERE name LIKE '%Laptop%';
```

### Manage Data

```sql
-- Insert new product
INSERT INTO products (name, description, price, category, stock) 
VALUES ('Monitor', '27-inch 4K monitor', 399.99, 'Electronics', 20);

-- Update product
UPDATE products SET price = 1199.99 WHERE id = 1;

-- Delete product
DELETE FROM products WHERE id = 5;

-- Clear all data (keep table structure)
TRUNCATE TABLE products;
TRUNCATE TABLE users;
```

### Database Management

```sql
-- Backup database
mysqldump -u root -p wso2_demo_db > backup.sql

-- Restore database
mysql -u root -p wso2_demo_db < backup.sql

-- Drop database (WARNING: Deletes all data!)
DROP DATABASE wso2_demo_db;
```

## 🐛 Troubleshooting

### Issue 1: Connection Refused

**Error:** `Communications link failure`

**Solution:**
```bash
# Check if MySQL is running
sudo systemctl status mysql   # Linux
brew services list            # macOS

# Start MySQL if not running
sudo systemctl start mysql    # Linux
brew services start mysql     # macOS
```

### Issue 2: Access Denied

**Error:** `Access denied for user 'root'@'localhost'`

**Solution:**
1. Verify password is correct
2. Reset root password if needed:

```bash
# Stop MySQL
sudo systemctl stop mysql

# Start in safe mode
sudo mysqld_safe --skip-grant-tables &

# Connect without password
mysql -u root

# Reset password
ALTER USER 'root'@'localhost' IDENTIFIED BY 'NewPassword123!';
FLUSH PRIVILEGES;

# Restart MySQL normally
sudo systemctl restart mysql
```

### Issue 3: Database Not Created

**Error:** `Unknown database 'wso2_demo_db'`

**Solution:**
1. Check URL has `createDatabaseIfNotExist=true`
2. Or create manually:
```sql
CREATE DATABASE wso2_demo_db;
```

### Issue 4: Character Encoding Issues

**Solution:**
```sql
-- Check database charset
SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME 
FROM information_schema.SCHEMATA 
WHERE SCHEMA_NAME = 'wso2_demo_db';

-- Recreate with proper charset
DROP DATABASE wso2_demo_db;
CREATE DATABASE wso2_demo_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

### Issue 5: Port Already in Use

**Error:** `Port 3306 already in use`

**Solution:**
```bash
# Check what's using port 3306
sudo lsof -i :3306        # macOS/Linux
netstat -ano | findstr 3306  # Windows

# Stop the conflicting service or change MySQL port
```

### Issue 6: Hibernate Schema Validation Failed

**Solution:**
- Set `spring.jpa.hibernate.ddl-auto=update` (not validate)
- Or manually create/fix tables to match entities

## 📊 Monitoring Database

### Using MySQL Workbench (GUI)

1. Download [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)
2. Create new connection:
   - Connection Name: WSO2 Demo Local
   - Hostname: localhost
   - Port: 3306
   - Username: root
3. Connect and explore database visually

### Using Command Line

```bash
# Monitor queries in real-time
mysql -u root -p -e "SHOW PROCESSLIST;"

# Check table sizes
mysql -u root -p -e "
SELECT 
    table_name AS 'Table',
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS 'Size (MB)'
FROM information_schema.TABLES
WHERE table_schema = 'wso2_demo_db'
ORDER BY (data_length + index_length) DESC;
"
```

## 🔐 Security Best Practices

1. **Don't use root in production**
   - Create dedicated database user
   - Grant only necessary privileges

2. **Use strong passwords**
   - Minimum 12 characters
   - Mix of letters, numbers, symbols

3. **Enable SSL** (for production)
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/wso2_demo_db?useSSL=true&requireSSL=true
   ```

4. **Regular backups**
   ```bash
   # Automated daily backup
   mysqldump -u root -p wso2_demo_db > backup_$(date +%Y%m%d).sql
   ```

5. **Externalize credentials** (for production)
   - Use environment variables
   - Use Spring Cloud Config
   - Use secrets management tools (HashiCorp Vault, AWS Secrets Manager)

## 🎯 Next Steps

1. ✅ MySQL installed and running
2. ✅ Database configured in `application.properties`
3. ✅ Application running and connected to MySQL
4. ✅ Tables auto-created by Hibernate
5. ✅ Sample data loaded

Now you can:
- Test APIs through Swagger UI: http://localhost:8080/swagger-ui.html
- View data in MySQL Workbench or command line
- Integrate with WSO2 API Manager (follow main README.md)

## 📚 Additional Resources

- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)

---

**Happy Coding! 🚀**

Your Spring Boot application is now using MySQL database for persistent storage!
