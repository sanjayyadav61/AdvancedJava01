<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:if test="${empty featuredProducts}">
    <%
        com.shopsphere.service.ProductService ps = new com.shopsphere.service.ProductService();
        request.setAttribute("categories", ps.getAllCategories());
        request.setAttribute("featuredProducts", ps.getFeaturedProducts(8));
        request.setAttribute("allProducts", ps.getAllActiveProducts());
    %>
</c:if>

<c:set var="pageTitle" value="ShopSphere | Smart Shopping, Distributed Java Architecture" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <!-- Hero Banner -->
        <section class="hero-banner p-4 p-md-5 mb-5 shadow">
            <div class="row align-items-center position-relative z-1">
                <div class="col-lg-7">
                    <span class="badge bg-primary px-3 py-2 rounded-pill mb-3 fw-semibold text-uppercase tracking-wider">
                        <i class="bi bi-stars me-1"></i> Premium Deals & Fast Delivery
                    </span>
                    <h1 class="display-5 fw-extrabold text-white mb-3">
                        Discover Quality Products & Exclusive Online Deals
                    </h1>
                    <p class="lead text-light opacity-75 mb-4">
                        Shop the latest electronics, fashion, home essentials, books, and fitness gear with verified customer ratings and secure checkout.
                    </p>
                    <div class="d-flex flex-wrap gap-3">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg rounded-pill px-4 fw-semibold shadow">
                            <i class="bi bi-cart3 me-2"></i> Explore Catalog
                        </a>
                        <a href="#featuredCategories" class="btn btn-outline-light btn-lg rounded-pill px-4 fw-semibold">
                            <i class="bi bi-grid me-2"></i> Browse Categories
                        </a>
                    </div>
                </div>
                <div class="col-lg-5 d-none d-lg-block text-center">
                    <img src="https://images.unsplash.com/photo-1517336714731-489689fd1ca8?auto=format&fit=crop&w=700&q=80" 
                         alt="Electronics Showcase" class="img-fluid rounded-4 shadow-lg border border-secondary border-opacity-25" style="max-height: 280px; object-fit: cover;">
                </div>
            </div>
        </section>

        <!-- Feature Badges -->
        <section class="row g-3 mb-5">
            <div class="col-md-3 col-6">
                <div class="d-flex align-items-center gap-3 p-3 bg-white rounded-4 border shadow-sm">
                    <div class="category-icon-box text-primary"><i class="bi bi-truck"></i></div>
                    <div>
                        <h6 class="mb-0 fw-bold">Free Delivery</h6>
                        <small class="text-muted">Orders over ₹500</small>
                    </div>
                </div>
            </div>
            <div class="col-md-3 col-6">
                <div class="d-flex align-items-center gap-3 p-3 bg-white rounded-4 border shadow-sm">
                    <div class="category-icon-box text-success"><i class="bi bi-shield-check"></i></div>
                    <div>
                        <h6 class="mb-0 fw-bold">Secure Checkout</h6>
                        <small class="text-muted">Mock UPI, Card & COD</small>
                    </div>
                </div>
            </div>
            <div class="col-md-3 col-6">
                <div class="d-flex align-items-center gap-3 p-3 bg-white rounded-4 border shadow-sm">
                    <div class="category-icon-box text-warning"><i class="bi bi-arrow-repeat"></i></div>
                    <div>
                        <h6 class="mb-0 fw-bold">Instant Restock</h6>
                        <small class="text-muted">RMI & Socket Sync</small>
                    </div>
                </div>
            </div>
            <div class="col-md-3 col-6">
                <div class="d-flex align-items-center gap-3 p-3 bg-white rounded-4 border shadow-sm">
                    <div class="category-icon-box text-danger"><i class="bi bi-percent"></i></div>
                    <div>
                        <h6 class="mb-0 fw-bold">Coupons Available</h6>
                        <small class="text-muted">Code: WELCOME50</small>
                    </div>
                </div>
            </div>
        </section>

        <!-- Featured Categories -->
        <section class="mb-5">
            <div class="d-flex justify-content-between align-items-end mb-4">
                <div>
                    <span class="text-primary text-uppercase small fw-bold tracking-wider">Browse by Department</span>
                    <h3 class="fw-bold mb-0">Featured Categories</h3>
                </div>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary rounded-pill btn-sm px-3">
                    View All <i class="bi bi-arrow-right ms-1"></i>
                </a>
            </div>

            <div class="row g-3">
                <c:forEach var="cat" items="${categories}">
                    <div class="col-lg-2 col-md-4 col-6">
                        <a href="${pageContext.request.contextPath}/products?category=${cat.categoryId}" class="text-decoration-none">
                            <div class="category-card p-3 text-center h-100 d-flex flex-column align-items-center justify-content-center">
                                <div class="category-icon-box mb-2">
                                    <i class="${cat.iconClass}"></i>
                                </div>
                                <h6 class="fw-bold text-dark mb-1 small">${cat.name}</h6>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </section>

        <%-- Trending Products Section --%>
        <section class="mb-5">
            <div class="d-flex justify-content-between align-items-end mb-4">
                <div>
                    <span class="text-danger text-uppercase small fw-bold tracking-wider">Top Deals</span>
                    <h3 class="fw-bold mb-0">Trending Products & Offers</h3>
                </div>
                <a href="${pageContext.request.contextPath}/products?sortBy=discount" class="btn btn-outline-secondary rounded-pill btn-sm px-3">
                    See More Deals <i class="bi bi-arrow-right ms-1"></i>
                </a>
            </div>

            <div class="row g-4">
                <c:forEach var="p" items="${featuredProducts}">
                    <div class="col-xl-3 col-lg-4 col-md-6">
                        <tags:productCard product="${p}" />
                    </div>
                </c:forEach>
            </div>
        </section>

        <!-- Callout Banner for Admin & Syllabus -->
        <section class="p-4 rounded-4 bg-primary text-white shadow-sm mb-4">
            <div class="row align-items-center g-3">
                <div class="col-lg-8">
                    <h4 class="fw-bold mb-1">Evaluating RTU Syllabus Topics?</h4>
                    <p class="mb-0 text-white-50">
                        Check out our interactive Syllabus Demos page covering JSTL XML, SQL, Expressions, i18n, JNDI, Applets and java.net Sockets.
                    </p>
                </div>
                <div class="col-lg-4 text-lg-end">
                    <a href="${pageContext.request.contextPath}/demos" class="btn btn-light rounded-pill px-4 fw-semibold text-primary">
                        Launch Live Demos <i class="bi bi-arrow-right ms-1"></i>
                    </a>
                </div>
            </div>
        </section>

    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
