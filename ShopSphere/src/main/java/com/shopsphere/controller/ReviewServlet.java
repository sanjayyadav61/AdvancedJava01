package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * RTU Advanced Java Servlet: ReviewServlet
 * Handles customer review and star-rating submissions for catalog products.
 * Demonstrates: HttpServlet, doPost(), session user retrieval, input parsing, PRG pattern.
 */
@WebServlet(name = "ReviewServlet", urlPatterns = {"/review", "/reviews"})
public class ReviewServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String productIdParam = request.getParameter("productId");
        if (productIdParam == null || productIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(productIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=" +
                    request.getContextPath() + "/product-details?id=" + productId + "&warning=login_required_review");
            return;
        }

        String ratingParam = request.getParameter("rating");
        String reviewText = request.getParameter("reviewText");

        int rating = 5;
        if (ratingParam != null && !ratingParam.trim().isEmpty()) {
            try {
                rating = Math.max(1, Math.min(5, Integer.parseInt(ratingParam)));
            } catch (NumberFormatException ignored) {
            }
        }

        productService.addReview(user.getUserId(), productId, rating, reviewText);
        response.sendRedirect(request.getContextPath() + "/product-details?id=" + productId + "&reviewSuccess=true");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");
        if (productId != null) {
            response.sendRedirect(request.getContextPath() + "/product-details?id=" + productId + "#reviews");
        } else {
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
}
