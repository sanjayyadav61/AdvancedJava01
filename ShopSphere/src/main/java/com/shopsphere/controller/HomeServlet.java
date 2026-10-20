package com.shopsphere.controller;

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
 * Controller for Home page.
 * Loads featured categories and deals for index.jsp.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home", "/index"})
public class HomeServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Category> categories = productService.getAllCategories();
        List<Product> featured = productService.getFeaturedProducts(8);
        List<Product> allProducts = productService.getAllActiveProducts();

        request.setAttribute("categories", categories);
        request.setAttribute("featuredProducts", featured);
        request.setAttribute("allProducts", allProducts);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
