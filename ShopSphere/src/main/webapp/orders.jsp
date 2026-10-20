<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="My Orders | ShopSphere E-Commerce" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <h3 class="fw-bold mb-4">
            <i class="bi bi-box-seam text-primary me-2"></i>My Orders & Purchase History
        </h3>

        <c:choose>
            <c:when test="${not empty orders}">
                <div class="d-flex flex-column gap-3">
                    <c:forEach var="order" items="${orders}">
                        <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                            <!-- Card Header -->
                            <div class="card-header bg-light py-3 px-4 border-bottom d-flex flex-wrap justify-content-between align-items-center gap-2">
                                <div class="d-flex flex-wrap gap-4 align-items-center">
                                    <div>
                                        <small class="text-muted d-block text-uppercase" style="font-size:0.75rem;">Order Placed</small>
                                        <span class="fw-semibold small">${order.createdAt}</span>
                                    </div>
                                    <div>
                                        <small class="text-muted d-block text-uppercase" style="font-size:0.75rem;">Total Amount</small>
                                        <span class="fw-bold text-primary">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
                                    </div>
                                    <div>
                                        <small class="text-muted d-block text-uppercase" style="font-size:0.75rem;">Payment</small>
                                        <span class="small fw-semibold">${order.paymentMethod} (${order.paymentStatus})</span>
                                    </div>
                                </div>

                                <div class="d-flex align-items-center gap-2">
                                    <span class="text-muted small">Order #${order.orderId}</span>
                                    <span class="badge rounded-pill px-3 py-1 
                                        ${order.orderStatus == 'DELIVERED' ? 'badge-status-delivered' : 
                                          order.orderStatus == 'SHIPPED' ? 'badge-status-shipped' : 
                                          order.orderStatus == 'CONFIRMED' ? 'badge-status-confirmed' : 'badge-status-placed'}">
                                        ${order.orderStatus}
                                    </span>
                                </div>
                            </div>

                            <!-- Card Body -->
                            <div class="card-body p-4">
                                <div class="row align-items-center g-3">
                                    <div class="col-md-8">
                                        <div class="d-flex flex-column gap-2">
                                            <c:forEach var="item" items="${order.items}">
                                                <div class="d-flex align-items-center gap-3">
                                                    <img src="${item.productImageUrl}" alt="${item.productName}" class="rounded border" style="width: 55px; height: 55px; object-fit: cover;">
                                                    <div>
                                                        <h6 class="mb-0 fw-semibold small">
                                                            <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" class="text-dark text-decoration-none">
                                                                ${item.productName}
                                                            </a>
                                                        </h6>
                                                        <small class="text-muted">Quantity: ${item.quantity} | Unit Price: ₹<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></small>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>

                                    <div class="col-md-4 text-md-end">
                                        <a href="${pageContext.request.contextPath}/order-details?id=${order.orderId}" class="btn btn-outline-primary rounded-pill btn-sm px-4 fw-semibold">
                                            <i class="bi bi-receipt me-1"></i> View Order Details & Tracking
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>

            <c:otherwise>
                <div class="card border-0 shadow-sm rounded-4 text-center p-5">
                    <div class="display-1 text-muted mb-3"><i class="bi bi-box2"></i></div>
                    <h4 class="fw-bold">No Orders Placed Yet</h4>
                    <p class="text-muted mb-4">You have not completed any purchases yet.</p>
                    <div>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4 py-2">
                            Explore Products
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
