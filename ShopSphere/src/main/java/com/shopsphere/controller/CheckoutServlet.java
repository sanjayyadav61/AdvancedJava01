package com.shopsphere.controller;

import com.shopsphere.model.Address;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CheckoutService;
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
 * Controller for Checkout and Order Placement.
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private CheckoutService checkoutService;
    private CartService cartService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        checkoutService = new CheckoutService();
        cartService = new CartService();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        List<CartItem> cartItems = cartService.getCartItems(user.getUserId());
        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        double subtotal = cartService.calculateSubtotal(cartItems);
        List<Address> addresses = userService.getUserAddresses(user.getUserId());

        // Check for applied coupon in session
        String couponCode = (String) session.getAttribute("appliedCouponCode");
        Coupon coupon = null;
        double discount = 0.0;
        if (couponCode != null) {
            coupon = checkoutService.validateCoupon(couponCode, subtotal);
            if (coupon != null) {
                discount = coupon.calculateDiscount(subtotal);
            } else {
                session.removeAttribute("appliedCouponCode");
            }
        }

        double grandTotal = Math.max(0.0, subtotal - discount);

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("discount", discount);
        request.setAttribute("grandTotal", grandTotal);
        request.setAttribute("addresses", addresses);
        request.setAttribute("appliedCoupon", coupon);

        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String action = request.getParameter("action");

        if ("applyCoupon".equalsIgnoreCase(action)) {
            String code = request.getParameter("couponCode");
            List<CartItem> cartItems = cartService.getCartItems(user.getUserId());
            double subtotal = cartService.calculateSubtotal(cartItems);

            Coupon coupon = checkoutService.validateCoupon(code, subtotal);
            if (coupon != null) {
                session.setAttribute("appliedCouponCode", coupon.getCode());
                session.setAttribute("couponSuccess", "Coupon '" + coupon.getCode() + "' applied successfully!");
            } else {
                session.removeAttribute("appliedCouponCode");
                session.setAttribute("couponError", "Invalid or expired coupon code, or minimum order not met.");
            }
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }

        if ("removeCoupon".equalsIgnoreCase(action)) {
            session.removeAttribute("appliedCouponCode");
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }

        // Place Order
        String addressIdStr = request.getParameter("addressId");
        String newAddressLine = request.getParameter("addressLine");
        String newCity = request.getParameter("city");
        String newState = request.getParameter("state");
        String newPincode = request.getParameter("pincode");
        String paymentMethod = request.getParameter("paymentMethod");

        int addressId = 0;

        // If user submitted a new address directly in checkout
        if ("new".equalsIgnoreCase(addressIdStr) || (addressIdStr == null && newAddressLine != null && !newAddressLine.trim().isEmpty())) {
            Address newAddr = new Address();
            newAddr.setUserId(user.getUserId());
            newAddr.setAddressLine(newAddressLine);
            newAddr.setCity(newCity);
            newAddr.setState(newState);
            newAddr.setPincode(newPincode);
            newAddr.setAddressType("HOME");
            newAddr.setDefault(true);
            userService.addAddress(newAddr);
            addressId = newAddr.getAddressId();
        } else if (addressIdStr != null) {
            try {
                addressId = Integer.parseInt(addressIdStr);
            } catch (NumberFormatException ignored) {}
        }

        if (addressId <= 0) {
            session.setAttribute("checkoutError", "Please select or provide a valid delivery address.");
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }

        String couponCode = (String) session.getAttribute("appliedCouponCode");
        CheckoutService.CheckoutResult result = checkoutService.processCheckout(
                user.getUserId(), addressId, couponCode, paymentMethod
        );

        if (result.isSuccess()) {
            session.removeAttribute("appliedCouponCode");
            session.setAttribute("cartCount", 0);
            response.sendRedirect(request.getContextPath() + "/order-success.jsp?orderId=" + result.getOrder().getOrderId());
        } else {
            session.setAttribute("checkoutError", result.getMessage());
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
}
