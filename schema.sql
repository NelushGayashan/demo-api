-- WSO2 Demo API Database Setup Script
-- This script manually creates the database schema
-- Note: Hibernate will auto-create tables, but this is for reference

-- Create database
CREATE DATABASE IF NOT EXISTS wso2_demo_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Use the database
USE wso2_demo_db;

-- Create products table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    price DOUBLE NOT NULL,
    category VARCHAR(255),
    stock INT,
    INDEX idx_category (category),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255),
    phone VARCHAR(255),
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample products
INSERT INTO products (name, description, price, category, stock) VALUES
('Laptop', 'High-performance laptop', 1299.99, 'Electronics', 15),
('Smartphone', 'Latest model smartphone', 899.99, 'Electronics', 30),
('Desk Chair', 'Ergonomic office chair', 249.99, 'Furniture', 20),
('Coffee Maker', 'Automatic coffee maker', 79.99, 'Appliances', 45),
('Book - Java Programming', 'Comprehensive Java guide', 49.99, 'Books', 100);

-- Insert sample users
INSERT INTO users (username, email, full_name, phone) VALUES
('john.doe', 'john@example.com', 'John Doe', '+1234567890'),
('jane.smith', 'jane@example.com', 'Jane Smith', '+1234567891'),
('bob.wilson', 'bob@example.com', 'Bob Wilson', '+1234567892');

-- Verify data
SELECT 'Products Table:' as '';
SELECT * FROM products;

SELECT 'Users Table:' as '';
SELECT * FROM users;

-- Show table structures
SELECT 'Products Table Structure:' as '';
DESCRIBE products;

SELECT 'Users Table Structure:' as '';
DESCRIBE users;
