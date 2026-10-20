package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.CouponDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.PaymentDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.model.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service Layer for Checkout, Coupon application, and Order Placement.
 * Enforces rule: "Calculate order totals on the server; do not trust browser-submitted totals."
 */
public class CheckoutService {

    private final CartDAO cartDAO = new CartDAO();
    private final CouponDAO couponDAO = new CouponDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    public static class CheckoutResult {
        private boolean success;
        private String message;
        private Order order;

        public CheckoutResult(boolean success, String message, Order order) {
            this.success = success;
            this.message = message;
            this.order = order;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Order getOrder() { return order; }
    }

    public Coupon validateCoupon(String couponCode, double subtotal) {
        if (couponCode == null || couponCode.trim().isEmpty()) {
            return null;
        }
        Coupon coupon = couponDAO.findByCode(couponCode);
        if (coupon != null && subtotal >= coupon.getMinimumOrder()) {
            return coupon;
        }
        return null;
    }

    public CheckoutResult processCheckout(int userId, int addressId, String couponCode, String paymentMethod) {
        List<CartItem> cartItems = cartDAO.getCartByUser(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            return new CheckoutResult(false, "Your shopping cart is empty.", null);
        }

        // Calculate subtotal strictly on the server
        double subtotal = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            if (ci.getProduct() == null || !ci.getProduct().isAvailable()) {
                return new CheckoutResult(false, "Item '" + (ci.getProduct() != null ? ci.getProduct().getName() : "Unknown") + "' is no longer available.", null);
            }
            if (ci.getQuantity() > ci.getProduct().getStock()) {
                return new CheckoutResult(false, "Requested quantity for '" + ci.getProduct().getName() + "' exceeds available stock (" + ci.getProduct().getStock() + ").", null);
            }

            double unitPrice = ci.getProduct().getDiscountedPrice();
            subtotal += unitPrice * ci.getQuantity();

            OrderItem item = new OrderItem();
            item.setProductId(ci.getProductId());
            item.setQuantity(ci.getQuantity());
            item.setPrice(unitPrice);
            item.setProductName(ci.getProduct().getName());
            orderItems.add(item);
        }

        // Calculate coupon discount on server
        double discount = 0.0;
        Coupon coupon = validateCoupon(couponCode, subtotal);
        if (coupon != null) {
            discount = coupon.calculateDiscount(subtotal);
        }

        double finalTotal = Math.max(0.0, subtotal - discount);
        finalTotal = Math.round(finalTotal * 100.0) / 100.0;

        // Build Order
        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setTotalAmount(finalTotal);
        order.setDiscount(discount);
        order.setPaymentMethod(paymentMethod != null ? paymentMethod.toUpperCase() : "CARD");
        order.setPaymentStatus("COD".equalsIgnoreCase(paymentMethod) ? "PENDING" : "PAID");
        order.setOrderStatus("PLACED");

        // Execute atomic transaction
        boolean created = orderDAO.createOrder(order, orderItems);
        if (!created) {
            return new CheckoutResult(false, "Failed to place order due to inventory or database error.", null);
        }

        // Create payment record
        Payment payment = new Payment();
        payment.setOrderId(order.getOrderId());
        payment.setPaymentMethod(order.getPaymentMethod());
        payment.setTransactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setAmount(finalTotal);
        payment.setPaymentStatus(order.getPaymentStatus());
        paymentDAO.createPayment(payment);

        // Record coupon usage if applied
        if (coupon != null) {
            couponDAO.recordUsage(coupon.getCouponId(), userId, order.getOrderId());
        }

        // Clear cart on successful order
        cartDAO.clearCart(userId);

        order.setItems(orderItems);
        return new CheckoutResult(true, "Order placed successfully!", order);
    }
}
