<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="User Profile | ShopSphere E-Commerce" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <h3 class="fw-bold mb-4">
            <i class="bi bi-person-gear text-primary me-2"></i>Account & Profile Settings
        </h3>

        <!-- Alerts -->
        <c:if test="${not empty sessionScope.profileSuccess}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>${sessionScope.profileSuccess}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="profileSuccess" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.passwordSuccess}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-shield-check me-2"></i>${sessionScope.passwordSuccess}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="passwordSuccess" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.passwordError}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>${sessionScope.passwordError}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="passwordError" scope="session"/>
        </c:if>

        <div class="row g-4">
            <!-- Left: Personal Information & Password -->
            <div class="col-lg-6">
                <!-- Info Form -->
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h5 class="fw-bold mb-3">Personal Details</h5>
                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <input type="hidden" name="action" value="updateProfile">
                        <div class="mb-3">
                            <label class="form-label small fw-semibold">Email (Permanent)</label>
                            <input type="text" class="form-control bg-light" value="${profileUser.email}" readonly>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small fw-semibold">Full Name</label>
                            <input type="text" name="name" class="form-control" value="${profileUser.name}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small fw-semibold">Mobile Number</label>
                            <input type="text" name="mobile" class="form-control" value="${profileUser.mobile}">
                        </div>
                        <button type="submit" class="btn btn-primary rounded-pill btn-sm px-4 fw-semibold">
                            Save Profile
                        </button>
                    </form>
                </div>

                <!-- Password Form -->
                <div class="card border-0 shadow-sm rounded-4 p-4">
                    <h5 class="fw-bold mb-3">Change Password</h5>
                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <input type="hidden" name="action" value="changePassword">
                        <div class="mb-3">
                            <label class="form-label small fw-semibold">Current Password</label>
                            <input type="password" name="currentPassword" class="form-control" required>
                        </div>
                        <div class="row g-2 mb-3">
                            <div class="col-6">
                                <label class="form-label small fw-semibold">New Password</label>
                                <input type="password" name="newPassword" class="form-control" required minlength="6">
                            </div>
                            <div class="col-6">
                                <label class="form-label small fw-semibold">Confirm New</label>
                                <input type="password" name="confirmPassword" class="form-control" required>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-outline-dark rounded-pill btn-sm px-4 fw-semibold">
                            Update Password
                        </button>
                    </form>
                </div>
            </div>

            <!-- Right: Delivery Addresses -->
            <div class="col-lg-6">
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h5 class="fw-bold mb-3">Saved Delivery Addresses</h5>

                    <c:choose>
                        <c:when test="${not empty addresses}">
                            <div class="d-flex flex-column gap-3 mb-4">
                                <c:forEach var="a" items="${addresses}">
                                    <div class="p-3 bg-light rounded-3 border d-flex justify-content-between align-items-center">
                                        <div>
                                            <span class="badge bg-secondary-subtle text-secondary small mb-1">${a.addressType}</span>
                                            <div class="small fw-semibold text-dark">${a.formattedAddress}</div>
                                        </div>
                                        <form action="${pageContext.request.contextPath}/profile" method="post" class="m-0">
                                            <input type="hidden" name="action" value="deleteAddress">
                                            <input type="hidden" name="addressId" value="${a.addressId}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm rounded-circle p-1" title="Delete address">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted small mb-4">No saved addresses yet. Add one below.</p>
                        </c:otherwise>
                    </c:choose>

                    <!-- Add Address Form -->
                    <h6 class="fw-bold mb-2">+ Add New Shipping Address</h6>
                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <input type="hidden" name="action" value="addAddress">
                        <div class="mb-2">
                            <input type="text" name="addressLine" class="form-control form-control-sm" placeholder="Street Address / House No." required>
                        </div>
                        <div class="row g-2 mb-2">
                            <div class="col-4">
                                <input type="text" name="city" class="form-control form-control-sm" placeholder="City" required>
                            </div>
                            <div class="col-4">
                                <input type="text" name="state" class="form-control form-control-sm" placeholder="State" required>
                            </div>
                            <div class="col-4">
                                <input type="text" name="pincode" class="form-control form-control-sm" placeholder="Pincode" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <select name="addressType" class="form-select form-select-sm">
                                <option value="HOME">Home</option>
                                <option value="WORK">Work / Office</option>
                                <option value="OTHER">Other</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-outline-primary btn-sm rounded-pill px-4">
                            Save Address
                        </button>
                    </form>
                </div>
            </div>
        </div>

    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
