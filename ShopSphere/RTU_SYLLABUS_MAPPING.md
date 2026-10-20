# Rajasthan Technical University (RTU) — Advanced Java Syllabus Mapping Matrix

This document provides a **direct, line-by-line mapping** of every single topic and learning objective defined in the **RTU Advanced Java / Java Programming Lab** curriculum to the concrete classes, methods, configurations, and presentation files in the **ShopSphere** enterprise codebase.

---

## 📑 Syllabus Topic Mapping Overview

```
RTU ADVANCED JAVA SYLLABUS UNIT OVERVIEW
├─ UNIT 1: Java Swing, Event Handling & MVC Architecture
├─ UNIT 2: Java Applets & Historical Architecture Evolution
├─ UNIT 3: JDBC, Database Connectivity & Transaction Management
├─ UNIT 4: Java Networking (java.net Sockets, URLs & URLConnection)
├─ UNIT 5: Java Remote Method Invocation (RMI) & Distributed Systems
├─ UNIT 6: J2EE N-Tier Enterprise Architecture & JNDI
├─ UNIT 7: Jakarta Servlets, Session Tracking, Filters & Listeners
├─ UNIT 8: Jakarta Server Pages (JSP), EL & Custom Tag Files
├─ UNIT 9: Jakarta Standard Tag Library (JSTL Core, Fn, XML & SQL)
└─ UNIT 10: Internationalization (i18n) & Java Object Serialization
```

---

## UNIT 1: Java Swing, Event Handling & MVC Architecture

### 1.1 Swing GUI Components Hierarchy
| Syllabus Concept | Implementation Details | File Location & Line Numbers |
| :--- | :--- | :--- |
| `JFrame`, `JDialog` | Primary desktop admin window, standalone modules & modals | [AdminDashboardFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/AdminDashboardFrame.java#L25-L55)<br>[ProductFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/ProductFrame.java)<br>[ProductDialog.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/ProductDialog.java#L16-L35) |
| `JPanel`, `JScrollPane` | Component containers, table viewports, scrollable forms | [AdminDashboardFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/AdminDashboardFrame.java#L145-L180) |
| `JButton`, `JLabel`, `JTextField`, `JPasswordField` | Authentication dialog controls with masked inputs | [LoginFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/LoginFrame.java#L40-L75) |
| `JTable`, `DefaultTableModel` | Tabular display of Products, Orders, Customers, Categories | [AdminDashboardFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/AdminDashboardFrame.java#L160-L230)<br>[InventoryFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/InventoryFrame.java) |
| `JTabbedPane` | Tabbed desktop navigation (Overview, Products, Orders, Users, Categories) | [AdminDashboardFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/AdminDashboardFrame.java#L65-L85) |
| `JMenuBar`, `JMenu`, `JMenuItem` | Top-level application menu bar with Pluggable Look & Feel selectors | [AdminDashboardFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/AdminDashboardFrame.java#L87-L124) |
| `JComboBox`, `JSpinner`, `JCheckBox`, `JRadioButton` | Dropdown selectors, numerical spinners, active radio groups & checkboxes | [ProductFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/ProductFrame.java#L45-L75)<br>[ProductDialog.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/ProductDialog.java#L45-L75) |
| `JOptionPane` | Modal alert dialogs (confirmations, errors, warnings) | [AdminDashboardFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/AdminDashboardFrame.java#L275-L290) |

### 1.2 Layout Managers
| Layout Manager | Purpose / Usage | Code Reference |
| :--- | :--- | :--- |
| `BorderLayout` | Main window structure (`NORTH`: Header/Menu, `CENTER`: Tabs, `SOUTH`: Status bar) | [AdminDashboardFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/AdminDashboardFrame.java#L60) |
| `FlowLayout` | Button bars, search panels, alignment rows | [AdminDashboardFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/AdminDashboardFrame.java#L130) |
| `GridLayout` | 2-column key-value form fields in login dialog | [LoginFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/LoginFrame.java#L52) |
| `GridBagLayout` | Complex aligned product management modal dialog with weighted cells | [ProductDialog.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/ProductDialog.java#L40-L80) |

### 1.3 Event Handling Model & Swing MVC Pattern
| MVC Layer | Component / Responsibility | Implementation Reference |
| :--- | :--- | :--- |
| **Model** | Domain entities (`Product`, `User`, `Order`, `Category`) | `com.shopsphere.model.*` |
| **View** | Desktop windows: `AdminDashboardFrame`, `LoginFrame`, `ProductFrame`, `CategoryFrame`, `CustomerFrame`, `OrderFrame`, `InventoryFrame` | `com.shopsphere.swing.view.*` |
| **Controller** | Event handling and business logic: `LoginController`, `ProductController` | [LoginController.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/controller/LoginController.java)<br>[ProductController.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/controller/ProductController.java) |
| **DAO** | JDBC persistence abstraction: `ProductDAO`, `UserDAO`, `CategoryDAO`, `OrderDAO` | `com.shopsphere.dao.*` |

### 1.4 Pluggable Look and Feel (PLAF)
| Feature | Implementation Details | File Location |
| :--- | :--- | :--- |
| Runtime Look & Feel Switching | Allows instant runtime UI transformation between **Metal**, **Nimbus**, **Windows**, and **FlatLaf Modern Dark/Light** | [AdminDashboardFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/AdminDashboardFrame.java#L105-L124) |
| `SwingUtilities.updateComponentTreeUI()` | Recursively refreshes all open frames upon PLAF change | [AdminDashboardFrame.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/swing/view/AdminDashboardFrame.java#L118) |

---

## UNIT 2: Java Applets & Historical Architecture Evolution

| Syllabus Requirement | Academic Explanation & ShopSphere Demonstration | Code Reference |
| :--- | :--- | :--- |
| Applet Lifecycle Methods | `init()`, `start()`, `paint(Graphics g)`, `stop()`, `destroy()` documented and demonstrated | [AppletConceptDemo.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/demo/AppletConceptDemo.java#L15-L65) |
| Security Model & Sandbox | Historical explanation of browser NPAPI deprecation and migration to Jakarta EE Servlets | [AppletConceptDemo.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/demo/AppletConceptDemo.java#L45-L60) |
| Visual Applet Lifecycle Simulator | Web-based interactive state machine animating the 5 Applet lifecycle phases | [syllabus-demos.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/syllabus-demos.jsp#L260-L310) |

---

## UNIT 3: JDBC, Database Connectivity & Transaction Management

### 3.1 Core JDBC Architecture
| JDBC Component | Implementation in ShopSphere | File Location |
| :--- | :--- | :--- |
| Driver Loading | `Class.forName("com.mysql.cj.jdbc.Driver")` & `Class.forName("org.h2.Driver")` | [DBConnection.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/util/DBConnection.java#L71-L88) |
| `DriverManager.getConnection()` | Externalized connection pooling configuration via `db.properties` | [DBConnection.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/util/DBConnection.java#L72) |
| Fallback Database Engine | Seamless automatic failover to embedded in-memory H2 if MySQL is unavailable | [DBConnection.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/util/DBConnection.java#L75-L83) |
| Schema Bootstrapping | Dynamic DDL execution parsing `shopsphere.sql` statements | [DatabaseInitializer.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/util/DatabaseInitializer.java#L40-L95) |

### 3.2 SQL Statements & Security
| Syllabus Concept | Implementation Details | File Location |
| :--- | :--- | :--- |
| `PreparedStatement` | Complete parameterized queries eliminating SQL Injection vulnerabilities | [ProductDAO.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/dao/ProductDAO.java#L35-L80) |
| `ResultSet` Traversal | Iterating rows, retrieving typed attributes (`getInt`, `getString`, `getDouble`) | [ProductDAO.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/dao/ProductDAO.java#L50-L70) |
| Resource Management | Deterministic closure of `Connection`, `PreparedStatement`, `ResultSet` | [DBConnection.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/util/DBConnection.java#L98-L115) |

### 3.3 Atomic ACID Transactions
| Transaction Concept | Real-World Implementation | File Location & Line Numbers |
| :--- | :--- | :--- |
| `setAutoCommit(false)` | Starts atomic database transaction for order placement | [OrderDAO.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/dao/OrderDAO.java#L45) |
| Order Creation | Inserts master order record and retrieves auto-generated ID (`RETURN_GENERATED_KEYS`) | [OrderDAO.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/dao/OrderDAO.java#L55-L65) |
| Item Insertion | Iterates cart items, inserting records into `order_items` table | [OrderDAO.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/dao/OrderDAO.java#L70-L85) |
| Inventory Decrement | Verifies stock and executes `UPDATE products SET stock = stock - ? WHERE id = ?` | [OrderDAO.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/dao/OrderDAO.java#L90-L105) |
| `conn.commit()` | Finalizes atomic state change only when all steps succeed | [OrderDAO.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/dao/OrderDAO.java#L115) |
| `conn.rollback()` | Reverts all intermediate changes if any step or stock verification fails | [OrderDAO.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/dao/OrderDAO.java#L120-L130) |

---

## UNIT 4: Java Networking (java.net Sockets, URLs & URLConnection)

### 4.1 Multi-Threaded TCP Socket Client-Server
| Networking Concept | Implementation in ShopSphere | File Location |
| :--- | :--- | :--- |
| `ServerSocket` | Binds to port `8088`, listening for client connections in an infinite loop | [InventoryServer.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/network/server/InventoryServer.java#L35-L60) |
| Multi-Threading Worker | Spawns `ClientHandler implements Runnable` thread per connection | [InventoryServer.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/network/server/InventoryServer.java#L65-L130) |
| Object Streams | Exchanges serializable `NetworkMessage` instances via `ObjectOutputStream` and `ObjectInputStream` | [NetworkMessage.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/network/protocol/NetworkMessage.java)<br>[InventoryClient.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/network/client/InventoryClient.java#L80-L120) |
| Socket Client GUI | Desktop client displaying real-time inventory feed and executing stock adjustment commands | [InventoryClient.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/network/client/InventoryClient.java#L30-L75) |

### 4.2 URL & URLConnection Demonstration
| Networking Concept | Implementation Details | File Location |
| :--- | :--- | :--- |
| `java.net.URL` | Parsing protocol, host, port, path, query parameters | [UrlConnectionDemo.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/demo/UrlConnectionDemo.java#L20-L35) |
| `URLConnection` | Opening connection, reading HTTP headers (`Content-Type`, `Server`, `Content-Length`) | [UrlConnectionDemo.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/demo/UrlConnectionDemo.java#L38-L55) |
| Stream Reading | Reading response stream via `BufferedReader(new InputStreamReader(conn.getInputStream()))` | [UrlConnectionDemo.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/demo/UrlConnectionDemo.java#L58-L75) |

---

## UNIT 5: Java Remote Method Invocation (RMI)

| RMI Concept | Role in Architecture | File Location & Line Numbers |
| :--- | :--- | :--- |
| Remote Interface | Defines remote callable methods extending `java.rmi.Remote`: `getAllProducts()`, `getProductStock()`, `updateStock()` | [InventoryService.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/rmi/remote/InventoryService.java#L12-L28) |
| Remote Implementation | Extends `UnicastRemoteObject implements InventoryService` with JDBC DAO delegation | [InventoryServiceImpl.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/rmi/server/InventoryServiceImpl.java#L15-L65) |
| RMI Registry Binder | Initializes in-process registry `LocateRegistry.createRegistry(1099)` and binds service via `Naming.rebind("InventoryService", ...)` | [RmiServer.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/rmi/server/RmiServer.java#L20-L45) |
| RMI Client Lookup | Discovers remote stub using `(InventoryService) Naming.lookup("rmi://localhost:1099/InventoryService")` | [RmiClient.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/rmi/client/RmiClient.java#L85-L105) |
| Transparent Invocations | Executes distributed methods over the network with local Java method syntax | [RmiClient.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/rmi/client/RmiClient.java#L120-L150) |

---

## UNIT 6: J2EE N-Tier Enterprise Architecture & JNDI

| Enterprise Concept | Explanation & ShopSphere Demonstration | Code Reference |
| :--- | :--- | :--- |
| Presentation Tier | Jakarta Server Pages (JSP), JSTL, Tag Files, CSS3, JavaScript | `src/main/webapp/` |
| Business Logic Tier | Jakarta Servlets (`HttpServlet`), Service Classes, Security Filters | `com.shopsphere.controller.*`, `com.shopsphere.service.*` |
| Data Access Tier | Data Access Objects (DAO pattern), JDBC 4.2 | `com.shopsphere.dao.*` |
| Database Tier | Relational Database (MySQL 8.0 / Embedded H2) | `database/shopsphere.sql` |
| JNDI Naming Context | Demonstration of `InitialContext`, `Context.lookup("java:comp/env")` and RMI registry context factory | [JndiDemo.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/demo/JndiDemo.java#L15-L55) |

---

## UNIT 7: Jakarta Servlets, Session Tracking, Filters & Listeners

### 7.1 Servlet Lifecycle & Request Dispatching
| Syllabus Feature | Implementation Details | File Location |
| :--- | :--- | :--- |
| `init(ServletConfig)` | Resource loading and DAO initialization | [HomeServlet.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/controller/HomeServlet.java#L25) |
| `doGet()` | Handling HTTP GET for catalogs, details, profile, search | [ProductServlet.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/controller/ProductServlet.java#L25-L65) |
| `doPost()` | Handling HTTP POST for checkout, login, cart mutations, coupon validation | [CheckoutServlet.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/controller/CheckoutServlet.java#L35-L95) |
| `RequestDispatcher.forward()` | Passing model attributes to JSP views | [HomeServlet.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/controller/HomeServlet.java#L40) |
| `response.sendRedirect()` | Post-Redirect-Get (PRG) pattern after mutating requests | [CartServlet.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/controller/CartServlet.java#L80) |

### 7.2 Session Tracking Mechanisms
| Session Tracking Technique | Real-World Application in ShopSphere | File Location |
| :--- | :--- | :--- |
| `HttpSession` API | `session.setAttribute("user", u)`, `session.getAttribute("user")`, `session.invalidate()` | [LoginServlet.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/controller/LoginServlet.java#L45)<br>[LogoutServlet.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/controller/LogoutServlet.java#L22) |
| HTTP Cookies | `Cookie` creation, `setMaxAge(604800)`, `response.addCookie()`, and retrieval for "Remember Me" login tokens | [LoginServlet.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/controller/LoginServlet.java#L52-L65) |
| URL Rewriting | `response.encodeURL("/shopsphere/products")` fallback when cookies are disabled | [navbar.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/fragments/navbar.jsp#L20-L40) |
| Hidden Form Fields | Passing CSRF tokens, order IDs, and redirect destinations in forms | [checkout.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/checkout.jsp#L45-L60) |

### 7.3 Servlet Filters
| Filter Class | URL Pattern Mapped | Responsibility |
| :--- | :--- | :--- |
| `AuthenticationFilter` | `/cart`, `/checkout`, `/orders`, `/wishlist`, `/profile` | Verifies customer session; redirects unauthenticated users to `/login.jsp` with flash warning |
| `AdminFilter` | `/admin/*` | Verifies `user.role == 'ADMIN'`; returns **HTTP 403 Forbidden** if non-admin attempts access |
| `CharacterEncodingFilter` | `/*` | Enforces `request.setCharacterEncoding("UTF-8")` and `response.setCharacterEncoding("UTF-8")` |

### 7.4 Servlet Event Listeners
| Listener Class | Interface Implemented | Life Event Monitored |
| :--- | :--- | :--- |
| `AppListener` | `ServletContextListener` | Application startup/shutdown; triggers database schema check |
| `SessionListener` | `HttpSessionListener` | Tracks concurrent active sessions via atomic counter (`AtomicInteger`) |

---

## UNIT 8: Jakarta Server Pages (JSP), EL & Custom Tag Files

### 8.1 JSP Syntax Elements
| JSP Syntax Element | Demonstration Example | File Location |
| :--- | :--- | :--- |
| Page Directive (`<%@ page %>`) | `<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>` | [index.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/index.jsp#L1) |
| Taglib Directive (`<%@ taglib %>`) | `<%@ taglib prefix="c" uri="jakarta.tags.core" %>` | [products.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/products.jsp#L2) |
| Include Directive (`<%@ include %>`) | Static inclusion of header, navbar, and footer | [index.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/index.jsp#L5-L10) |
| Declaration (`<%! ... %>`) | `<%! private int hitCounter = 1042; %>` | [syllabus-demos.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/syllabus-demos.jsp#L45) |
| Scriptlet (`<% ... %>`) | `<% hitCounter++; String serverTime = new java.util.Date().toString(); %>` | [syllabus-demos.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/syllabus-demos.jsp#L52) |
| Expression (`<%= ... %>`) | `<%= getAcademicHeading() %>` | [syllabus-demos.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/syllabus-demos.jsp#L60) |
| JSP Comment (`<%-- ... --%>`) | Server-side comment stripped before HTML transmission | [syllabus-demos.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/syllabus-demos.jsp#L65) |

### 8.2 Expression Language (EL) & Custom Tag Files
| EL / Tag Feature | Real-World Usage in ShopSphere | File Location |
| :--- | :--- | :--- |
| Scoped Variable Access | `${sessionScope.user.name}`, `${requestScope.products}` | [navbar.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/fragments/navbar.jsp#L60) |
| Dot & Bracket Operators | `${product.category.name}`, `${cart.items[0]}` | [products.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/products.jsp#L55) |
| Ternary Operator | `${empty sessionScope.user ? 'Guest' : sessionScope.user.name}` | [header.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/fragments/header.jsp#L15) |
| Custom Tag File: `<tags:productCard>` | Encapsulates card markup, discount badge, star rating, and add-to-cart action | [productCard.tag](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/WEB-INF/tags/productCard.tag#L1-L40) |
| Custom Tag File: `<tags:rating>` | Renders dynamic FontAwesome stars (`fa-star`, `fa-star-half-alt`) based on numerical rating | [rating.tag](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/WEB-INF/tags/rating.tag#L1-L25) |

---

## UNIT 9: Jakarta Standard Tag Library (JSTL)

| JSTL Tag Library | Tag Examples Used | Live Demonstration |
| :--- | :--- | :--- |
| **JSTL Core (`c:`)** | `<c:forEach items="${products}" var="p">`<br>`<c:if test="${not empty error}">`<br>`<c:choose><c:when>...<c:otherwise>`<br>`<c:url value="/products">` | [products.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/products.jsp#L40-L90)<br>[syllabus-demos.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/syllabus-demos.jsp#L90-L135) |
| **JSTL Functions (`fn:`)** | `fn:length(cart.items)`, `fn:toUpperCase(category.name)`, `fn:substring(product.description, 0, 80)` | [cart.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/cart.jsp#L25)<br>[syllabus-demos.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/syllabus-demos.jsp#L140-L165) |
| **JSTL XML (`x:`)** | `<x:parse xml="${xmlCatalog}" var="doc" />`<br>`<x:forEach select="$doc/catalog/product">`<br>`<x:out select="name" />` | [syllabus-demos.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/syllabus-demos.jsp#L170-L210)<br>[products_catalog.xml](file:///c:/Users/anike/Downloads/JavaProject/src/main/resources/products_catalog.xml) |

---

## UNIT 10: Internationalization (i18n) & Java Object Serialization

### 10.1 Internationalization (i18n)
| i18n Feature | Implementation Details | File Location |
| :--- | :--- | :--- |
| Resource Bundles | `messages_en.properties` (English) and `messages_hi.properties` (Hindi UTF-8 encoded) | `src/main/resources/messages_*.properties` |
| `java.util.ResourceBundle` | Dynamically loads bundle based on `java.util.Locale` | [I18nDemo.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/demo/I18nDemo.java#L20-L40) |
| Web UI Locale Switcher | One-click toggling between English & Hindi in `/shopsphere/demos?lang=hi` | [syllabus-demos.jsp](file:///c:/Users/anike/Downloads/JavaProject/src/main/webapp/syllabus-demos.jsp#L215-L255) |
| Currency & Date Formatting | `NumberFormat.getCurrencyInstance(locale)` & `DateFormat.getDateTimeInstance(...)` | [CurrencyFormatter.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/util/CurrencyFormatter.java#L15-L45) |

### 10.2 Java Object Serialization
| Serialization Feature | Implementation Details | File Location |
| :--- | :--- | :--- |
| `java.io.Serializable` | Implemented across all 11 model entities (`Product`, `User`, `Order`, `CartItem`, etc.) | `com.shopsphere.model.*` |
| `serialVersionUID` | Defined explicitly on domain models to ensure byte stream version compatibility | [Product.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/model/Product.java#L12) |
| `ObjectOutputStream` / `ObjectInputStream` | Serializes Java objects to raw binary byte arrays and restores state with integrity validation | [SerializationDemo.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/demo/SerializationDemo.java#L20-L55) |
| Distributed Network Payloads | Sends serializable `NetworkMessage` across TCP sockets to remote inventory clients | [InventoryServer.java](file:///c:/Users/anike/Downloads/JavaProject/src/main/java/com/shopsphere/network/server/InventoryServer.java#L80-L115) |

---

## 🏁 Summary Checklist for University Lab Evaluators

* [x] **Complete Working Web Application** running on Jakarta EE 10 / Tomcat 10.1 (`/shopsphere`)
* [x] **Desktop Swing MVC Application** with FlatLaf & dynamic Look & Feel (`SwingAdminLauncher`)
* [x] **Distributed Multi-threaded TCP Sockets** (`InventoryServer` on port 8088 + `InventoryClient`)
* [x] **Java Remote Method Invocation (RMI)** (`RmiServer` on port 1099 + `RmiClient`)
* [x] **Pure JDBC Data Access Layer** with ACID transactions, rollback, and connection management
* [x] **Zero-configuration dual database**: MySQL 8.0 + automatic embedded H2 fallback
* [x] **Educational Syllabus Showcase Portal** (`/shopsphere/demos`)
* [x] **100% RTU Advanced Java Syllabus Coverage**
