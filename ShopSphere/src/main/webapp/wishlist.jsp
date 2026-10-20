<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="My Wishlist | ShopSphere E-Commerce" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <h3 class="fw-bold mb-4">
            <i class="bi bi-heart text-danger me-2"></i>My Saved Wishlist Items
        </h3>

        <c:choose>
            <c:when test="${not empty wishlist}">
                <div class="row g-4">
                    <c:forEach var="item" items="${wishlist}">
                        <div class="col-xl-3 col-lg-4 col-md-6">
                            <div class="card h-100 border-0 shadow-sm rounded-4 overflow-hidden">
                                <a href="${pageContext.request.contextPath}/product-details?id=${item.product.productId}">
                                    <img src="${item.product.imageUrl}" alt="${item.product.name}" class="card-img-top" style="height: 200px; object-fit: cover;">
                                </a>
                                <div class="card-body d-flex flex-column p-3">
                                    <span class="small text-muted text-uppercase">${item.product.brand}</span>
                                    <h6 class="card-title fw-semibold text-truncate mb-2">
                                        <a href="${pageContext.request.contextPath}/product-details?id=${item.product.productId}" class="text-dark text-decoration-none">
                                            ${item.product.name}
                                        </a>
                                    </h6>
                                    <div class="fs-5 fw-bold text-primary mb-3">
                                        ₹<fmt:formatNumber value="${item.product.discountedPrice}" pattern="#,##0.00"/>
                                    </div>

                                    <div class="mt-auto d-grid gap-2">
                                        <form action="${pageContext.request.contextPath}/wishlist" method="post" class="m-0">
                                            <input type="hidden" name="action" value="moveToCart">
                                            <input type="hidden" name="productId" value="${item.product.productId}">
                                            <button type="submit" class="btn btn-primary btn-sm rounded-pill w-100 fw-semibold">
                                                <i class="bi bi-cart-plus me-1"></i> Move to Cart
                                            </button>
                                        </form>

                                        <form action="${pageContext.request.contextPath}/wishlist" method="post" class="m-0">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="productId" value="${item.product.productId}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm rounded-pill w-100">
                                                <i class="bi bi-trash3 me-1"></i> Remove
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>

            <c:otherwise>
                <div class="card border-0 shadow-sm rounded-4 text-center p-5">
                    <div class="display-1 text-muted mb-3"><i class="bi bi-heartbreak"></i></div>
                    <h4 class="fw-bold">Your Wishlist is Empty</h4>
                    <p class="text-muted mb-4">Save products you love and want to purchase later.</p>
                    <div>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4 py-2">
                            Browse Catalog
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
