package com.shopsphere.controller.admin;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Admin Product Catalog CRUD and Inventory Management.
 */
@WebServlet(name = "AdminProductServlet", urlPatterns = {"/admin/products"})
public class AdminProductServlet extends HttpServlet {

    private ProductService productService;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Product> products = productDAO.findAll();
        List<Category> categories = productService.getAllCategoriesAdmin();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("save".equalsIgnoreCase(action)) {
                String idStr = request.getParameter("productId");
                int id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;

                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                String name = request.getParameter("name");
                String brand = request.getParameter("brand");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                double discount = Double.parseDouble(request.getParameter("discount"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                String imageUrl = request.getParameter("imageUrl");
                boolean status = request.getParameter("status") != null;

                Product p = new Product();
                p.setProductId(id);
                p.setCategoryId(categoryId);
                p.setName(name);
                p.setBrand(brand);
                p.setDescription(description);
                p.setPrice(price);
                p.setDiscount(discount);
                p.setStock(stock);
                p.setImageUrl(imageUrl);
                p.setStatus(status);

                productService.saveProduct(p);
                request.getSession().setAttribute("adminSuccess", "Product saved successfully!");
            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("productId"));
                productService.deleteProduct(id);
                request.getSession().setAttribute("adminSuccess", "Product removed.");
            } else if ("updateStock".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("productId"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                productDAO.setStock(id, stock);
                request.getSession().setAttribute("adminSuccess", "Stock updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("adminError", "Operation failed: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
}
