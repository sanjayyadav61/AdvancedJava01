package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for User Authentication (Customer and Admin).
 * Handles Session management, role assignment and convenience Cookies.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

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

        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
            return;
        }

        // Check for remembered email cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("rememberedEmail".equals(c.getName())) {
                    request.setAttribute("savedEmail", c.getValue());
                    break;
                }
            }
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email and Password are required.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        User user = userService.authenticate(email.trim(), password.trim());

        if (user != null) {
            // Establish authenticated session
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            session.setAttribute("cartCount", cartService.getCartCount(user.getUserId()));

            // Handle Remember Me Cookie (Non-sensitive email only)
            Cookie emailCookie = new Cookie("rememberedEmail", user.getEmail());
            if ("on".equalsIgnoreCase(rememberMe) || "true".equalsIgnoreCase(rememberMe)) {
                emailCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
            } else {
                emailCookie.setMaxAge(0); // clear cookie
            }
            emailCookie.setPath(request.getContextPath() + "/");
            response.addCookie(emailCookie);

            // Redirect to target URL if present
            String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            if (redirectUrl != null) {
                session.removeAttribute("redirectAfterLogin");
                response.sendRedirect(redirectUrl);
                return;
            }

            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } else {
            request.setAttribute("errorMessage", "Invalid email or password. Please try again.");
            request.setAttribute("enteredEmail", email);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
