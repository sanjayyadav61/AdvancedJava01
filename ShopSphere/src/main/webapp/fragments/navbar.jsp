<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar navbar-expand-lg navbar-light bg-white sticky-top border-bottom py-2 shadow-sm">
    <div class="container">
        <!-- Logo -->
        <a class="navbar-brand d-flex align-items-center gap-2 fw-bold text-dark fs-4" href="${pageContext.request.contextPath}/home">
            <span class="navbar-brand-badge">
                <i class="bi bi-bag-check-fill fs-5"></i>
            </span>
            <span>Shop<span class="text-primary">Sphere</span></span>
        </a>

        <!-- Mobile Toggle -->
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarMain">
            <!-- Left Nav Links -->
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 align-items-lg-center">
                <li class="nav-item">
                    <a class="nav-link px-3" href="${pageContext.request.contextPath}/home">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link px-3" href="${pageContext.request.contextPath}/products">All Products</a>
                </li>
                
                <!-- Categories Dropdown -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle px-3" href="#" id="catDropdown" role="button" data-bs-toggle="dropdown">
                        Categories
                    </a>
                    <ul class="dropdown-menu shadow-sm border-0 mt-2">
                        <c:forEach var="cat" items="${applicationScope.globalCategories}">
                            <li>
                                <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/products?category=${cat.categoryId}">
                                    <i class="${cat.iconClass} me-2 text-primary"></i>${cat.name}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </li>

            </ul>

            <!-- Global Search Bar -->
            <form action="${pageContext.request.contextPath}/products" method="get" class="d-flex my-2 my-lg-0 me-lg-3 flex-grow-1" style="max-width: 380px;">
                <div class="input-group">
                    <input type="text" name="q" value="${param.q}" class="form-control search-bar-input" placeholder="Search laptops, shoes, books...">
                    <button class="btn btn-primary rounded-circle ms-1 p-2 line-height-1" type="submit" title="Search">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </form>

            <!-- Right Actions: Wishlist, Cart, User Profile -->
            <ul class="navbar-nav align-items-lg-center gap-2">
                <!-- Wishlist Icon -->
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/wishlist" class="btn btn-light rounded-circle position-relative p-2" title="My Wishlist">
                        <i class="bi bi-heart fs-6 text-danger"></i>
                    </a>
                </li>

                <!-- Cart Icon with Live Counter -->
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-light rounded-circle position-relative p-2" title="Shopping Cart">
                        <i class="bi bi-cart3 fs-6 text-primary"></i>
                        <c:if test="${not empty sessionScope.cartCount and sessionScope.cartCount > 0}">
                            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                ${sessionScope.cartCount}
                            </span>
                        </c:if>
                    </a>
                </li>

                <!-- User Session Dropdown -->
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item dropdown ms-2">
                            <a class="btn btn-outline-secondary dropdown-toggle rounded-pill px-3 py-1 d-inline-flex align-items-center gap-2" href="#" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle text-primary fs-5"></i>
                                <span class="fw-semibold small">${sessionScope.user.name}</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 mt-2">
                                <li>
                                    <div class="dropdown-header">
                                        <div class="fw-bold text-dark">${sessionScope.user.name}</div>
                                        <div class="text-muted small">${sessionScope.user.email}</div>
                                        <span class="badge ${sessionScope.user.admin ? 'bg-danger' : 'bg-success'} rounded-pill mt-1">
                                            ${sessionScope.user.role}
                                        </span>
                                    </div>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <c:if test="${sessionScope.user.admin}">
                                    <li>
                                        <a class="dropdown-item text-primary fw-semibold" href="${pageContext.request.contextPath}/admin/dashboard">
                                            <i class="bi bi-speedometer2 me-2"></i>Admin Dashboard
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                </c:if>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders"><i class="bi bi-box-seam me-2"></i>My Orders</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/wishlist"><i class="bi bi-heart me-2"></i>Wishlist</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile"><i class="bi bi-gear me-2"></i>Profile Settings</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i>Sign Out
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item ms-2 d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline-primary rounded-pill px-3 py-1 btn-sm fw-semibold">
                                Sign In
                            </a>
                            <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-primary rounded-pill px-3 py-1 btn-sm fw-semibold">
                                Register
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<!-- Global Flash Messages -->
<c:if test="${not empty sessionScope.authError}">
    <div class="alert alert-warning alert-dismissible fade show container mt-3 mb-0" role="alert">
        <i class="bi bi-shield-exclamation me-2"></i>${sessionScope.authError}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="authError" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.cartAlert}">
    <div class="alert alert-success alert-dismissible fade show container mt-3 mb-0" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i>${sessionScope.cartAlert}
        <a href="${pageContext.request.contextPath}/cart" class="alert-link ms-2">View Cart</a>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="cartAlert" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.cartError}">
    <div class="alert alert-danger alert-dismissible fade show container mt-3 mb-0" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>${sessionScope.cartError}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="cartError" scope="session"/>
</c:if>
