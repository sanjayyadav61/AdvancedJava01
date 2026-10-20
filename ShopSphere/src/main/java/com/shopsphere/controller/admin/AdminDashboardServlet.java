package com.shopsphere.controller.admin;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Order;
import com.shopsphere.model.Product;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Admin Dashboard overview metrics.
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private OrderService orderService;
    private ProductService productService;
    private UserService userService;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        orderService = new OrderService();
        productService = new ProductService();
        userService = new UserService();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        double totalRevenue = orderService.getTotalRevenue();
        int totalOrders = orderService.getTotalOrdersCount();
        int totalProducts = productDAO.countProducts();
        int totalCustomers = userService.getCustomerCount();

        List<Product> lowStockProducts = productService.getLowStockAlerts(5);
        List<Order> recentOrders = orderService.getAllOrders();
        if (recentOrders.size() > 5) {
            recentOrders = recentOrders.subList(0, 5);
        }

        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("lowStockProducts", lowStockProducts);
        request.setAttribute("recentOrders", recentOrders);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
