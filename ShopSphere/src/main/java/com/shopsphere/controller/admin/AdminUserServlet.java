package com.shopsphere.controller.admin;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Admin Customer Management and account status toggling.
 */
@WebServlet(name = "AdminUserServlet", urlPatterns = {"/admin/users"})
public class AdminUserServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);

        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("toggleStatus".equalsIgnoreCase(action)) {
            try {
                int userId = Integer.parseInt(request.getParameter("userId"));
                String currentStatus = request.getParameter("currentStatus");
                String newStatus = "ACTIVE".equalsIgnoreCase(currentStatus) ? "BLOCKED" : "ACTIVE";

                userService.updateUserStatus(userId, newStatus);
                request.getSession().setAttribute("adminSuccess", "User status changed to " + newStatus);
            } catch (Exception e) {
                request.getSession().setAttribute("adminError", "Failed to update user status: " + e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
