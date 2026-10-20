<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Product Management | Admin" scope="request"/>
<%@ include file="../fragments/header.jsp" %>
<%@ include file="../fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-3 border-bottom">
            <div>
                <span class="badge bg-danger rounded-pill px-3 py-1 fw-semibold small text-uppercase">Admin Portal</span>
                <h3 class="fw-bold mb-0 mt-1">Product Catalog Management</h3>
                <small class="text-muted">Total ${products.size()} products in database</small>
            </div>

            <div class="d-flex gap-2 mt-2 mt-md-0">
                <button type="button" class="btn btn-primary rounded-pill btn-sm px-4 fw-semibold shadow-sm" data-bs-toggle="modal" data-bs-target="#addProductModal">
                    <i class="bi bi-plus-lg me-1"></i> Add New Product
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

        <c:if test="${not empty sessionScope.adminError}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>${sessionScope.adminError}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="adminError" scope="session"/>
        </c:if>

        <!-- Products Table -->
        <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
            <div class="table-responsive">
                <table class="table align-middle table-hover mb-0">
                    <thead class="table-light small text-uppercase text-muted">
                        <tr>
                            <th class="ps-4">ID</th>
                            <th>Product</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Discount</th>
                            <th>Stock</th>
                            <th>Status</th>
                            <th class="text-end pe-4">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td class="ps-4 fw-bold text-muted">#${p.productId}</td>
                                <td>
                                    <div class="d-flex align-items-center gap-3">
                                        <img src="${p.imageUrl}" alt="${p.name}" class="rounded border" style="width: 44px; height: 44px; object-fit: cover;">
                                        <div>
                                            <div class="fw-semibold small">${p.name}</div>
                                            <small class="text-muted">${p.brand}</small>
                                        </div>
                                    </div>
                                </td>
                                <td><span class="badge bg-light text-dark border small">${p.categoryName}</span></td>
                                <td class="fw-bold">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                <td><span class="badge bg-danger-subtle text-danger">${p.discount}%</span></td>
                                <td>
                                    <span class="badge ${p.stock <= 5 ? 'bg-warning text-dark' : 'bg-success-subtle text-success'}">
                                        ${p.stock} units
                                    </span>
                                </td>
                                <td>
                                    <span class="badge ${p.status ? 'bg-success' : 'bg-secondary'} rounded-pill">
                                        ${p.status ? 'Active' : 'Hidden'}
                                    </span>
                                </td>
                                <td class="text-end pe-4">
                                    <div class="btn-group btn-group-sm">
                                        <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#editModal_${p.productId}" title="Edit Product">
                                            <i class="bi bi-pencil"></i>
                                        </button>
                                        <form action="${pageContext.request.contextPath}/admin/products" method="post" class="d-inline" onsubmit="return confirm('Delete this product permanently?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="productId" value="${p.productId}">
                                            <button type="submit" class="btn btn-outline-danger" title="Delete">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>

                            <!-- Edit Product Modal -->
                            <div class="modal fade" id="editModal_${p.productId}" tabindex="-1">
                                <div class="modal-dialog modal-lg">
                                    <div class="modal-content border-0 shadow rounded-4">
                                        <form action="${pageContext.request.contextPath}/admin/products" method="post">
                                            <input type="hidden" name="action" value="save">
                                            <input type="hidden" name="productId" value="${p.productId}">
                                            
                                            <div class="modal-header border-bottom">
                                                <h5 class="modal-title fw-bold">Edit Product #${p.productId}</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                            </div>

                                            <div class="modal-body p-4">
                                                <div class="row g-3">
                                                    <div class="col-md-6">
                                                        <label class="form-label small fw-semibold">Product Name</label>
                                                        <input type="text" name="name" class="form-control" value="${p.name}" required>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label class="form-label small fw-semibold">Brand</label>
                                                        <input type="text" name="brand" class="form-control" value="${p.brand}" required>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label class="form-label small fw-semibold">Category</label>
                                                        <select name="categoryId" class="form-select" required>
                                                            <c:forEach var="cat" items="${categories}">
                                                                <option value="${cat.categoryId}" ${cat.categoryId == p.categoryId ? 'selected' : ''}>${cat.name}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                    <div class="col-md-3">
                                                        <label class="form-label small fw-semibold">Price (₹)</label>
                                                        <input type="number" step="0.01" name="price" class="form-control" value="${p.price}" required>
                                                    </div>
                                                    <div class="col-md-3">
                                                        <label class="form-label small fw-semibold">Discount (%)</label>
                                                        <input type="number" step="0.01" name="discount" class="form-control" value="${p.discount}">
                                                    </div>
                                                    <div class="col-md-4">
                                                        <label class="form-label small fw-semibold">Stock Quantity</label>
                                                        <input type="number" name="stock" class="form-control" value="${p.stock}" required>
                                                    </div>
                                                    <div class="col-md-8">
                                                        <label class="form-label small fw-semibold">Image URL</label>
                                                        <input type="text" name="imageUrl" class="form-control" value="${p.imageUrl}">
                                                    </div>
                                                    <div class="col-12">
                                                        <label class="form-label small fw-semibold">Description</label>
                                                        <textarea name="description" rows="3" class="form-control">${p.description}</textarea>
                                                    </div>
                                                    <div class="col-12">
                                                        <div class="form-check">
                                                            <input class="form-check-input" type="checkbox" name="status" id="stat_${p.productId}" value="true" ${p.status ? 'checked' : ''}>
                                                            <label class="form-check-label small" for="stat_${p.productId}">Active / Visible in Catalog</label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="modal-footer border-top">
                                                <button type="button" class="btn btn-light rounded-pill btn-sm px-3" data-bs-dismiss="modal">Cancel</button>
                                                <button type="submit" class="btn btn-primary rounded-pill btn-sm px-4 fw-semibold">Update Product</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</main>

<!-- Add Product Modal -->
<div class="modal fade" id="addProductModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content border-0 shadow rounded-4">
            <form action="${pageContext.request.contextPath}/admin/products" method="post">
                <input type="hidden" name="action" value="save">
                <input type="hidden" name="productId" value="0">
                
                <div class="modal-header border-bottom">
                    <h5 class="modal-title fw-bold">Add New Product to Catalog</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body p-4">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Product Name</label>
                            <input type="text" name="name" class="form-control" placeholder="e.g. Dell XPS 15 OLED" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Brand</label>
                            <input type="text" name="brand" class="form-control" placeholder="e.g. Dell" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Category</label>
                            <select name="categoryId" class="form-select" required>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.categoryId}">${cat.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-semibold">Price (₹)</label>
                            <input type="number" step="0.01" name="price" class="form-control" placeholder="49999.00" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-semibold">Discount (%)</label>
                            <input type="number" step="0.01" name="discount" class="form-control" value="0.00">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-semibold">Initial Stock Quantity</label>
                            <input type="number" name="stock" class="form-control" value="15" required>
                        </div>
                        <div class="col-md-8">
                            <label class="form-label small fw-semibold">Image URL</label>
                            <input type="text" name="imageUrl" class="form-control" placeholder="https://images.unsplash.com/..." value="https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800">
                        </div>
                        <div class="col-12">
                            <label class="form-label small fw-semibold">Product Description</label>
                            <textarea name="description" rows="3" class="form-control" placeholder="Key features, specifications, warranty..."></textarea>
                        </div>
                        <div class="col-12">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="status" id="newStatus" value="true" checked>
                                <label class="form-check-label small" for="newStatus">Active / Visible immediately in Store</label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-light rounded-pill btn-sm px-3" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-pill btn-sm px-4 fw-semibold">Create Product</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="../fragments/footer.jsp" %>
