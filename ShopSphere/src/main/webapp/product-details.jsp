<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="pageTitle" value="${product.name} | ShopSphere" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products" class="text-decoration-none">Products</a></li>
                <li class="breadcrumb-item active" aria-current="page">${product.name}</li>
            </ol>
        </nav>

        <c:if test="${param.reviewSuccess eq 'true'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-star-fill me-2 text-warning"></i>Thank you! Your customer review has been published.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Product Hero Card -->
        <div class="card border-0 shadow-sm rounded-4 overflow-hidden p-4 mb-5">
            <div class="row g-5">
                <!-- Image Gallery -->
                <div class="col-lg-6">
                    <div class="rounded-4 overflow-hidden bg-light text-center border p-3">
                        <img src="${empty product.imageUrl ? 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800' : product.imageUrl}" 
                             alt="${product.name}" class="img-fluid object-fit-contain rounded-3" style="max-height: 440px;">
                    </div>
                </div>

                <!-- Product Info & Purchase Actions -->
                <div class="col-lg-6 d-flex flex-column">
                    <div class="mb-2">
                        <span class="badge bg-light text-primary border px-3 py-1 rounded-pill text-uppercase fw-bold small">
                            ${product.brand}
                        </span>
                        <c:if test="${product.discount > 0}">
                            <span class="badge bg-danger rounded-pill px-3 py-1 fw-bold small ms-1">
                                ${product.discount}% OFF
                            </span>
                        </c:if>
                    </div>

                    <h2 class="fw-bold text-dark mb-2">${product.name}</h2>

                    <!-- Star Ratings Tag Component -->
                    <div class="d-flex align-items-center gap-2 mb-3">
                        <tags:rating rating="${avgRating}" />
                        <span class="text-muted small">|</span>
                        <a href="#reviewsSection" class="small text-decoration-none text-muted">${reviews.size()} Customer Reviews</a>
                    </div>

                    <hr class="my-2 border-secondary border-opacity-10">

                    <!-- Pricing Display -->
                    <div class="my-3">
                        <div class="d-flex align-items-baseline gap-3">
                            <span class="display-6 fw-bold text-primary">₹<fmt:formatNumber value="${product.discountedPrice}" pattern="#,##0.00"/></span>
                            <c:if test="${product.discount > 0}">
                                <span class="fs-5 text-muted text-decoration-line-through">₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span>
                                <span class="badge bg-success-subtle text-success small">
                                    Save ₹<fmt:formatNumber value="${product.price - product.discountedPrice}" pattern="#,##0.00"/>
                                </span>
                            </c:if>
                        </div>
                        <small class="text-muted">Inclusive of all local taxes. Free shipping eligible.</small>
                    </div>

                    <!-- Stock Status -->
                    <div class="mb-4">
                        <c:choose>
                            <c:when test="${product.stock <= 0}">
                                <span class="badge bg-danger rounded-pill px-3 py-2 fs-6">
                                    <i class="bi bi-x-circle me-1"></i> Currently Out of Stock
                                </span>
                            </c:when>
                            <c:when test="${product.lowStock}">
                                <span class="badge bg-warning rounded-pill px-3 py-2 fs-6 text-dark">
                                    <i class="bi bi-exclamation-triangle-fill me-1"></i> Low Stock Alert: Only ${product.stock} units remaining!
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-success rounded-pill px-3 py-2 fs-6">
                                    <i class="bi bi-check-circle-fill me-1"></i> In Stock (${product.stock} available)
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <p class="text-secondary mb-4 leading-relaxed">${product.description}</p>

                    <!-- Add to Cart & Wishlist Actions -->
                    <div class="mt-auto pt-3 border-top">
                        <form action="${pageContext.request.contextPath}/cart" method="post" class="row g-2 align-items-center">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${product.productId}">
                            <input type="hidden" name="redirect" value="cart">

                            <div class="col-sm-4 col-5">
                                <div class="input-group">
                                    <span class="input-group-text bg-light">Qty</span>
                                    <input type="number" name="quantity" value="1" min="1" max="${product.stock}" class="form-control text-center">
                                </div>
                            </div>

                            <div class="col-sm-5 col-7">
                                <c:choose>
                                    <c:when test="${product.stock > 0}">
                                        <button type="submit" class="btn btn-primary rounded-pill w-100 py-2 fw-semibold shadow-sm">
                                            <i class="bi bi-cart-plus me-1"></i> Add to Cart
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn btn-secondary rounded-pill w-100 py-2 disabled" disabled>
                                            Out of Stock
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="col-sm-3 col-12">
                                <c:choose>
                                    <c:when test="${inWishlist}">
                                        <a href="${pageContext.request.contextPath}/wishlist" class="btn btn-outline-danger rounded-pill w-100 py-2">
                                            <i class="bi bi-heart-fill me-1"></i> Saved
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <form action="${pageContext.request.contextPath}/wishlist" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="add">
                                            <input type="hidden" name="productId" value="${product.productId}">
                                            <input type="hidden" name="redirect" value="details">
                                            <button type="submit" class="btn btn-outline-secondary rounded-pill w-100 py-2">
                                                <i class="bi bi-heart me-1"></i> Wishlist
                                            </button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
        </div>

        <!-- Reviews Section -->
        <section id="reviewsSection" class="row g-4 mb-5">
            <div class="col-lg-7">
                <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
                    <h4 class="fw-bold mb-3 d-flex align-items-center justify-content-between">
                        <span>Customer Feedback & Ratings</span>
                        <span class="badge bg-light text-dark border">${reviews.size()} Reviews</span>
                    </h4>

                    <c:choose>
                        <c:when test="${not empty reviews}">
                            <div class="d-flex flex-column gap-3">
                                <c:forEach var="rev" items="${reviews}">
                                    <div class="p-3 bg-light rounded-3 border">
                                        <div class="d-flex justify-content-between align-items-center mb-1">
                                            <span class="fw-bold text-dark"><i class="bi bi-person-circle me-1 text-primary"></i>${rev.userName}</span>
                                            <tags:rating rating="${rev.rating * 1.0}" />
                                        </div>
                                        <p class="mb-1 text-secondary small">${rev.reviewText}</p>
                                        <small class="text-muted" style="font-size: 0.75rem;">Reviewed on ${rev.createdAt}</small>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted">No reviews yet for this product. Be the first to share your thoughts!</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Write a Review Box -->
            <div class="col-lg-5">
                <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
                    <h5 class="fw-bold mb-2">Write a Review</h5>
                    <p class="text-muted small mb-3">Share your product experience with RTU peers</p>

                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <form action="${pageContext.request.contextPath}/review" method="post">
                                <input type="hidden" name="productId" value="${product.productId}">
                                <div class="mb-3">
                                    <label class="form-label small fw-semibold">Overall Rating</label>
                                    <select name="rating" class="form-select" required>
                                        <option value="5" selected>★★★★★ 5 Stars - Outstanding</option>
                                        <option value="4">★★★★☆ 4 Stars - Very Good</option>
                                        <option value="3">★★★☆☆ 3 Stars - Average</option>
                                        <option value="2">★★☆☆☆ 2 Stars - Below Average</option>
                                        <option value="1">★☆☆☆☆ 1 Star - Poor</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label small fw-semibold">Your Feedback</label>
                                    <textarea name="reviewText" rows="4" class="form-control" placeholder="What did you like or dislike about this product?" required></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary rounded-pill w-100 py-2 fw-semibold">
                                    Submit Review
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div class="p-4 bg-light rounded-3 text-center">
                                <i class="bi bi-lock-fill text-muted fs-3 mb-2 d-block"></i>
                                <p class="small text-muted mb-3">Please sign in to post a rating and review for this product.</p>
                                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline-primary btn-sm rounded-pill px-4">
                                    Sign In to Review
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </section>

    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
