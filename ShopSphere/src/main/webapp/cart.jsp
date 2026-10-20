<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Shopping Cart | ShopSphere E-Commerce" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <h3 class="fw-bold mb-4">
            <i class="bi bi-cart3 text-primary me-2"></i>Your Shopping Cart
        </h3>

        <c:choose>
            <c:when test="${not empty cartItems}">
                <div class="row g-4">
                    <!-- Cart Items List -->
                    <div class="col-lg-8">
                        <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-3">
                            <div class="table-responsive">
                                <table class="table align-middle mb-0">
                                    <thead class="table-light">
                                        <tr class="small text-uppercase text-muted">
                                            <th scope="col" class="ps-4">Product</th>
                                            <th scope="col">Price</th>
                                            <th scope="col" style="width: 140px;">Quantity</th>
                                            <th scope="col">Subtotal</th>
                                            <th scope="col" class="text-end pe-4">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${cartItems}">
                                            <tr>
                                                <td class="ps-4 py-3">
                                                    <div class="d-flex align-items-center gap-3">
                                                        <img src="${empty item.product.imageUrl ? 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=200' : item.product.imageUrl}" 
                                                             alt="${item.product.name}" class="cart-item-img border">
                                                        <div>
                                                            <span class="small text-muted text-uppercase">${item.product.brand}</span>
                                                            <h6 class="mb-0 fw-semibold">
                                                                <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" class="text-dark text-decoration-none">
                                                                    ${item.product.name}
                                                                </a>
                                                            </h6>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <span class="fw-semibold">₹<fmt:formatNumber value="${item.product.discountedPrice}" pattern="#,##0.00"/></span>
                                                </td>
                                                <td>
                                                    <form action="${pageContext.request.contextPath}/cart" method="post" class="d-flex align-items-center gap-1">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="cartId" value="${item.cartId}">
                                                        
                                                        <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.product.stock}" 
                                                               class="form-control form-control-sm text-center" style="width: 70px;" onchange="this.form.submit()">
                                                    </form>
                                                </td>
                                                <td>
                                                    <span class="fw-bold text-primary">₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00"/></span>
                                                </td>
                                                <td class="text-end pe-4">
                                                    <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline">
                                                        <input type="hidden" name="action" value="remove">
                                                        <input type="hidden" name="cartId" value="${item.cartId}">
                                                        <button type="submit" class="btn btn-outline-danger btn-sm rounded-circle p-2 line-height-1" title="Remove item">
                                                            <i class="bi bi-trash3"></i>
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- Actions Row -->
                        <div class="d-flex justify-content-between align-items-center">
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary rounded-pill btn-sm px-3">
                                <i class="bi bi-arrow-left me-1"></i> Continue Shopping
                            </a>
                            <form action="${pageContext.request.contextPath}/cart" method="post" onsubmit="return confirm('Clear entire cart?');">
                                <input type="hidden" name="action" value="clear">
                                <button type="submit" class="btn btn-outline-danger rounded-pill btn-sm px-3">
                                    <i class="bi bi-trash me-1"></i> Clear Cart
                                </button>
                            </form>
                        </div>
                    </div>

                    <!-- Order Summary Card -->
                    <div class="col-lg-4">
                        <div class="card order-summary-card shadow-sm p-4 sticky-top" style="top: 90px;">
                            <h5 class="fw-bold mb-3 border-bottom pb-2">Order Summary</h5>

                            <div class="d-flex justify-content-between mb-2">
                                <span class="text-muted">Subtotal (${cartItems.size()} items)</span>
                                <span class="fw-semibold">₹<fmt:formatNumber value="${subtotal}" pattern="#,##0.00"/></span>
                            </div>

                            <div class="d-flex justify-content-between mb-2">
                                <span class="text-muted">Estimated Delivery</span>
                                <span class="text-success fw-semibold">FREE</span>
                            </div>

                            <hr class="my-3">

                            <div class="d-flex justify-content-between align-items-baseline mb-4">
                                <span class="fs-5 fw-bold">Grand Total</span>
                                <span class="fs-4 fw-bold text-primary">₹<fmt:formatNumber value="${subtotal}" pattern="#,##0.00"/></span>
                            </div>

                            <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary rounded-pill py-2 w-100 fw-bold shadow-sm">
                                Proceed to Checkout <i class="bi bi-arrow-right ms-1"></i>
                            </a>

                            <div class="mt-3 text-center small text-muted">
                                <i class="bi bi-shield-lock-fill text-success me-1"></i> Safe & Secure 256-Bit SSL Checkout
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <div class="card border-0 shadow-sm rounded-4 text-center p-5 my-4">
                    <div class="display-1 text-muted mb-3"><i class="bi bi-cart-x"></i></div>
                    <h4 class="fw-bold">Your Cart is Currently Empty</h4>
                    <p class="text-muted mb-4">Looks like you haven't added any products to your bag yet.</p>
                    <div>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4 py-2 fw-semibold">
                            Start Shopping Now <i class="bi bi-arrow-right ms-1"></i>
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
