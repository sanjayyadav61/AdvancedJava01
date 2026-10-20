package com.shopsphere.service;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.OrderItemDAO;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;

import java.util.List;

/**
 * Service Layer for Order management and reporting.
 * Follows RTU Layered Architecture: OrderService -> OrderDAO / OrderItemDAO.
 */
public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();

    public Order getOrderDetails(int orderId) {
        return orderDAO.findById(orderId);
    }

    public List<OrderItem> getOrderItems(int orderId) {
        return orderItemDAO.getItemsByOrderId(orderId);
    }

    public List<Order> getUserOrders(int userId) {
        return orderDAO.findByUserId(userId);
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateStatus(orderId, status);
    }

    public double getTotalRevenue() {
        return orderDAO.getTotalRevenue();
    }

    public int getTotalOrdersCount() {
        return orderDAO.countOrders();
    }
}
