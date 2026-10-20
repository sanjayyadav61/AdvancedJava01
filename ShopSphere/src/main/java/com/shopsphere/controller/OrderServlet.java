package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Customer Order history and details.
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/order-details"})
public class OrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String path = request.getServletPath();

        if ("/order-details".equals(path)) {
            String idStr = request.getParameter("id");
            if (idStr == null) {
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }
            int orderId = Integer.parseInt(idStr);
            Order order = orderService.getOrderDetails(orderId);

            // Security check: ensure order belongs to current user (unless admin)
            if (order == null || (!user.isAdmin() && order.getUserId() != user.getUserId())) {
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }

            request.setAttribute("order", order);
            request.getRequestDispatcher("/order-details.jsp").forward(request, response);
        } else {
            List<Order> orders = orderService.getUserOrders(user.getUserId());
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/orders.jsp").forward(request, response);
        }
    }
}
