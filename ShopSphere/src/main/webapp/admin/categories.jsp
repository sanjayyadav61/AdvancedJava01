<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Category Management | Admin" scope="request"/>
<%@ include file="../fragments/header.jsp" %>
<%@ include file="../fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-3 border-bottom">
            <div>
                <span class="badge bg-danger rounded-pill px-3 py-1 fw-semibold small text-uppercase">Admin Portal</span>
                <h3 class="fw-bold mb-0 mt-1">Category Management</h3>
                <small class="text-muted">Manage product departments and navigation categories</small>
            </div>

            <div class="d-flex gap-2 mt-2 mt-md-0">
                <button type="button" class="btn btn-primary rounded-pill btn-sm px-4 fw-semibold shadow-sm" data-bs-toggle="modal" data-bs-target="#addCategoryModal">
                    <i class="bi bi-plus-lg me-1"></i> Add Category
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
                            <th class="ps-4">ID</th>
                            <th>Icon</th>
                            <th>Category Name</th>
                            <th>Description</th>
                            <th>Status</th>
                            <th class="text-end pe-4">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${categories}">
                            <tr>
                                <td class="ps-4 fw-bold text-muted">#${c.categoryId}</td>
                                <td>
                                    <div class="category-icon-box" style="width: 36px; height: 36px; font-size: 1rem;">
                                        <i class="${c.iconClass}"></i>
                                    </div>
                                </td>
                                <td class="fw-bold">${c.name}</td>
                                <td class="small text-muted">${c.description}</td>
                                <td>
                                    <span class="badge ${c.status ? 'bg-success' : 'bg-secondary'} rounded-pill">
                                        ${c.status ? 'Active' : 'Inactive'}
                                    </span>
                                </td>
                                <td class="text-end pe-4">
                                    <form action="${pageContext.request.contextPath}/admin/categories" method="post" class="d-inline" onsubmit="return confirm('Delete this category? Note: Categories with assigned products cannot be deleted.');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="categoryId" value="${c.categoryId}">
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

<!-- Add Category Modal -->
<div class="modal fade" id="addCategoryModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow rounded-4">
            <form action="${pageContext.request.contextPath}/admin/categories" method="post">
                <input type="hidden" name="action" value="save">
                <input type="hidden" name="categoryId" value="0">

                <div class="modal-header border-bottom">
                    <h5 class="modal-title fw-bold">Create New Category</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Category Name</label>
                        <input type="text" name="name" class="form-control" placeholder="e.g. Gaming & Consoles" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Icon CSS Class</label>
                        <input type="text" name="iconClass" class="form-control" placeholder="bi bi-controller" value="bi bi-grid">
                        <small class="text-muted">Bootstrap icon class (e.g. bi bi-laptop, bi bi-book)</small>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Description</label>
                        <textarea name="description" rows="3" class="form-control" placeholder="Department overview..."></textarea>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="status" id="catStatus" value="true" checked>
                        <label class="form-check-label small" for="catStatus">Active / Display in Navigation</label>
                    </div>
                </div>

                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-light rounded-pill btn-sm px-3" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-pill btn-sm px-4 fw-semibold">Save Category</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="../fragments/footer.jsp" %>
