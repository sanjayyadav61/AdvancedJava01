package com.shopsphere.controller;

import com.shopsphere.model.Address;
import com.shopsphere.model.User;
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
 * Controller for Customer Profile and Address management.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        User user = userService.getUserById(sessionUser.getUserId());
        List<Address> addresses = userService.getUserAddresses(user.getUserId());

        request.setAttribute("profileUser", user);
        request.setAttribute("addresses", addresses);

        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        String action = request.getParameter("action");

        if ("updateProfile".equalsIgnoreCase(action)) {
            String name = request.getParameter("name");
            String mobile = request.getParameter("mobile");

            User u = new User();
            u.setUserId(sessionUser.getUserId());
            u.setName(name);
            u.setMobile(mobile);

            boolean ok = userService.updateProfile(u);
            if (ok) {
                sessionUser.setName(name);
                sessionUser.setMobile(mobile);
                session.setAttribute("user", sessionUser);
                session.setAttribute("profileSuccess", "Profile updated successfully!");
            } else {
                session.setAttribute("profileError", "Failed to update profile.");
            }
        } else if ("changePassword".equalsIgnoreCase(action)) {
            String currentPass = request.getParameter("currentPassword");
            String newPass = request.getParameter("newPassword");
            String confirmPass = request.getParameter("confirmPassword");

            User validated = userService.authenticate(sessionUser.getEmail(), currentPass);
            if (validated == null) {
                session.setAttribute("passwordError", "Current password is incorrect.");
            } else if (!newPass.equals(confirmPass)) {
                session.setAttribute("passwordError", "New passwords do not match.");
            } else if (newPass.length() < 6) {
                session.setAttribute("passwordError", "Password must be at least 6 characters.");
            } else {
                userService.updatePassword(sessionUser.getUserId(), newPass);
                session.setAttribute("passwordSuccess", "Password updated successfully!");
            }
        } else if ("addAddress".equalsIgnoreCase(action)) {
            String addressLine = request.getParameter("addressLine");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String pincode = request.getParameter("pincode");
            String addressType = request.getParameter("addressType");

            Address a = new Address();
            a.setUserId(sessionUser.getUserId());
            a.setAddressLine(addressLine);
            a.setCity(city);
            a.setState(state);
            a.setPincode(pincode);
            a.setAddressType(addressType != null ? addressType : "HOME");

            userService.addAddress(a);
            session.setAttribute("addressSuccess", "New address added successfully!");
        } else if ("deleteAddress".equalsIgnoreCase(action)) {
            int addressId = Integer.parseInt(request.getParameter("addressId"));
            userService.deleteAddress(addressId, sessionUser.getUserId());
            session.setAttribute("addressSuccess", "Address removed.");
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
