<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Admin Dashboard | ShopSphere" scope="request"/>
<%@ include file="../fragments/header.jsp" %>
<%@ include file="../fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <!-- Admin Navigation Bar -->
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-3 border-bottom">
            <div>
                <span class="badge bg-danger rounded-pill px-3 py-1 fw-semibold small text-uppercase">Management Console</span>
                <h3 class="fw-bold mb-0 mt-1">Admin Dashboard Overview</h3>
                <small class="text-muted">Logged in as Administrator (${sessionScope.user.email})</small>
            </div>

            <!-- Admin Sub-navigation Links -->
            <div class="btn-group rounded-pill shadow-sm mt-2 mt-md-0" role="group">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-primary btn-sm active">Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline-primary btn-sm">Products</a>
                <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-outline-primary btn-sm">Categories</a>
                <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline-primary btn-sm">Orders</a>
                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline-primary btn-sm">Customers</a>
                <a href="${pageContext.request.contextPath}/admin/coupons" class="btn btn-outline-primary btn-sm">Coupons</a>
            </div>
        </div>

        <!-- Metric Stat Cards -->
        <div class="row g-4 mb-5">
            <div class="col-xl-3 col-sm-6">
                <div class="metric-stat-card revenue shadow-sm">
                    <span class="text-muted small fw-bold text-uppercase">Total Sales Revenue</span>
                    <h3 class="fw-bold text-dark mb-0 mt-2">₹<fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00"/></h3>
                    <small class="text-success fw-semibold"><i class="bi bi-graph-up-arrow me-1"></i> Paid Customer Orders</small>
                </div>
            </div>

            <div class="col-xl-3 col-sm-6">
                <div class="metric-stat-card orders shadow-sm">
                    <span class="text-muted small fw-bold text-uppercase">Total Orders Placed</span>
                    <h3 class="fw-bold text-dark mb-0 mt-2">${totalOrders}</h3>
                    <small class="text-primary fw-semibold"><i class="bi bi-bag-check me-1"></i> Lifetime Transactions</small>
                </div>
            </div>

            <div class="col-xl-3 col-sm-6">
                <div class="metric-stat-card products shadow-sm">
                    <span class="text-muted small fw-bold text-uppercase">Catalog Products</span>
                    <h3 class="fw-bold text-dark mb-0 mt-2">${totalProducts}</h3>
                    <small class="text-warning fw-semibold"><i class="bi bi-box me-1"></i> Across All Categories</small>
                </div>
            </div>

            <div class="col-xl-3 col-sm-6">
                <div class="metric-stat-card users shadow-sm">
                    <span class="text-muted small fw-bold text-uppercase">Registered Customers</span>
                    <h3 class="fw-bold text-dark mb-0 mt-2">${totalCustomers}</h3>
                    <small class="text-purple fw-semibold"><i class="bi bi-people me-1"></i> Active User Accounts</small>
                </div>
            </div>
        </div>

        <!-- Low Stock Alerts & Recent Orders -->
        <div class="row g-4 mb-4">
            
            <!-- Low Stock Alert (Stock <= 5) -->
            <div class="col-lg-5">
                <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="fw-bold mb-0 text-danger">
                            <i class="bi bi-exclamation-triangle-fill me-1"></i> Low Stock Alerts
                        </h5>
                        <span class="badge bg-danger rounded-pill">${lowStockProducts.size()} Items</span>
                    </div>

                    <c:choose>
                        <c:when test="${not empty lowStockProducts}">
                            <div class="table-responsive">
                                <table class="table table-sm align-middle mb-0">
                                    <thead class="small text-muted">
                                        <tr>
                                            <th>Product</th>
                                            <th>Stock</th>
                                            <th class="text-end">Quick Restock</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="lp" items="${lowStockProducts}">
                                            <tr>
                                                <td>
                                                    <div class="small fw-semibold text-truncate" style="max-width: 160px;">${lp.name}</div>
                                                    <small class="text-muted">${lp.brand}</small>
                                                </td>
                                                <td>
                                                    <span class="badge bg-danger-subtle text-danger fw-bold">${lp.stock} left</span>
                                                </td>
                                                <td class="text-end">
                                                    <form action="${pageContext.request.contextPath}/admin/products" method="post" class="d-inline">
                                                        <input type="hidden" name="action" value="updateStock">
                                                        <input type="hidden" name="productId" value="${lp.productId}">
                                                        <input type="hidden" name="stock" value="${lp.stock + 10}">
                                                        <button type="submit" class="btn btn-outline-success btn-sm rounded-pill py-0 px-2" style="font-size:0.75rem;">
                                                            +10 Units
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center p-4 text-muted small">
                                <i class="bi bi-check-circle text-success fs-3 d-block mb-1"></i>
                                All inventory levels are healthy!
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Recent Orders -->
            <div class="col-lg-7">
                <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="fw-bold mb-0">Recent Customer Orders</h5>
                        <a href="${pageContext.request.contextPath}/admin/orders" class="small text-decoration-none">View All</a>
                    </div>

                    <div class="table-responsive">
                        <table class="table table-sm align-middle mb-0">
                            <thead class="small text-muted">
                                <tr>
                                    <th>Order</th>
                                    <th>Customer</th>
                                    <th>Amount</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="ro" items="${recentOrders}">
                                    <tr>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/order-details?id=${ro.orderId}" class="fw-bold text-decoration-none">
                                                #${ro.orderId}
                                            </a>
                                        </td>
                                        <td class="small text-truncate" style="max-width: 140px;">${ro.userName}</td>
                                        <td class="small fw-semibold text-primary">₹<fmt:formatNumber value="${ro.totalAmount}" pattern="#,##0.00"/></td>
                                        <td>
                                            <span class="badge rounded-pill small px-2 
                                                ${ro.orderStatus == 'DELIVERED' ? 'badge-status-delivered' : 
                                                  ro.orderStatus == 'SHIPPED' ? 'badge-status-shipped' : 'badge-status-placed'}">
                                                ${ro.orderStatus}
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>

    </div>
</main>

<%@ include file="../fragments/footer.jsp" %>
