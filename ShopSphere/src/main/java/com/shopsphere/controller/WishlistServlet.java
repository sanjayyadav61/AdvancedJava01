package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.model.WishlistItem;
import com.shopsphere.service.CartService;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Customer Wishlist operations.
 */
@WebServlet(name = "WishlistServlet", urlPatterns = {"/wishlist"})
public class WishlistServlet extends HttpServlet {

    private UserService userService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        List<WishlistItem> wishlist = userService.getUserWishlist(user.getUserId());
        request.setAttribute("wishlist", wishlist);

        request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String action = request.getParameter("action");
        String productIdStr = request.getParameter("productId");

        if (productIdStr != null) {
            try {
                int productId = Integer.parseInt(productIdStr);
                if ("add".equalsIgnoreCase(action)) {
                    userService.addToWishlist(user.getUserId(), productId);
                } else if ("remove".equalsIgnoreCase(action)) {
                    userService.removeFromWishlist(user.getUserId(), productId);
                } else if ("moveToCart".equalsIgnoreCase(action)) {
                    cartService.addToCart(user.getUserId(), productId, 1);
                    userService.removeFromWishlist(user.getUserId(), productId);
                    session.setAttribute("cartCount", cartService.getCartCount(user.getUserId()));
                }
            } catch (NumberFormatException ignored) {}
        }

        String redirect = request.getParameter("redirect");
        if ("details".equalsIgnoreCase(redirect)) {
            response.sendRedirect(request.getContextPath() + "/product-details?id=" + productIdStr);
        } else {
            response.sendRedirect(request.getContextPath() + "/wishlist");
        }
    }
}
