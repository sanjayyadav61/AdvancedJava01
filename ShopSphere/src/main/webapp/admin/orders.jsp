<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Order Management | Admin" scope="request"/>
<%@ include file="../fragments/header.jsp" %>
<%@ include file="../fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-3 border-bottom">
            <div>
                <span class="badge bg-danger rounded-pill px-3 py-1 fw-semibold small text-uppercase">Admin Portal</span>
                <h3 class="fw-bold mb-0 mt-1">Customer Order Management</h3>
                <small class="text-muted">Track order fulfillments and update delivery lifecycle</small>
            </div>

            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-secondary rounded-pill btn-sm px-3">
                Back to Dashboard
            </a>
        </div>

        <c:if test="${not empty sessionScope.adminSuccess}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>${sessionScope.adminSuccess}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="adminSuccess" scope="session"/>
        </c:if>

        <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
            <div class="table-responsive">
                <table class="table align-middle table-hover mb-0">
                    <thead class="table-light small text-uppercase text-muted">
                        <tr>
                            <th class="ps-4">Order ID</th>
                            <th>Customer</th>
                            <th>Date</th>
                            <th>Items</th>
                            <th>Total Amount</th>
                            <th>Payment</th>
                            <th>Current Status</th>
                            <th class="text-end pe-4">Update Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td class="ps-4 fw-bold">
                                    <a href="${pageContext.request.contextPath}/order-details?id=${o.orderId}" class="text-decoration-none">
                                        #${o.orderId}
                                    </a>
                                </td>
                                <td>
                                    <div class="fw-semibold small">${o.userName}</div>
                                    <small class="text-muted">${o.userEmail}</small>
                                </td>
                                <td class="small text-muted">${o.createdAt}</td>
                                <td><span class="badge bg-light text-dark border">${o.items.size()} items</span></td>
                                <td class="fw-bold text-primary">₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                                <td>
                                    <span class="small fw-semibold">${o.paymentMethod}</span>
                                    <span class="badge bg-success-subtle text-success small d-block" style="font-size: 0.7rem;">${o.paymentStatus}</span>
                                </td>
                                <td>
                                    <span class="badge rounded-pill px-3 py-1 
                                        ${o.orderStatus == 'DELIVERED' ? 'badge-status-delivered' : 
                                          o.orderStatus == 'SHIPPED' ? 'badge-status-shipped' : 
                                          o.orderStatus == 'CONFIRMED' ? 'badge-status-confirmed' : 'badge-status-placed'}">
                                        ${o.orderStatus}
                                    </span>
                                </td>
                                <td class="text-end pe-4">
                                    <form action="${pageContext.request.contextPath}/admin/orders" method="post" class="d-inline-flex gap-1">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="orderId" value="${o.orderId}">
                                        <select name="status" class="form-select form-select-sm" style="width: 130px;">
                                            <option value="PLACED" ${o.orderStatus == 'PLACED' ? 'selected' : ''}>Placed</option>
                                            <option value="CONFIRMED" ${o.orderStatus == 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                                            <option value="SHIPPED" ${o.orderStatus == 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                            <option value="DELIVERED" ${o.orderStatus == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                            <option value="CANCELLED" ${o.orderStatus == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                        </select>
                                        <button type="submit" class="btn btn-outline-primary btn-sm rounded-pill px-2" title="Save status">
                                            Update
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</main>

<%@ include file="../fragments/footer.jsp" %>
