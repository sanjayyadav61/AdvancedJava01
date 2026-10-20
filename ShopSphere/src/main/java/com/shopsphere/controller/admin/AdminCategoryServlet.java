package com.shopsphere.controller.admin;

import com.shopsphere.model.Category;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Admin Category CRUD.
 */
@WebServlet(name = "AdminCategoryServlet", urlPatterns = {"/admin/categories"})
public class AdminCategoryServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Category> categories = productService.getAllCategoriesAdmin();
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("save".equalsIgnoreCase(action)) {
                String idStr = request.getParameter("categoryId");
                int id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String iconClass = request.getParameter("iconClass");
                boolean status = request.getParameter("status") != null;

                Category c = new Category();
                c.setCategoryId(id);
                c.setName(name);
                c.setDescription(description);
                c.setIconClass(iconClass != null && !iconClass.isEmpty() ? iconClass : "bi bi-grid");
                c.setStatus(status);

                productService.saveCategory(c);
                request.getSession().setAttribute("adminSuccess", "Category saved successfully!");
            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("categoryId"));
                productService.deleteCategory(id);
                request.getSession().setAttribute("adminSuccess", "Category deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("adminError", "Operation failed: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }
}
