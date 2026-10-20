<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:if test="${empty products}">
    <%
        com.shopsphere.service.ProductService ps = new com.shopsphere.service.ProductService();
        request.setAttribute("products", ps.getAllActiveProducts());
        request.setAttribute("categories", ps.getAllCategories());
    %>
</c:if>

<c:set var="pageTitle" value="Product Catalog | ShopSphere E-Commerce" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-3">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none">Home</a></li>
                <li class="breadcrumb-item active" aria-current="page">All Products</li>
            </ol>
        </nav>

        <div class="row g-4">
            <!-- Sidebar Filters -->
            <div class="col-lg-3">
                <%@ include file="fragments/sidebar.jsp" %>
            </div>

            <!-- Product Grid -->
            <div class="col-lg-9">
                <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-2 border-bottom">
                    <div>
                        <h4 class="fw-bold mb-0">Products Catalog</h4>
                        <small class="text-muted">Showing ${products.size()} items matching your criteria</small>
                    </div>

                    <c:if test="${not empty searchQuery}">
                        <span class="badge bg-light text-dark border px-3 py-2 rounded-pill">
                            Search: "${searchQuery}"
                            <a href="${pageContext.request.contextPath}/products" class="text-muted ms-2"><i class="bi bi-x-circle"></i></a>
                        </span>
                    </c:if>
                </div>

                <c:choose>
                    <c:when test="${not empty products}">
                        <div class="row g-4">
                            <c:forEach var="p" items="${products}">
                                <div class="col-xl-4 col-md-6">
                                    <tags:productCard product="${p}" />
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="card border-0 shadow-sm rounded-4 text-center p-5">
                            <div class="display-1 text-muted mb-3"><i class="bi bi-search"></i></div>
                            <h4 class="fw-bold">No Products Found</h4>
                            <p class="text-muted">We couldn't find any products matching your selected filters.</p>
                            <div>
                                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4">
                                    Clear All Filters
                                </a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>

    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
