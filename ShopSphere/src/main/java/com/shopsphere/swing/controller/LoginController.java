package com.shopsphere.swing.controller;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.swing.view.AdminDashboardFrame;
import com.shopsphere.swing.view.LoginFrame;
import com.shopsphere.util.PasswordUtil;

import javax.swing.*;

/**
 * RTU Swing MVC Controller: LoginController
 * Handles authentication events and business logic between LoginFrame (View) and UserDAO (Model).
 * Fulfills Section 21: Swing MVC Controller pattern.
 */
public class LoginController {

    private final LoginFrame view;
    private final UserDAO userDAO;

    public LoginController(LoginFrame view) {
        this.view = view;
        this.userDAO = new UserDAO();
    }

    /**
     * Authenticates administrator credentials and transitions to AdminDashboardFrame.
     */
    public void handleLogin(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Please enter both email and password.",
                    "Input Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDAO.findByEmail(email.trim());

        if (user != null && PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                JOptionPane.showMessageDialog(view,
                        "Welcome to ShopSphere Admin Console, " + user.getName() + "!",
                        "Authentication Successful",
                        JOptionPane.INFORMATION_MESSAGE);

                view.dispose();
                SwingUtilities.invokeLater(() -> {
                    AdminDashboardFrame dashboard = new AdminDashboardFrame(user);
                    dashboard.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(view,
                        "Access Denied: Account '" + email + "' does not hold administrator privileges.",
                        "Authorization Failure",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view,
                    "Invalid email or password. Please verify your credentials.",
                    "Authentication Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
