package com.shopsphere.controller;

import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller for Customer Registration.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String mobile = request.getParameter("mobile");

        if (name == null || email == null || password == null ||
            name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "All required fields must be filled.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("mobile", mobile);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (password.length() < 6) {
            request.setAttribute("errorMessage", "Password must be at least 6 characters long.");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("mobile", mobile);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        boolean registered = userService.register(name.trim(), email.trim().toLowerCase(), password, mobile != null ? mobile.trim() : "");

        if (registered) {
            request.getSession(true).setAttribute("successMessage", "Account created successfully! Please sign in.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            request.setAttribute("errorMessage", "An account with this email address already exists.");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("mobile", mobile);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
