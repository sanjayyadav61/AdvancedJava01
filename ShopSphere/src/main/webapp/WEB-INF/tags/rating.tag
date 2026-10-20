<%@ tag description="Product Star Rating Display Component" pageEncoding="UTF-8"%>
<%@ attribute name="rating" type="java.lang.Double" required="true" description="Average rating score from 1.0 to 5.0" %>
<%@ attribute name="maxStars" type="java.lang.Integer" required="false" description="Total stars (defaults to 5)" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="max" value="${empty maxStars ? 5 : maxStars}" />
<div class="rating-stars d-inline-flex align-items-center gap-1 text-warning">
    <c:forEach var="i" begin="1" end="${max}">
        <c:choose>
            <c:when test="${rating >= i}">
                <i class="bi bi-star-fill"></i>
            </c:when>
            <c:when test="${rating >= (i - 0.5)}">
                <i class="bi bi-star-half"></i>
            </c:when>
            <c:otherwise>
                <i class="bi bi-star"></i>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <span class="ms-1 text-muted small fw-semibold">(${rating > 0 ? rating : 'No ratings'})</span>
</div>
