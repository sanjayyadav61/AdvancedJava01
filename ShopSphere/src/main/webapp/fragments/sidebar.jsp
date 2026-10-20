<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="card border-0 shadow-sm rounded-4 p-3 mb-4">
    <h6 class="fw-bold mb-3 d-flex align-items-center justify-content-between">
        <span><i class="bi bi-funnel me-1 text-primary"></i> Filter Products</span>
        <a href="${pageContext.request.contextPath}/products" class="text-decoration-none small text-muted">Reset</a>
    </h6>

    <form action="${pageContext.request.contextPath}/products" method="get">
        <c:if test="${not empty searchQuery}">
            <input type="hidden" name="q" value="${searchQuery}">
        </c:if>

        <!-- Categories -->
        <div class="mb-4">
            <label class="form-label small fw-semibold text-muted text-uppercase">Categories</label>
            <div class="d-flex flex-column gap-1">
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="category" id="catAll" value="" 
                           ${empty selectedCategory ? 'checked' : ''} onchange="this.form.submit()">
                    <label class="form-check-label small" for="catAll">All Categories</label>
                </div>
                <c:forEach var="cat" items="${categories}">
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="category" id="cat_${cat.categoryId}" value="${cat.categoryId}"
                               ${selectedCategory == cat.categoryId ? 'checked' : ''} onchange="this.form.submit()">
                        <label class="form-check-label small" for="cat_${cat.categoryId}">${cat.name}</label>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Price Range -->
        <div class="mb-4">
            <label class="form-label small fw-semibold text-muted text-uppercase">Price Range (₹)</label>
            <div class="row g-2">
                <div class="col-6">
                    <input type="number" name="minPrice" value="${minPrice}" placeholder="Min" class="form-control form-control-sm">
                </div>
                <div class="col-6">
                    <input type="number" name="maxPrice" value="${maxPrice}" placeholder="Max" class="form-control form-control-sm">
                </div>
            </div>
        </div>

        <!-- Sorting -->
        <div class="mb-4">
            <label class="form-label small fw-semibold text-muted text-uppercase">Sort By</label>
            <select name="sortBy" class="form-select form-select-sm" onchange="this.form.submit()">
                <option value="" ${empty sortBy ? 'selected' : ''}>Featured / Default</option>
                <option value="price_asc" ${sortBy == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                <option value="price_desc" ${sortBy == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                <option value="discount" ${sortBy == 'discount' ? 'selected' : ''}>Biggest Discount</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary btn-sm rounded-pill w-100 fw-semibold">
            Apply Filters
        </button>
    </form>
</div>
