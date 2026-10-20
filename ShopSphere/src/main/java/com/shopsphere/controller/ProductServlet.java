package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.model.Review;
import com.shopsphere.model.User;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Product catalog, search, filter and details.
 * Demonstrates cookies for recently viewed items.
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/products", "/product-details"})
public class ProductServlet extends HttpServlet {

    private ProductService productService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/product-details".equals(path)) {
            handleProductDetails(request, response);
        } else {
            handleProductList(request, response);
        }
    }

    private void handleProductList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("q");
        String categoryParam = request.getParameter("category");
        String minPriceParam = request.getParameter("minPrice");
        String maxPriceParam = request.getParameter("maxPrice");
        String sortBy = request.getParameter("sortBy");

        Integer categoryId = null;
        if (categoryParam != null && !categoryParam.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        Double minPrice = null;
        if (minPriceParam != null && !minPriceParam.trim().isEmpty()) {
            try {
                minPrice = Double.parseDouble(minPriceParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        Double maxPrice = null;
        if (maxPriceParam != null && !maxPriceParam.trim().isEmpty()) {
            try {
                maxPrice = Double.parseDouble(maxPriceParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        List<Product> products = productService.searchProducts(keyword, categoryId, minPrice, maxPrice, sortBy);
        List<Category> categories = productService.getAllCategories();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("selectedCategory", categoryId);
        request.setAttribute("searchQuery", keyword);
        request.setAttribute("minPrice", minPrice);
        request.setAttribute("maxPrice", maxPrice);
        request.setAttribute("sortBy", sortBy);

        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }

    private void handleProductDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        // Add Recently Viewed Cookie (Syllabus topic: Cookies for non-sensitive data)
        Cookie recentCookie = new Cookie("recentProduct", String.valueOf(productId));
        recentCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        recentCookie.setPath(request.getContextPath() + "/");
        response.addCookie(recentCookie);

        List<Review> reviews = productService.getProductReviews(productId);
        double avgRating = productService.getProductAverageRating(productId);
        List<Product> relatedProducts = productService.getProductsByCategory(product.getCategoryId());

        // Check if item is in wishlist for logged-in user
        boolean inWishlist = false;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            inWishlist = userService.isInWishlist(user.getUserId(), productId);
        }

        request.setAttribute("product", product);
        request.setAttribute("reviews", reviews);
        request.setAttribute("avgRating", avgRating);
        request.setAttribute("relatedProducts", relatedProducts);
        request.setAttribute("inWishlist", inWishlist);

        request.getRequestDispatcher("/product-details.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Submit product review
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String productIdParam = request.getParameter("productId");
        String ratingParam = request.getParameter("rating");
        String reviewText = request.getParameter("reviewText");

        if (productIdParam != null && ratingParam != null) {
            try {
                int productId = Integer.parseInt(productIdParam);
                int rating = Integer.parseInt(ratingParam);
                productService.addReview(user.getUserId(), productId, rating, reviewText);
                response.sendRedirect(request.getContextPath() + "/product-details?id=" + productId + "&reviewSuccess=true");
                return;
            } catch (Exception ignored) {}
        }

        response.sendRedirect(request.getContextPath() + "/products");
    }
}
