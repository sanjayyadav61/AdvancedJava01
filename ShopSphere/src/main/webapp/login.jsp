<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Sign In | ShopSphere E-Commerce" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="card border-0 shadow-lg rounded-4 p-4 p-md-5">
                    
                    <div class="text-center mb-4">
                        <div class="navbar-brand-badge mb-2">
                            <i class="bi bi-person-fill fs-5"></i>
                        </div>
                        <h3 class="fw-bold">Welcome Back</h3>
                        <p class="text-muted small">Sign in to manage orders, cart & wishlist</p>
                    </div>

                    <!-- Alerts -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger py-2 small" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-1"></i>${errorMessage}
                        </div>
                    </c:if>
                    <c:if test="${not empty sessionScope.successMessage}">
                        <div class="alert alert-success py-2 small" role="alert">
                            <i class="bi bi-check-circle-fill me-1"></i>${sessionScope.successMessage}
                        </div>
                        <c:remove var="successMessage" scope="session"/>
                    </c:if>
                    <c:if test="${param.loggedOut eq 'true'}">
                        <div class="alert alert-info py-2 small" role="alert">
                            <i class="bi bi-info-circle-fill me-1"></i>You have been logged out successfully.
                        </div>
                    </c:if>

                    <!-- Login Form -->
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="mb-3">
                            <label for="loginEmail" class="form-label small fw-semibold">Email Address</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light border-end-0"><i class="bi bi-envelope text-muted"></i></span>
                                <input type="email" class="form-control border-start-0" id="loginEmail" name="email" 
                                       value="${not empty enteredEmail ? enteredEmail : (not empty savedEmail ? savedEmail : '')}" 
                                       placeholder="name@example.com" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <div class="d-flex justify-content-between align-items-center">
                                <label for="loginPassword" class="form-label small fw-semibold mb-0">Password</label>
                            </div>
                            <div class="input-group mt-1">
                                <span class="input-group-text bg-light border-end-0"><i class="bi bi-lock text-muted"></i></span>
                                <input type="password" class="form-control border-start-0" id="loginPassword" name="password" 
                                       placeholder="••••••••" required>
                            </div>
                        </div>

                        <div class="form-check mb-4">
                            <input class="form-check-input" type="checkbox" id="rememberMe" name="rememberMe" 
                                   ${not empty savedEmail ? 'checked' : ''}>
                            <label class="form-check-label small text-muted" for="rememberMe">
                                Remember email on this browser (Cookie)
                            </label>
                        </div>

                        <button type="submit" class="btn btn-primary rounded-pill w-100 py-2 fw-semibold shadow-sm mb-3">
                            Sign In to Account
                        </button>
                    </form>

                    <!-- Quick Demo Credentials for Academic Evaluation -->
                    <div class="p-3 bg-light rounded-3 border mb-3">
                        <div class="small fw-bold text-muted mb-2 text-uppercase">Academic Viva Demo Credentials:</div>
                        <div class="d-grid gap-2">
                            <button type="button" class="btn btn-outline-secondary btn-sm text-start" onclick="fillDemoUser('customer')">
                                <i class="bi bi-person me-1 text-success"></i> Customer: <code>customer@shopsphere.com</code> / <code>password123</code>
                            </button>
                            <button type="button" class="btn btn-outline-secondary btn-sm text-start" onclick="fillDemoUser('admin')">
                                <i class="bi bi-shield-lock me-1 text-danger"></i> Admin: <code>admin@shopsphere.com</code> / <code>admin123</code>
                            </button>
                        </div>
                    </div>

                    <div class="text-center small text-muted">
                        Don't have an account yet? 
                        <a href="${pageContext.request.contextPath}/register.jsp" class="text-primary fw-semibold text-decoration-none">Register here</a>
                    </div>

                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
