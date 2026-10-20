package com.shopsphere.controller.admin;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * RTU Advanced Java Servlet: AdminLoginServlet
 * Handles dedicated administrative authentication for the ShopSphere store.
 * Demonstrates: HttpServlet, doPost(), doGet(), role validation, session attribution.
 */
@WebServlet(name = "AdminLoginServlet", urlPatterns = {"/admin/login"})
public class AdminLoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            }
        }
        // Forward to the login view with admin intent
        request.setAttribute("isAdminPortal", true);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userService.login(email, password);

        if (user != null && "ADMIN".equalsIgnoreCase(user.getRole())) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            request.setAttribute("error", "Invalid administrator credentials or unauthorized role.");
            request.setAttribute("isAdminPortal", true);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
