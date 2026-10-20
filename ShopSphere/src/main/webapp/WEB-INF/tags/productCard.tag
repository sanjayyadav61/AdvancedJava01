<%@ tag description="Reusable E-Commerce Product Card Component" pageEncoding="UTF-8"%>
<%@ attribute name="product" type="com.shopsphere.model.Product" required="true" description="Product object" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<div class="card product-card h-100 border-0 shadow-sm transition-all position-relative">
    <c:if test="${product.discount > 0}">
        <span class="badge bg-danger position-absolute top-0 start-0 m-3 px-2 py-1 shadow-sm rounded-pill">
            <i class="bi bi-tag-fill me-1"></i>${product.discount}% OFF
        </span>
    </c:if>

    <!-- Wishlist Button -->
    <form action="${pageContext.request.contextPath}/wishlist" method="post" class="position-absolute top-0 end-0 m-3 z-2">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="productId" value="${product.productId}">
        <button type="submit" class="btn btn-light btn-sm rounded-circle shadow-sm text-danger p-2 line-height-1" title="Add to Wishlist">
            <i class="bi bi-heart"></i>
        </button>
    </form>

    <!-- Product Image -->
    <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}" class="overflow-hidden bg-light rounded-top text-center d-block">
        <img src="${empty product.imageUrl ? 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600' : product.imageUrl}" 
             alt="${product.name}" 
             class="card-img-top product-thumb object-fit-cover" 
             style="height: 220px; width: 100%;">
    </a>

    <!-- Card Body -->
    <div class="card-body d-flex flex-column p-3">
        <div class="d-flex justify-content-between align-items-center mb-1">
            <span class="text-uppercase small fw-bold text-muted">${product.brand}</span>
            <c:choose>
                <c:when test="${product.stock <= 0}">
                    <span class="badge bg-secondary-subtle text-secondary small">Out of Stock</span>
                </c:when>
                <c:when test="${product.lowStock}">
                    <span class="badge bg-warning-subtle text-warning-emphasis small">Only ${product.stock} left!</span>
                </c:when>
                <c:otherwise>
                    <span class="badge bg-success-subtle text-success small">In Stock</span>
                </c:otherwise>
            </c:choose>
        </div>

        <h6 class="card-title mb-2 text-truncate-2">
            <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}" class="text-dark text-decoration-none fw-semibold">
                ${product.name}
            </a>
        </h6>

        <!-- Pricing -->
        <div class="mt-auto pt-2">
            <div class="d-flex align-items-baseline gap-2 mb-3">
                <span class="fs-5 fw-bold text-primary">₹<fmt:formatNumber value="${product.discountedPrice}" pattern="#,##0.00"/></span>
                <c:if test="${product.discount > 0}">
                    <span class="text-muted text-decoration-line-through small">₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span>
                </c:if>
            </div>

            <!-- Add to Cart Form -->
            <form action="${pageContext.request.contextPath}/cart" method="post" class="d-grid">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="productId" value="${product.productId}">
                <input type="hidden" name="quantity" value="1">
                <input type="hidden" name="redirect" value="cart">
                <c:choose>
                    <c:when test="${product.stock > 0}">
                        <button type="submit" class="btn btn-outline-primary btn-sm rounded-pill fw-semibold py-2">
                            <i class="bi bi-cart-plus me-1"></i> Add to Cart
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-secondary btn-sm rounded-pill disabled py-2" disabled>
                            Out of Stock
                        </button>
                    </c:otherwise>
                </c:choose>
            </form>
        </div>
    </div>
</div>
