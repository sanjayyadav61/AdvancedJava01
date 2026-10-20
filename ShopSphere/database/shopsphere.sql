-- ====================================================================
-- SHOPSPHERE - RTU Advanced Java E-Commerce Capstone Project
-- Complete Database Schema & Seed Data
-- Database Engine: MySQL 8.0+ / Compatible with MariaDB & H2 (MySQL Mode)
-- ====================================================================

CREATE DATABASE IF NOT EXISTS shopsphere CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopsphere;

-- --------------------------------------------------------------------
-- 1. Table: users
-- Roles: 'CUSTOMER', 'ADMIN'
-- Status: 'ACTIVE', 'INACTIVE', 'BLOCKED'
-- --------------------------------------------------------------------
DROP TABLE IF EXISTS coupon_usage;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS coupons;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- SHA-256 Hashed Password
    mobile VARCHAR(20),
    role VARCHAR(20) DEFAULT 'CUSTOMER', -- 'CUSTOMER' or 'ADMIN'
    status VARCHAR(20) DEFAULT 'ACTIVE', -- 'ACTIVE', 'BLOCKED'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------------------
-- 2. Table: addresses
-- Address types: 'HOME', 'WORK', 'OTHER'
-- --------------------------------------------------------------------
CREATE TABLE addresses (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_line VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(20) NOT NULL,
    address_type VARCHAR(20) DEFAULT 'HOME',
    is_default BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- --------------------------------------------------------------------
-- 3. Table: categories
-- --------------------------------------------------------------------
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    icon_class VARCHAR(50) DEFAULT 'bi bi-grid',
    status BOOLEAN DEFAULT TRUE
);

-- --------------------------------------------------------------------
-- 4. Table: products
-- --------------------------------------------------------------------
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(5, 2) DEFAULT 0.00, -- Percentage discount (e.g. 15.00)
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(500),
    status BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT
);

-- --------------------------------------------------------------------
-- 5. Table: cart
-- --------------------------------------------------------------------
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_product (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- --------------------------------------------------------------------
-- 6. Table: wishlist
-- --------------------------------------------------------------------
CREATE TABLE wishlist (
    wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_wish_user_product (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- --------------------------------------------------------------------
-- 7. Table: coupons
-- --------------------------------------------------------------------
CREATE TABLE coupons (
    coupon_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    discount_type VARCHAR(20) DEFAULT 'PERCENTAGE', -- 'PERCENTAGE', 'FLAT'
    discount_value DECIMAL(10, 2) NOT NULL,
    minimum_order DECIMAL(10, 2) DEFAULT 0.00,
    maximum_discount DECIMAL(10, 2) DEFAULT 9999.00,
    expiry_date DATE,
    status BOOLEAN DEFAULT TRUE
);

-- --------------------------------------------------------------------
-- 8. Table: orders
-- Statuses: 'PLACED', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'
-- --------------------------------------------------------------------
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_id INT,
    total_amount DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(10, 2) DEFAULT 0.00,
    payment_method VARCHAR(50) DEFAULT 'CARD', -- 'CARD', 'UPI', 'NETBANKING', 'COD'
    payment_status VARCHAR(30) DEFAULT 'PENDING', -- 'PENDING', 'PAID', 'FAILED'
    order_status VARCHAR(30) DEFAULT 'PLACED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES addresses(address_id) ON DELETE SET NULL
);

-- --------------------------------------------------------------------
-- 9. Table: order_items
-- --------------------------------------------------------------------
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL, -- Historical price at checkout
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- --------------------------------------------------------------------
-- 10. Table: payments
-- --------------------------------------------------------------------
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_reference VARCHAR(100) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(30) DEFAULT 'SUCCESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- --------------------------------------------------------------------
-- 11. Table: reviews
-- --------------------------------------------------------------------
CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- --------------------------------------------------------------------
-- 12. Table: coupon_usage
-- --------------------------------------------------------------------
CREATE TABLE coupon_usage (
    usage_id INT AUTO_INCREMENT PRIMARY KEY,
    coupon_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- ====================================================================
-- SEED DATA
-- Default Passwords:
-- admin123    -> 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- password123 -> ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f
-- ====================================================================

-- Users: Admin & Customers
INSERT INTO users (user_id, name, email, password, mobile, role, status) VALUES
(1, 'Administrator', 'admin@shopsphere.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', '9876543210', 'ADMIN', 'ACTIVE'),
(2, 'Rahul Sharma', 'customer@shopsphere.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', '9812345678', 'CUSTOMER', 'ACTIVE'),
(3, 'Priya Patel', 'priya@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', '9898989898', 'CUSTOMER', 'ACTIVE');

-- Addresses
INSERT INTO addresses (address_id, user_id, address_line, city, state, pincode, address_type, is_default) VALUES
(1, 2, 'Flat 402, Sunshine Heights, Malviya Nagar', 'Jaipur', 'Rajasthan', '302017', 'HOME', TRUE),
(2, 2, 'Tech Park Phase 2, Sitapura', 'Jaipur', 'Rajasthan', '302022', 'WORK', FALSE),
(3, 3, '12-B Green Avenue, Civil Lines', 'Kota', 'Rajasthan', '324001', 'HOME', TRUE);

-- Categories
INSERT INTO categories (category_id, name, description, icon_class, status) VALUES
(1, 'Electronics', 'Laptops, Smartphones, Audio, Smart Watches and Accessories', 'bi bi-laptop', TRUE),
(2, 'Fashion', 'Men & Women Apparel, Footwear and Premium Accessories', 'bi bi-bag-check', TRUE),
(3, 'Home & Living', 'Furniture, Kitchen Appliances, Decor and Bedding', 'bi bi-house-door', TRUE),
(4, 'Books & Media', 'Academic, Technical, Novels, RTU Reference Guides & Self-Help', 'bi bi-book', TRUE),
(5, 'Fitness & Sports', 'Workout gear, dumbbells, smart fitness bands and outdoor sports', 'bi bi-heart-pulse', TRUE);

-- Products
INSERT INTO products (product_id, category_id, name, brand, description, price, discount, stock, image_url, status) VALUES
(1, 1, 'Apple MacBook Air M3 (16GB, 512GB)', 'Apple', '13.6-inch Liquid Retina Display, 18 hours battery life, 8-core CPU and 10-core GPU in midnight finish.', 114900.00, 10.00, 15, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?auto=format&fit=crop&w=800&q=80', TRUE),
(2, 1, 'Sony WH-1000XM5 Wireless Headphones', 'Sony', 'Industry-leading noise canceling with Auto NC Optimizer, crystal clear hands-free calling and 30-hour battery life.', 26990.00, 15.00, 24, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=800&q=80', TRUE),
(3, 1, 'Samsung Galaxy S24 Ultra 5G', 'Samsung', '200MP Camera with Galaxy AI, 12GB RAM, 256GB Storage, Titanium Gray with integrated S-Pen.', 129999.00, 8.00, 12, 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?auto=format&fit=crop&w=800&q=80', TRUE),
(4, 1, 'Logitech MX Master 3S Wireless Mouse', 'Logitech', 'Quiet clicks, 8K DPI tracking on glass, MagSpeed electromagnetic scrolling, ergonomic silhouette.', 8995.00, 12.00, 30, 'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?auto=format&fit=crop&w=800&q=80', TRUE),
(5, 2, 'Nike Air Zoom Pegasus 40 Running Shoes', 'Nike', 'Responsive cushioning in everyday runners with dual Zoom Air units and engineered breathable mesh.', 8995.00, 20.00, 18, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=800&q=80', TRUE),
(6, 2, 'Levi''s 511 Slim Fit Jeans', 'Levi''s', 'Classic slim fit with room to move, stretch denim fabric in authentic dark indigo wash.', 2799.00, 25.00, 40, 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?auto=format&fit=crop&w=800&q=80', TRUE),
(7, 2, 'Fossil Gen 6 Smartwatch Smoke Stainless', 'Fossil', 'Snapdragon 4100+ platform, heart rate and SpO2 tracking, rapid charging, customizable watch dials.', 14995.00, 30.00, 10, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=800&q=80', TRUE),
(8, 3, 'Philips Hue Smart Ambient Light Bar', 'Philips', 'Millions of colors and warm-to-cool white light, voice control sync with Alexa and Google Assistant.', 4599.00, 10.00, 22, 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=800&q=80', TRUE),
(9, 3, 'Nespresso Vertuo Pop Espresso Machine', 'Nespresso', 'One-touch brewing for 4 cup sizes, patented Centrifusion technology for rich crema in compact design.', 11999.00, 15.00, 8, 'https://images.unsplash.com/photo-1517668808822-9ebb02ae2a0e?auto=format&fit=crop&w=800&q=80', TRUE),
(10, 4, 'Core Java: Advanced Features (Volume II)', 'Cay S. Horstmann', 'Comprehensive coverage of Swing, Networking, RMI, JDBC, Annotations, XML and Java Security.', 950.00, 15.00, 50, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=800&q=80', TRUE),
(11, 4, 'Head First Servlets and JSP (2nd Edition)', 'Bryan Basham', 'Pass the Sun Certified Web Component Developer exam; covers Servlets, JSTL, Filters and MVC.', 820.00, 10.00, 35, 'https://images.unsplash.com/photo-1532012164546-f432f2e3777f?auto=format&fit=crop&w=800&q=80', TRUE),
(12, 5, 'Adjustable Dumbbell Set (24kg)', 'Kobo', 'Quick dial weight adjustment from 2.5kg to 24kg with compact tray, non-slip textured knurled grip.', 7499.00, 20.00, 5, 'https://images.unsplash.com/photo-1584735935682-2f2b69dff9d2?auto=format&fit=crop&w=800&q=80', TRUE),
(13, 1, 'Apple iPad Air 11-inch M2', 'Apple', 'Liquid Retina display with P3 wide color, Touch ID, landscape 12MP front camera with Center Stage.', 59900.00, 5.00, 14, 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?auto=format&fit=crop&w=800&q=80', TRUE),
(14, 2, 'Ray-Ban Classic Aviator Sunglasses', 'Ray-Ban', 'Gold metal frame with classic green G-15 crystal lenses offering 100% UV protection.', 7890.00, 10.00, 25, 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?auto=format&fit=crop&w=800&q=80', TRUE),
(15, 3, 'Ergonomic Mesh Office Chair', 'Green Soul', 'High back with breathable mesh, 3D adjustable armrests, heavy-duty metal base and lumbar support.', 8999.00, 25.00, 4, 'https://images.unsplash.com/photo-1580481077195-c3a821a5060f?auto=format&fit=crop&w=800&q=80', TRUE),
(16, 5, 'Yoga Mat Anti-Slip with Alignment Lines', 'Boldfit', '6mm high density TPE eco-friendly workout mat with carry strap, waterproof and tear-resistant.', 999.00, 30.00, 45, 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?auto=format&fit=crop&w=800&q=80', TRUE);

-- Coupons
INSERT INTO coupons (coupon_id, code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) VALUES
(1, 'WELCOME50', 'FLAT', 50.00, 500.00, 50.00, '2027-12-31', TRUE),
(2, 'FESTIVE20', 'PERCENTAGE', 20.00, 1000.00, 1500.00, '2027-12-31', TRUE),
(3, 'SUPER100', 'FLAT', 100.00, 1500.00, 100.00, '2027-12-31', TRUE),
(4, 'TECH10', 'PERCENTAGE', 10.00, 5000.00, 5000.00, '2027-12-31', TRUE);

-- Reviews
INSERT INTO reviews (review_id, user_id, product_id, rating, review_text) VALUES
(1, 2, 1, 5, 'Exceptional performance and battery life! Runs Android Studio and Eclipse simultaneously without heating.'),
(2, 3, 2, 5, 'The active noise cancellation is magical during train commutes. Very comfortable ear cushions.'),
(3, 2, 10, 5, 'Best book for RTU Advanced Java examination. Explains RMI, Sockets and Swing with great clarity.'),
(4, 3, 5, 4, 'Great cushioning for morning 5K runs. Very responsive Zoom Air cushioning.');

-- Sample Orders
INSERT INTO orders (order_id, user_id, address_id, total_amount, discount, payment_method, payment_status, order_status, created_at) VALUES
(1001, 2, 1, 22941.50, 50.00, 'CARD', 'PAID', 'DELIVERED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(1002, 2, 1, 950.00, 0.00, 'UPI', 'PAID', 'SHIPPED', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1003, 3, 3, 7196.00, 100.00, 'NETBANKING', 'PAID', 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- Order Items
INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price) VALUES
(1, 1001, 2, 1, 22941.50),
(2, 1002, 10, 1, 950.00),
(3, 1003, 5, 1, 7196.00);

-- Payments
INSERT INTO payments (payment_id, order_id, payment_method, transaction_reference, amount, payment_status) VALUES
(1, 1001, 'CARD', 'TXN-CARD-9921448', 22941.50, 'SUCCESS'),
(2, 1002, 'UPI', 'UPI-REF-8831920', 950.00, 'SUCCESS'),
(3, 1003, 'NETBANKING', 'NETB-REF-1192837', 7196.00, 'SUCCESS');

-- Wishlist item for demonstration
INSERT INTO wishlist (wishlist_id, user_id, product_id) VALUES
(1, 2, 4),
(2, 2, 7);

-- Cart item for demonstration
INSERT INTO cart (cart_id, user_id, product_id, quantity) VALUES
(1, 2, 11, 2);
