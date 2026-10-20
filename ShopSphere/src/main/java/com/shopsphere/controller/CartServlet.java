package com.shopsphere.controller;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Shopping Cart management.
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        List<CartItem> cartItems = cartService.getCartItems(user.getUserId());
        double subtotal = cartService.calculateSubtotal(cartItems);

        session.setAttribute("cartCount", cartService.getCartCount(user.getUserId()));
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("subtotal", subtotal);

        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String action = request.getParameter("action");
        if (action == null) action = "add";

        try {
            switch (action) {
                case "add": {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    int quantity = 1;
                    if (request.getParameter("quantity") != null) {
                        quantity = Integer.parseInt(request.getParameter("quantity"));
                    }
                    boolean added = cartService.addToCart(user.getUserId(), productId, quantity);
                    session.setAttribute("cartCount", cartService.getCartCount(user.getUserId()));
                    if (added) {
                        session.setAttribute("cartAlert", "Product added to cart!");
                    } else {
                        session.setAttribute("cartError", "Could not add product to cart (Out of stock or unavailable).");
                    }
                    String redirect = request.getParameter("redirect");
                    if ("cart".equals(redirect)) {
                        response.sendRedirect(request.getContextPath() + "/cart");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/product-details?id=" + productId);
                    }
                    return;
                }
                case "update": {
                    int cartId = Integer.parseInt(request.getParameter("cartId"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    cartService.updateQuantity(cartId, user.getUserId(), quantity);
                    session.setAttribute("cartCount", cartService.getCartCount(user.getUserId()));
                    response.sendRedirect(request.getContextPath() + "/cart");
                    return;
                }
                case "remove": {
                    int cartId = Integer.parseInt(request.getParameter("cartId"));
                    cartService.removeFromCart(cartId, user.getUserId());
                    session.setAttribute("cartCount", cartService.getCartCount(user.getUserId()));
                    response.sendRedirect(request.getContextPath() + "/cart");
                    return;
                }
                case "clear": {
                    cartService.clearCart(user.getUserId());
                    session.setAttribute("cartCount", 0);
                    response.sendRedirect(request.getContextPath() + "/cart");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
