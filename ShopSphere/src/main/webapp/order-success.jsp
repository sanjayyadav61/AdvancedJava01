<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Order Confirmed! | ShopSphere" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card border-0 shadow-lg rounded-4 text-center p-5">
                    
                    <div class="mb-4">
                        <div class="d-inline-flex align-items-center justify-content-center rounded-circle bg-success text-white shadow-sm" style="width: 80px; height: 80px;">
                            <i class="bi bi-check-lg display-4"></i>
                        </div>
                    </div>

                    <h2 class="fw-bold mb-2">Thank You For Your Order!</h2>
                    <p class="text-muted mb-4">
                        Your order has been received and is being processed by our automated inventory management system.
                    </p>

                    <c:if test="${not empty param.orderId}">
                        <div class="p-3 bg-light rounded-3 border mb-4 d-inline-block">
                            <span class="text-muted small text-uppercase">Tracking Reference Order ID:</span>
                            <div class="fs-4 fw-bold text-primary">#${param.orderId}</div>
                        </div>
                    </c:if>

                    <div class="row g-3 justify-content-center mb-4 text-start">
                        <div class="col-sm-6">
                            <div class="border rounded-3 p-3 h-100">
                                <small class="text-muted d-block mb-1"><i class="bi bi-truck text-primary me-1"></i> Delivery Estimate</small>
                                <span class="fw-semibold small">Within 3-5 Business Days</span>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="border rounded-3 p-3 h-100">
                                <small class="text-muted d-block mb-1"><i class="bi bi-bell text-success me-1"></i> Order Notifications</small>
                                <span class="fw-semibold small">Updates sent via Registered Email</span>
                            </div>
                        </div>
                    </div>

                    <div class="d-flex flex-wrap gap-2 justify-content-center">
                        <c:if test="${not empty param.orderId}">
                            <a href="${pageContext.request.contextPath}/order-details?id=${param.orderId}" class="btn btn-outline-primary rounded-pill px-4 fw-semibold">
                                View Order Invoice & Tracking
                            </a>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4 fw-semibold">
                            Continue Shopping
                        </a>
                    </div>

                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
