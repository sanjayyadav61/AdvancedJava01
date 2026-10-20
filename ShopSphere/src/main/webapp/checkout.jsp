<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Secure Checkout | ShopSphere E-Commerce" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <h3 class="fw-bold mb-4">
            <i class="bi bi-credit-card-2-front text-primary me-2"></i>Checkout & Payment
        </h3>

        <!-- Checkout Error Alert -->
        <c:if test="${not empty sessionScope.checkoutError}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>${sessionScope.checkoutError}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="checkoutError" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.couponSuccess}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-tag-fill me-2"></i>${sessionScope.couponSuccess}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="couponSuccess" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.couponError}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-circle-fill me-2"></i>${sessionScope.couponError}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="couponError" scope="session"/>
        </c:if>

        <div class="row g-4">
            <!-- Left Column: Address & Payment Methods -->
            <div class="col-lg-8">
                <form id="orderForm" action="${pageContext.request.contextPath}/checkout" method="post">
                    
                    <!-- 1. Shipping Address Selection -->
                    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                        <h5 class="fw-bold mb-3 d-flex align-items-center">
                            <span class="badge bg-primary rounded-circle me-2" style="width:28px; height:28px; line-height:20px;">1</span>
                            Delivery Address
                        </h5>

                        <c:if test="${not empty addresses}">
                            <div class="mb-3">
                                <label class="form-label small fw-semibold text-muted text-uppercase">Select a Saved Address:</label>
                                <div class="row g-2">
                                    <c:forEach var="addr" items="${addresses}" varStatus="status">
                                        <div class="col-md-6">
                                            <div class="border rounded-3 p-3 position-relative h-100">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="addressId" id="addr_${addr.addressId}" 
                                                           value="${addr.addressId}" ${status.first ? 'checked' : ''}>
                                                    <label class="form-check-label fw-semibold" for="addr_${addr.addressId}">
                                                        ${addr.addressType} Address
                                                    </label>
                                                    <div class="small text-muted mt-1">
                                                        ${addr.formattedAddress}
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>

                        <!-- Add / New Address Accordion -->
                        <div class="form-check mb-2">
                            <input class="form-check-input" type="radio" name="addressId" id="addr_new" value="new" ${empty addresses ? 'checked' : ''}
                                   data-bs-toggle="collapse" data-bs-target="#newAddressFields">
                            <label class="form-check-label fw-semibold" for="addr_new">
                                + Ship to a New Delivery Address
                            </label>
                        </div>

                        <div class="collapse ${empty addresses ? 'show' : ''} mt-3 p-3 bg-light rounded-3 border" id="newAddressFields">
                            <div class="mb-3">
                                <label class="form-label small fw-semibold">Street / Flat / Apartment</label>
                                <input type="text" name="addressLine" class="form-control" placeholder="Flat 402, Sunshine Heights">
                            </div>
                            <div class="row g-2">
                                <div class="col-md-4">
                                    <label class="form-label small fw-semibold">City</label>
                                    <input type="text" name="city" class="form-control" placeholder="Jaipur">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-semibold">State</label>
                                    <input type="text" name="state" class="form-control" placeholder="Rajasthan">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-semibold">Pincode</label>
                                    <input type="text" name="pincode" class="form-control" placeholder="302017">
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 2. Payment Method -->
                    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                        <h5 class="fw-bold mb-3 d-flex align-items-center">
                            <span class="badge bg-primary rounded-circle me-2" style="width:28px; height:28px; line-height:20px;">2</span>
                            Select Payment Method
                        </h5>

                        <div class="d-flex flex-column gap-2">
                            <div class="border rounded-3 p-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="pay_card" value="CARD" checked>
                                    <label class="form-check-label fw-semibold d-flex justify-content-between align-items-center" for="pay_card">
                                        <span><i class="bi bi-credit-card me-2 text-primary"></i> Credit / Debit Card</span>
                                        <span class="badge bg-light text-muted border">Visa / Mastercard / RuPay</span>
                                    </label>
                                </div>
                            </div>

                            <div class="border rounded-3 p-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="pay_upi" value="UPI">
                                    <label class="form-check-label fw-semibold d-flex justify-content-between align-items-center" for="pay_upi">
                                        <span><i class="bi bi-qr-code-scan me-2 text-success"></i> Instant UPI / QR</span>
                                        <span class="badge bg-light text-muted border">GPay / PhonePe / Paytm</span>
                                    </label>
                                </div>
                            </div>

                            <div class="border rounded-3 p-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="pay_net" value="NETBANKING">
                                    <label class="form-check-label fw-semibold d-flex justify-content-between align-items-center" for="pay_net">
                                        <span><i class="bi bi-bank me-2 text-info"></i> Net Banking</span>
                                        <span class="badge bg-light text-muted border">SBI / HDFC / ICICI / Axis</span>
                                    </label>
                                </div>
                            </div>

                            <div class="border rounded-3 p-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="pay_cod" value="COD">
                                    <label class="form-check-label fw-semibold d-flex justify-content-between align-items-center" for="pay_cod">
                                        <span><i class="bi bi-cash-stack me-2 text-warning"></i> Cash on Delivery (COD)</span>
                                        <span class="badge bg-light text-muted border">Pay at doorstep</span>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Place Order Button -->
                    <button type="submit" class="btn btn-primary btn-lg rounded-pill w-100 py-3 fw-bold shadow">
                        <i class="bi bi-shield-check me-2"></i> Confirm & Place Order (₹<fmt:formatNumber value="${grandTotal}" pattern="#,##0.00"/>)
                    </button>
                </form>
            </div>

            <!-- Right Column: Coupons & Items Summary -->
            <div class="col-lg-4">
                
                <!-- Coupon Box -->
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h6 class="fw-bold mb-3"><i class="bi bi-tag text-primary me-1"></i> Apply Discount Coupon</h6>
                    
                    <c:choose>
                        <c:when test="${not empty appliedCoupon}">
                            <div class="p-3 bg-success-subtle text-success-emphasis rounded-3 border border-success-subtle mb-2 d-flex justify-content-between align-items-center">
                                <div>
                                    <div class="fw-bold">${appliedCoupon.code} Applied</div>
                                    <small>Saved ₹<fmt:formatNumber value="${discount}" pattern="#,##0.00"/></small>
                                </div>
                                <form action="${pageContext.request.contextPath}/checkout" method="post" class="m-0">
                                    <input type="hidden" name="action" value="removeCoupon">
                                    <button type="submit" class="btn btn-sm btn-outline-danger rounded-pill">Remove</button>
                                </form>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/checkout" method="post" class="d-flex gap-2 mb-2">
                                <input type="hidden" name="action" value="applyCoupon">
                                <input type="text" name="couponCode" id="couponCodeInput" class="form-control form-control-sm text-uppercase" placeholder="e.g. WELCOME50">
                                <button type="submit" class="btn btn-outline-primary btn-sm px-3 fw-semibold">Apply</button>
                            </form>
                            <small class="text-muted">Try: <code>WELCOME50</code>, <code>FESTIVE20</code>, <code>SUPER100</code></small>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Price Breakdown -->
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h6 class="fw-bold mb-3 border-bottom pb-2">Order Price Details</h6>

                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted">Items Subtotal</span>
                        <span class="fw-semibold">₹<fmt:formatNumber value="${subtotal}" pattern="#,##0.00"/></span>
                    </div>

                    <c:if test="${discount > 0}">
                        <div class="d-flex justify-content-between mb-2 text-success">
                            <span>Coupon Discount</span>
                            <span class="fw-semibold">-₹<fmt:formatNumber value="${discount}" pattern="#,##0.00"/></span>
                        </div>
                    </c:if>

                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted">Delivery Charges</span>
                        <span class="text-success fw-semibold">FREE</span>
                    </div>

                    <hr class="my-2">

                    <div class="d-flex justify-content-between align-items-baseline fs-5 fw-bold text-dark pt-1">
                        <span>Total Payable</span>
                        <span class="text-primary">₹<fmt:formatNumber value="${grandTotal}" pattern="#,##0.00"/></span>
                    </div>
                </div>

                <!-- Cart Items Thumbnail List -->
                <div class="card border-0 shadow-sm rounded-4 p-4">
                    <h6 class="fw-bold mb-3">Items in This Order (${cartItems.size()})</h6>
                    <div class="d-flex flex-column gap-3">
                        <c:forEach var="item" items="${cartItems}">
                            <div class="d-flex align-items-center gap-3">
                                <img src="${item.product.imageUrl}" alt="${item.product.name}" class="rounded border" style="width: 50px; height: 50px; object-fit: cover;">
                                <div class="flex-grow-1 text-truncate">
                                    <div class="small fw-semibold text-truncate">${item.product.name}</div>
                                    <small class="text-muted">Qty: ${item.quantity} × ₹<fmt:formatNumber value="${item.product.discountedPrice}" pattern="#,##0.00"/></small>
                                </div>
                                <div class="small fw-bold">₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00"/></div>
                            </div>
                        </c:forEach>
                    </div>
                </div>

            </div>
        </div>

    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
