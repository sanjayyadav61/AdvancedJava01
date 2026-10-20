<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Coupon Management | Admin" scope="request"/>
<%@ include file="../fragments/header.jsp" %>
<%@ include file="../fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-3 border-bottom">
            <div>
                <span class="badge bg-danger rounded-pill px-3 py-1 fw-semibold small text-uppercase">Admin Portal</span>
                <h3 class="fw-bold mb-0 mt-1">Discount Coupon Codes</h3>
                <small class="text-muted">Create promotional vouchers and percentage discounts</small>
            </div>

            <div class="d-flex gap-2 mt-2 mt-md-0">
                <button type="button" class="btn btn-primary rounded-pill btn-sm px-4 fw-semibold shadow-sm" data-bs-toggle="modal" data-bs-target="#addCouponModal">
                    <i class="bi bi-plus-lg me-1"></i> Add Coupon
                </button>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-secondary rounded-pill btn-sm px-3">
                    Back to Dashboard
                </a>
            </div>
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
                            <th class="ps-4">Coupon Code</th>
                            <th>Discount Type</th>
                            <th>Value</th>
                            <th>Minimum Order</th>
                            <th>Max Discount</th>
                            <th>Expiry Date</th>
                            <th>Status</th>
                            <th class="text-end pe-4">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${coupons}">
                            <tr>
                                <td class="ps-4">
                                    <span class="badge bg-light text-primary border fs-6 font-monospace px-3 py-1">
                                        ${c.code}
                                    </span>
                                </td>
                                <td><span class="badge bg-secondary-subtle text-secondary">${c.discountType}</span></td>
                                <td class="fw-bold">
                                    <c:choose>
                                        <c:when test="${c.discountType == 'PERCENTAGE'}">${c.discountValue}% OFF</c:when>
                                        <c:otherwise>₹<fmt:formatNumber value="${c.discountValue}" pattern="#,##0.00"/> FLAT</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>₹<fmt:formatNumber value="${c.minimumOrder}" pattern="#,##0.00"/></td>
                                <td>₹<fmt:formatNumber value="${c.maximumDiscount}" pattern="#,##0.00"/></td>
                                <td class="small text-muted">${empty c.expiryDate ? 'Never Expires' : c.expiryDate}</td>
                                <td>
                                    <span class="badge ${c.status ? 'bg-success' : 'bg-secondary'} rounded-pill">
                                        ${c.status ? 'Active' : 'Disabled'}
                                    </span>
                                </td>
                                <td class="text-end pe-4">
                                    <form action="${pageContext.request.contextPath}/admin/coupons" method="post" class="d-inline" onsubmit="return confirm('Delete this coupon?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="couponId" value="${c.couponId}">
                                        <button type="submit" class="btn btn-outline-danger btn-sm rounded-circle p-1" title="Delete">
                                            <i class="bi bi-trash"></i>
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

<!-- Add Coupon Modal -->
<div class="modal fade" id="addCouponModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow rounded-4">
            <form action="${pageContext.request.contextPath}/admin/coupons" method="post">
                <input type="hidden" name="action" value="save">
                <input type="hidden" name="couponId" value="0">

                <div class="modal-header border-bottom">
                    <h5 class="modal-title fw-bold">Create New Coupon Code</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Coupon Code</label>
                        <input type="text" name="code" class="form-control text-uppercase" placeholder="e.g. FESTIVE25" required>
                    </div>
                    <div class="row g-2 mb-3">
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Discount Type</label>
                            <select name="discountType" class="form-select">
                                <option value="PERCENTAGE">Percentage (%)</option>
                                <option value="FLAT">Flat Amount (₹)</option>
                            </select>
                        </div>
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Discount Value</label>
                            <input type="number" step="0.01" name="discountValue" class="form-control" placeholder="e.g. 20" required>
                        </div>
                    </div>
                    <div class="row g-2 mb-3">
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Minimum Order (₹)</label>
                            <input type="number" step="0.01" name="minimumOrder" class="form-control" value="500.00">
                        </div>
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Max Discount Cap (₹)</label>
                            <input type="number" step="0.01" name="maximumDiscount" class="form-control" value="1000.00">
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Expiry Date (Optional)</label>
                        <input type="date" name="expiryDate" class="form-control">
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="status" id="couponStatus" value="true" checked>
                        <label class="form-check-label small" for="couponStatus">Active immediately</label>
                    </div>
                </div>

                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-light rounded-pill btn-sm px-3" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-pill btn-sm px-4 fw-semibold">Create Coupon</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="../fragments/footer.jsp" %>
