<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Customer Management | Admin" scope="request"/>
<%@ include file="../fragments/header.jsp" %>
<%@ include file="../fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-3 border-bottom">
            <div>
                <span class="badge bg-danger rounded-pill px-3 py-1 fw-semibold small text-uppercase">Admin Portal</span>
                <h3 class="fw-bold mb-0 mt-1">Customer User Accounts</h3>
                <small class="text-muted">Manage registered customer accounts and access permissions</small>
            </div>

            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-secondary rounded-pill btn-sm px-3">
                Back to Dashboard
            </a>
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
                            <th class="ps-4">User ID</th>
                            <th>Name</th>
                            <th>Email Address</th>
                            <th>Mobile</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Registered On</th>
                            <th class="text-end pe-4">Account Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td class="ps-4 fw-bold text-muted">#${u.userId}</td>
                                <td>
                                    <div class="d-flex align-items-center gap-2">
                                        <div class="avatar-sm rounded-circle bg-light border p-2 text-primary">
                                            <i class="bi bi-person"></i>
                                        </div>
                                        <span class="fw-semibold small">${u.name}</span>
                                    </div>
                                </td>
                                <td class="small">${u.email}</td>
                                <td class="small text-muted">${empty u.mobile ? '-' : u.mobile}</td>
                                <td>
                                    <span class="badge ${u.admin ? 'bg-danger' : 'bg-primary-subtle text-primary'} rounded-pill">
                                        ${u.role}
                                    </span>
                                </td>
                                <td>
                                    <span class="badge ${u.status == 'ACTIVE' ? 'bg-success' : 'bg-danger'} rounded-pill">
                                        ${u.status}
                                    </span>
                                </td>
                                <td class="small text-muted">${u.createdAt}</td>
                                <td class="text-end pe-4">
                                    <c:choose>
                                        <c:when test="${u.userId != sessionScope.user.userId}">
                                            <form action="${pageContext.request.contextPath}/admin/users" method="post" class="d-inline">
                                                <input type="hidden" name="action" value="toggleStatus">
                                                <input type="hidden" name="userId" value="${u.userId}">
                                                <input type="hidden" name="currentStatus" value="${u.status}">
                                                <button type="submit" class="btn btn-sm ${u.status == 'ACTIVE' ? 'btn-outline-danger' : 'btn-outline-success'} rounded-pill px-3">
                                                    ${u.status == 'ACTIVE' ? 'Block Account' : 'Activate Account'}
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-light text-muted border">Current Admin</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</main>

<%@ include file="../fragments/footer.jsp" %>
