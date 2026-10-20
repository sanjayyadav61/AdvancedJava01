<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Register | ShopSphere E-Commerce" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-7 col-lg-6">
                <div class="card border-0 shadow-lg rounded-4 p-4 p-md-5">
                    
                    <div class="text-center mb-4">
                        <div class="navbar-brand-badge mb-2">
                            <i class="bi bi-person-plus-fill fs-5"></i>
                        </div>
                        <h3 class="fw-bold">Create an Account</h3>
                        <p class="text-muted small">Join ShopSphere to buy products and track deliveries</p>
                    </div>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger py-2 small" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-1"></i>${errorMessage}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/register" method="post">
                        <div class="mb-3">
                            <label for="regName" class="form-label small fw-semibold">Full Name</label>
                            <input type="text" class="form-control" id="regName" name="name" value="${name}" placeholder="e.g. Rahul Sharma" required>
                        </div>

                        <div class="mb-3">
                            <label for="regEmail" class="form-label small fw-semibold">Email Address</label>
                            <input type="email" class="form-control" id="regEmail" name="email" value="${email}" placeholder="rahul@example.com" required>
                        </div>

                        <div class="mb-3">
                            <label for="regMobile" class="form-label small fw-semibold">Mobile Number</label>
                            <input type="tel" class="form-control" id="regMobile" name="mobile" value="${mobile}" placeholder="9876543210">
                        </div>

                        <div class="row g-2 mb-4">
                            <div class="col-sm-6">
                                <label for="regPassword" class="form-label small fw-semibold">Password</label>
                                <input type="password" class="form-control" id="regPassword" name="password" placeholder="At least 6 chars" required minlength="6">
                            </div>
                            <div class="col-sm-6">
                                <label for="regConfirmPassword" class="form-label small fw-semibold">Confirm Password</label>
                                <input type="password" class="form-control" id="regConfirmPassword" name="confirmPassword" placeholder="Repeat password" required>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary rounded-pill w-100 py-2 fw-semibold shadow-sm mb-3">
                            Register Account
                        </button>
                    </form>

                    <div class="text-center small text-muted">
                        Already have an account? 
                        <a href="${pageContext.request.contextPath}/login.jsp" class="text-primary fw-semibold text-decoration-none">Sign in here</a>
                    </div>

                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
