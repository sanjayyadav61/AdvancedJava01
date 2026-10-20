<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Order #${order.orderId} Details | ShopSphere" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <!-- Breadcrumb & Print -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb small mb-0">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none">Home</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/orders" class="text-decoration-none">Orders</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Order #${order.orderId}</li>
                </ol>
            </nav>
            <button onclick="window.print()" class="btn btn-outline-secondary rounded-pill btn-sm d-print-none">
                <i class="bi bi-printer me-1"></i> Print Invoice
            </button>
        </div>

        <!-- Tracking Visual Timeline -->
        <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
            <h5 class="fw-bold mb-4">Delivery Tracking Progress</h5>
            <div class="row g-2 text-center">
                <div class="col-3">
                    <div class="d-inline-flex p-3 rounded-circle ${order.orderStatus != 'CANCELLED' ? 'bg-success text-white' : 'bg-secondary text-white'} mb-2">
                        <i class="bi bi-cart-check fs-5"></i>
                    </div>
                    <div class="fw-semibold small">Order Placed</div>
                    <small class="text-muted d-none d-md-block">${order.createdAt}</small>
                </div>
                <div class="col-3">
                    <div class="d-inline-flex p-3 rounded-circle 
                        ${order.orderStatus == 'CONFIRMED' || order.orderStatus == 'SHIPPED' || order.orderStatus == 'DELIVERED' ? 'bg-success text-white' : 'bg-light text-muted border'} mb-2">
                        <i class="bi bi-gear-wide-connected fs-5"></i>
                    </div>
                    <div class="fw-semibold small">Confirmed</div>
                    <small class="text-muted d-none d-md-block">Processed by Store</small>
                </div>
                <div class="col-3">
                    <div class="d-inline-flex p-3 rounded-circle 
                        ${order.orderStatus == 'SHIPPED' || order.orderStatus == 'DELIVERED' ? 'bg-success text-white' : 'bg-light text-muted border'} mb-2">
                        <i class="bi bi-truck fs-5"></i>
                    </div>
                    <div class="fw-semibold small">Dispatched</div>
                    <small class="text-muted d-none d-md-block">In Transit</small>
                </div>
                <div class="col-3">
                    <div class="d-inline-flex p-3 rounded-circle 
                        ${order.orderStatus == 'DELIVERED' ? 'bg-success text-white' : 'bg-light text-muted border'} mb-2">
                        <i class="bi bi-house-check fs-5"></i>
                    </div>
                    <div class="fw-semibold small">Delivered</div>
                    <small class="text-muted d-none d-md-block">Package Received</small>
                </div>
            </div>
        </div>

        <div class="row g-4">
            <!-- Line Items Table -->
            <div class="col-lg-8">
                <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-4">
                    <div class="card-header bg-light py-3 px-4 border-bottom">
                        <h6 class="fw-bold mb-0">Ordered Items (${order.items.size()})</h6>
                    </div>
                    <div class="table-responsive">
                        <table class="table align-middle mb-0">
                            <thead class="table-light small text-uppercase text-muted">
                                <tr>
                                    <th class="ps-4">Item</th>
                                    <th>Price</th>
                                    <th>Quantity</th>
                                    <th class="text-end pe-4">Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${order.items}">
                                    <tr>
                                        <td class="ps-4 py-3">
                                            <div class="d-flex align-items-center gap-3">
                                                <img src="${item.productImageUrl}" alt="${item.productName}" class="rounded border" style="width: 50px; height: 50px; object-fit: cover;">
                                                <div>
                                                    <span class="small text-muted text-uppercase">${item.productBrand}</span>
                                                    <h6 class="mb-0 fw-semibold small">${item.productName}</h6>
                                                </div>
                                            </div>
                                        </td>
                                        <td>₹<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></td>
                                        <td>${item.quantity}</td>
                                        <td class="text-end pe-4 fw-bold text-primary">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Right Column: Shipping & Payment Info -->
            <div class="col-lg-4">
                
                <!-- Shipping Address -->
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h6 class="fw-bold mb-3"><i class="bi bi-geo-alt text-primary me-1"></i> Delivery Address</h6>
                    <c:choose>
                        <c:when test="${not empty order.address}">
                            <div class="small fw-semibold text-dark">${order.userName}</div>
                            <div class="small text-muted mt-1">${order.address.formattedAddress}</div>
                            <div class="badge bg-light text-muted border mt-2">${order.address.addressType} Address</div>
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted small">Standard registered delivery location</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Payment Details -->
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h6 class="fw-bold mb-3"><i class="bi bi-credit-card text-success me-1"></i> Payment Information</h6>
                    <div class="d-flex justify-content-between mb-1 small">
                        <span class="text-muted">Method:</span>
                        <span class="fw-semibold">${order.paymentMethod}</span>
                    </div>
                    <div class="d-flex justify-content-between mb-1 small">
                        <span class="text-muted">Status:</span>
                        <span class="badge bg-success-subtle text-success">${order.paymentStatus}</span>
                    </div>
                </div>

                <!-- Invoice Breakdown -->
                <div class="card border-0 shadow-sm rounded-4 p-4">
                    <h6 class="fw-bold mb-3 border-bottom pb-2">Invoice Summary</h6>
                    <div class="d-flex justify-content-between mb-2 small">
                        <span class="text-muted">Order Total (Gross)</span>
                        <span class="fw-semibold">₹<fmt:formatNumber value="${order.totalAmount + order.discount}" pattern="#,##0.00"/></span>
                    </div>
                    <c:if test="${order.discount > 0}">
                        <div class="d-flex justify-content-between mb-2 small text-success">
                            <span>Coupon Discount</span>
                            <span class="fw-semibold">-₹<fmt:formatNumber value="${order.discount}" pattern="#,##0.00"/></span>
                        </div>
                    </c:if>
                    <div class="d-flex justify-content-between mb-2 small">
                        <span class="text-muted">Shipping Fee</span>
                        <span class="text-success fw-semibold">FREE</span>
                    </div>
                    <hr class="my-2">
                    <div class="d-flex justify-content-between align-items-baseline fs-5 fw-bold text-dark pt-1">
                        <span>Net Paid</span>
                        <span class="text-primary">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
                    </div>
                </div>

            </div>
        </div>

    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
