package com.shopsphere.controller.admin;

import com.shopsphere.model.Order;
import com.shopsphere.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Admin Order Management and status updates.
 */
@WebServlet(name = "AdminOrderServlet", urlPatterns = {"/admin/orders"})
public class AdminOrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Order> orders = orderService.getAllOrders();
        request.setAttribute("orders", orders);

        request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("updateStatus".equalsIgnoreCase(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String newStatus = request.getParameter("status");
                orderService.updateOrderStatus(orderId, newStatus);
                request.getSession().setAttribute("adminSuccess", "Order #" + orderId + " status updated to " + newStatus);
            } catch (Exception e) {
                request.getSession().setAttribute("adminError", "Failed to update order status: " + e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}
