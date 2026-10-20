# ShopSphere — Distributed Java Enterprise E-Commerce Platform

[![Java 17+](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Jakarta EE 10](https://img.shields.io/badge/Jakarta%20EE-10-F80000?logo=jakartaee&logoColor=white)](https://jakarta.ee/)
[![Apache Tomcat 10.1](https://img.shields.io/badge/Apache%20Tomcat-10.1-F8DC75?logo=apachetomcat&logoColor=black)](https://tomcat.apache.org/)
[![MySQL 8.0](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![FlatLaf UI](https://img.shields.io/badge/Swing-FlatLaf%20Modern-5C6BC0)](https://www.formdev.com/flatlaf/)

> **ShopSphere** is a production-grade, distributed Java Enterprise & Desktop E-Commerce system designed to fulfill the **Rajasthan Technical University (RTU) Advanced Java / Java Programming Lab** curriculum with comprehensive real-world functionality.

---

## 🌟 Highlights & Key Features

* **3-Tier Enterprise Architecture**: Clean separation between Presentation Layer (JSP 3.1, JSTL 3.0, EL 5.0, Custom Tag Files), Business Logic Layer (Servlets 6.0, Services, Security Filters, Lifecycle Listeners), and Persistence Layer (Pure JDBC 4.2 DAOs, `PreparedStatement`, Atomic Transactions).
* **Dual Database Engine**: Native support for **MySQL 8.0+** with zero-configuration automated fallback to **Embedded H2 (MySQL Mode)** for instant offline lab demonstrations without setting up database servers.
* **Modern Desktop Administration**: Full **Java Swing MVC** desktop client powered by the **FlatLaf Modern Theme**, complete with dynamic Pluggable Look & Feel (Metal, Nimbus, Windows, FlatLaf Dark/Light), JTable sort/filter, product image preview, and live revenue metrics.
* **Distributed Inventory Sockets**: Multi-threaded TCP Socket Server (`java.net.ServerSocket`) on port `8088` communicating with a dedicated Swing client using serialized Java objects (`ObjectOutputStream`/`ObjectInputStream`).
* **Java RMI Remote Method Invocation**: Enterprise Remote Inventory Service bound to the `rmiregistry` on port `1099` implementing remote stock updates, low-stock alerts, and distributed pricing queries.
* **Interactive Syllabus Demonstration Hub**: Built-in `/shopsphere/demos` web portal showcasing live JSP Declarations, Scriptlets, Expressions, JSTL Core/Functions/XML parsing (`products_catalog.xml`), English/Hindi Internationalization (i18n), and an interactive Applet lifecycle simulator.

---

## 📋 Technology Stack

| Component | Technology | Version | Purpose |
| :--- | :--- | :--- | :--- |
| **Language** | Java (JDK) | 17+ (Tested on Java 26) | Core Runtime & Object-Oriented Framework |
| **Web Container** | Apache Tomcat Embedded | 10.1.20 | Jakarta EE 10 Servlet Container |
| **Servlet Engine** | Jakarta Servlet API | 6.0.0 | HTTP Request/Response Controllers & Filters |
| **Template Engine** | Jakarta Server Pages (JSP) | 3.1.0 | Server-side HTML View Rendering |
| **Tag Libraries** | Jakarta Standard Tag Library (JSTL) | 3.0.0 | Core, Functions, XML & SQL tags |
| **Desktop GUI** | Java Swing & AWT | Built-in | Desktop Administration Console |
| **GUI Theme** | FlatLaf (Modern Look & Feel) | 3.4.1 | Sleek Dark/Light Desktop User Experience |
| **Persistence** | JDBC & MySQL Connector/J | 8.3.0 | Relational Data Access & Atomic Transactions |
| **Fallback DB** | Embedded H2 Database | 2.2.224 | Zero-config standalone in-memory DB |
| **Build Tool** | Apache Maven | 3.9+ | Dependency management and WAR packaging |

---

## 🚀 Quick Start Guide

### Prerequisites
1. **JDK 17 or later** installed (`java -version`).
2. **Apache Maven 3.8+** installed (`mvn -version`).
3. *(Optional)* **MySQL 8.0+** running on `localhost:3306` with user `root` and password `root`. If MySQL is unavailable, ShopSphere will **automatically boot an embedded database** with all sample products and accounts pre-loaded!

---

### Running the Application Modules via Maven

#### 1. Compile and Package WAR
```cmd
mvn clean package
```
Generates the deployable enterprise archive `target/shopsphere.war`.

#### 2. Start Web Server (Tomcat)
```cmd
mvn exec:java
```
Access the application at: **[http://localhost:8080/shopsphere](http://localhost:8080/shopsphere)**

#### 3. Start Desktop Swing Administration
```cmd
mvn exec:java "-Dexec.mainClass=com.shopsphere.swing.SwingAdminLauncher"
```

#### 4. Start TCP Socket Inventory Server (Port 8088)
```cmd
mvn exec:java "-Dexec.mainClass=com.shopsphere.network.server.InventoryServer"
```

#### 5. Start TCP Socket Client
```cmd
mvn exec:java "-Dexec.mainClass=com.shopsphere.network.client.InventoryClient"
```

#### 6. Start Java RMI Server & Registry (Port 1099)
```cmd
mvn exec:java "-Dexec.mainClass=com.shopsphere.rmi.server.RmiServer"
```

#### 7. Start Java RMI Client
```cmd
mvn exec:java "-Dexec.mainClass=com.shopsphere.rmi.client.RmiClient"
```

#### 8. Run Educational Syllabus Demos (i18n, JNDI, Serialization)
```cmd
mvn exec:java "-Dexec.mainClass=com.shopsphere.demo.SerializationDemo"
mvn exec:java "-Dexec.mainClass=com.shopsphere.demo.I18nDemo"
mvn exec:java "-Dexec.mainClass=com.shopsphere.demo.JndiDemo"
mvn exec:java "-Dexec.mainClass=com.shopsphere.demo.UrlConnectionDemo"
mvn exec:java "-Dexec.mainClass=com.shopsphere.demo.AppletConceptDemo"
```

---

## 🔑 Default User Credentials

| Role | Email / Username | Password | Access Rights |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin@shopsphere.com` | `admin123` | Full Admin Dashboard, Inventory, Products, Categories, Orders, Coupons |
| **Customer** | `customer@shopsphere.com` | `password123` | Browse catalog, Wishlist, Cart, Checkout, Order History, Profile |
| **Sample Customer 2** | `priya@example.com` | `password123` | Additional verified test customer |

---

## 🛒 Coupon Codes for Checkout Testing

| Coupon Code | Discount | Minimum Spend | Description |
| :--- | :--- | :--- | :--- |
| `WELCOME50` | 50% OFF | ₹500 | New Customer Welcome Offer |
| `FESTIVE20` | 20% OFF | ₹1,000 | Festival Season Discount |
| `FLAT500` | ₹500 FLAT | ₹2,500 | Flat discount on orders above ₹2,500 |

---

## 📂 Project Architecture & Directory Layout

```
JavaProject/
├── pom.xml                                  <- Maven Project Object Model configuration
├── database/
│   └── shopsphere.sql                       <- Complete DDL schema & seed data (12 tables)
│
├── src/main/resources/
│   ├── db.properties                        <- MySQL & H2 fallback configuration
│   ├── messages_en.properties               <- English i18n resource bundle
│   ├── messages_hi.properties               <- Hindi i18n resource bundle
│   └── products_catalog.xml                 <- XML catalog for JSTL <x:parse> demo
│
├── src/main/java/com/shopsphere/
│   ├── ShopSphereWebServer.java             <- Embedded Tomcat runner
│   │
│   ├── model/                               <- Serializable Domain Entities (11 models)
│   │   ├── User.java, Product.java, Category.java, Order.java, OrderItem.java,
│   │   ├── CartItem.java, WishlistItem.java, Address.java, Payment.java,
│   │   └── Review.java, Coupon.java
│   │
│   ├── dao/                                 <- Pure JDBC Data Access Objects
│   │   ├── UserDAO.java, ProductDAO.java, CategoryDAO.java,
│   │   ├── OrderDAO.java (Atomic JDBC Transactions with rollback),
│   │   ├── CartDAO.java, WishlistDAO.java, AddressDAO.java,
│   │   └── PaymentDAO.java, ReviewDAO.java, CouponDAO.java
│   │
│   ├── service/                             <- Business Logic Services
│   │   ├── UserService.java, ProductService.java,
│   │   ├── CartService.java, CheckoutService.java, OrderService.java
│   │
│   ├── filter/                              <- Security & Encoding Filters
│   │   ├── AuthenticationFilter.java        <- Protects customer routes (/cart, /checkout, etc.)
│   │   ├── AdminFilter.java                 <- Protects /admin/* routes (HTTP 403 Forbidden)
│   │   └── CharacterEncodingFilter.java     <- Forces UTF-8 encoding
│   │
│   ├── listener/                            <- Servlet Context & Session Listeners
│   │   ├── AppListener.java                 <- ServletContextListener for lifecycle events
│   │   └── SessionListener.java             <- HttpSessionListener tracking active sessions
│   │
│   ├── controller/                          <- Customer & Public HTTP Servlets
│   │   ├── HomeServlet.java, ProductServlet.java, CartServlet.java,
│   │   ├── CheckoutServlet.java, OrderServlet.java, WishlistServlet.java,
│   │   ├── LoginServlet.java, RegisterServlet.java, LogoutServlet.java,
│   │   ├── ProfileServlet.java, SyllabusDemoServlet.java
│   │
│   ├── controller/admin/                    <- Admin HTTP Servlets
│   │   ├── AdminDashboardServlet.java, AdminProductServlet.java,
│   │   ├── AdminCategoryServlet.java, AdminOrderServlet.java,
│   │   ├── AdminUserServlet.java, AdminCouponServlet.java
│   │
│   ├── swing/                               <- Desktop Swing MVC Administration
│   │   ├── SwingAdminLauncher.java          <- Desktop entry point
│   │   ├── controller/                      <- Swing Controllers (LoginController, ProductController)
│   │   └── view/                            <- Swing Views (AdminDashboardFrame, LoginFrame, ProductFrame,
│   │                                           CategoryFrame, CustomerFrame, OrderFrame, InventoryFrame, ProductDialog)
│   │
│   ├── network/                             <- java.net Socket Client-Server
│   │   ├── protocol/NetworkMessage.java     <- Serializable message envelope
│   │   ├── server/InventoryServer.java      <- Multi-threaded TCP Server (port 8088)
│   │   └── client/InventoryClient.java      <- Swing socket client
│   │
│   ├── rmi/                                 <- Java Remote Method Invocation
│   │   ├── remote/InventoryService.java     <- java.rmi.Remote interface
│   │   ├── server/InventoryServiceImpl.java <- UnicastRemoteObject implementation
│   │   ├── server/RmiServer.java            <- RMI Registry binder (port 1099)
│   │   └── client/RmiClient.java            <- Interactive RMI client
│   │
│   ├── demo/                                <- RTU Syllabus Concept Demonstrators
│   │   ├── SerializationDemo.java           <- ObjectOutputStream / ObjectInputStream
│   │   ├── I18nDemo.java                    <- ResourceBundle & Locale
│   │   ├── JndiDemo.java                    <- JNDI InitialContext & DataSource lookup
│   │   ├── UrlConnectionDemo.java           <- java.net.URL & URLConnection
│   │   └── AppletConceptDemo.java           <- Applet lifecycle & modern web comparison
│   │
│   └── util/                                <- Utilities & Helpers
│       ├── DBConnection.java                <- JDBC Connection manager with H2 fallback
│       ├── DatabaseInitializer.java         <- DDL parser and database bootstrapper
│       ├── PasswordUtil.java                <- SHA-256 password hashing & verification
│       └── CurrencyFormatter.java           <- INR / Locale formatting
│
└── src/main/webapp/                         <- Presentation Layer (Jakarta JSP & Web Assets)
    ├── WEB-INF/
    │   ├── web.xml                          <- Deployment descriptor
    │   └── tags/                            <- JSP Tag Files
    │       ├── productCard.tag              <- Reusable product presentation component
    │       └── rating.tag                   <- Visual star rating component
    │
    ├── fragments/                           <- Reusable JSP partials
    │   ├── header.jsp                       <- HTML head, fonts, Bootstrap 5, FontAwesome
    │   ├── navbar.jsp                       <- Dynamic navigation bar with cart badges
    │   ├── footer.jsp                       <- System footer & RTU metadata
    │   └── sidebar.jsp                      <- Admin portal sidebar navigation
    │
    ├── css/style.css                        <- Modern custom CSS styling & animations
    ├── js/app.js                            <- Client-side validation & toast notifications
    │
    ├── index.jsp                            <- Homepage with hero banner & featured products
    ├── products.jsp                         <- Catalog browsing with filters, search & sort
    ├── product-details.jsp                  <- Detailed product view, reviews, add to cart
    ├── cart.jsp                             <- Interactive cart with quantity controls
    ├── checkout.jsp                         <- Shipping address, payment & coupon input
    ├── order-success.jsp                    <- Order confirmation with invoice details
    ├── orders.jsp                           <- Customer order history with tracking
    ├── order-details.jsp                    <- Detailed itemized order invoice
    ├── wishlist.jsp                         <- Customer saved wishlist items
    ├── profile.jsp                          <- User profile & address management
    ├── login.jsp                            <- Customer & Admin login form
    ├── register.jsp                         <- New customer registration form
    ├── syllabus-demos.jsp                   <- RTU Syllabus Demonstration portal
    │
    └── admin/                               <- Admin JSP Management Views
        ├── dashboard.jsp                    <- Analytics, revenue metrics, recent orders
        ├── products.jsp                     <- Product inventory table & modal CRUD
        ├── categories.jsp                   <- Category management
        ├── orders.jsp                       <- Order status updates (PENDING -> DELIVERED)
        ├── users.jsp                        <- Customer list & role management
        └── coupons.jsp                      <- Promotional coupon creation & tracking
```

---

## 📖 RTU Syllabus Mapping & Documentation

* **[RTU_SYLLABUS_MAPPING.md](file:///c:/Users/anike/Downloads/JavaProject/RTU_SYLLABUS_MAPPING.md)**: Exhaustive line-by-line mapping of every single RTU Advanced Java syllabus topic to the exact code files, classes, methods, and line numbers in this repository.
* **[PROJECT_REPORT.md](file:///c:/Users/anike/Downloads/JavaProject/PROJECT_REPORT.md)**: Full academic project report including Problem Statement, System Architecture, ER Diagrams, UML Sequence Diagrams, Module Descriptions, Test Cases, and 50+ Viva Voce Questions & Answers.

---

## 👨‍💻 Project Developer Information

* **Course**: Advanced Java / Java Enterprise Programming (RTU Syllabus)
* **Project Name**: ShopSphere — Distributed Java Enterprise E-Commerce System
* **Academic Year**: 2026-2027
* **Status**: Complete & Production-Ready
