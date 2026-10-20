<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<footer class="bg-dark text-light pt-5 pb-4 mt-auto">
    <div class="container">
        <div class="row g-4">
            <!-- Col 1: About Platform -->
            <div class="col-lg-4 col-md-6">
                <div class="d-flex align-items-center gap-2 mb-3">
                    <span class="navbar-brand-badge">
                        <i class="bi bi-bag-check-fill fs-5"></i>
                    </span>
                    <span class="fs-4 fw-bold text-white">Shop<span class="text-primary">Sphere</span></span>
                </div>
                <p class="text-secondary small mb-3">
                    Your premier online shopping destination for electronics, fashion, home lifestyle, and books with genuine quality and express doorstep delivery.
                </p>
                <div class="d-flex gap-2">
                    <span class="badge bg-secondary-subtle text-secondary small"><i class="bi bi-shield-check me-1"></i>100% Secure</span>
                    <span class="badge bg-secondary-subtle text-secondary small"><i class="bi bi-truck me-1"></i>Fast Delivery</span>
                </div>
            </div>

            <!-- Col 2: Quick Links -->
            <div class="col-lg-3 col-md-6">
                <h6 class="text-white text-uppercase fw-bold mb-3 small">Store Navigation</h6>
                <ul class="list-unstyled text-secondary small">
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/home" class="text-secondary text-decoration-none">Home</a></li>
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/products" class="text-secondary text-decoration-none">All Products</a></li>
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/cart" class="text-secondary text-decoration-none">Shopping Cart</a></li>
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/wishlist" class="text-secondary text-decoration-none">My Wishlist</a></li>
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/orders" class="text-secondary text-decoration-none">Order Tracking</a></li>
                </ul>
            </div>

            <!-- Col 3: Customer Service -->
            <div class="col-lg-3 col-md-6">
                <h6 class="text-white text-uppercase fw-bold mb-3 small">Customer Support</h6>
                <ul class="list-unstyled text-secondary small">
                    <li class="mb-2"><i class="bi bi-check2-circle text-primary me-2"></i>Free Shipping on Orders > ₹500</li>
                    <li class="mb-2"><i class="bi bi-arrow-repeat text-primary me-2"></i>7-Day Replacement Guarantee</li>
                    <li class="mb-2"><i class="bi bi-credit-card text-primary me-2"></i>UPI, Card & COD Accepted</li>
                    <li class="mb-2"><i class="bi bi-headset text-primary me-2"></i>24/7 Dedicated Customer Care</li>
                </ul>
            </div>

            <!-- Col 4: Store Management -->
            <div class="col-lg-2 col-md-6">
                <h6 class="text-white text-uppercase fw-bold mb-3 small">Administration</h6>
                <p class="text-secondary small mb-3">
                    Store manager access for product catalog, inventory, and order fulfillment.
                </p>
                <div>
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-light btn-sm rounded-pill w-100">
                        <i class="bi bi-shield-lock me-1"></i> Admin Portal
                    </a>
                </div>
            </div>
        </div>

        <hr class="border-secondary my-4">

        <div class="row align-items-center text-secondary small">
            <div class="col-md-6 text-center text-md-start">
                © 2026 ShopSphere. All rights reserved. Designed for Academic Demonstration.
            </div>
            <div class="col-md-6 text-center text-md-end mt-2 mt-md-0">
                <span>Built with Java 17+, Apache Tomcat 10/11 & Bootstrap 5</span>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap 5 JavaScript Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- ShopSphere Custom App Script -->
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
