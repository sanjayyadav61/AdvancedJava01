package com.shopsphere.swing.controller;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * RTU Swing MVC Controller: ProductController
 * Manages product administration logic between Product views and ProductDAO.
 * Fulfills Section 21 of the project guide: Swing MVC Controller pattern.
 */
public class ProductController {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;

    public ProductController() {
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        return productDAO.search(keyword.trim());
    }

    public boolean saveProduct(Product product, Component parent) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Product name is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (product.getPrice() < 0) {
            JOptionPane.showMessageDialog(parent, "Price cannot be negative.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        boolean success;
        if (product.getProductId() > 0) {
            success = productDAO.update(product);
        } else {
            success = productDAO.create(product);
        }

        if (success) {
            JOptionPane.showMessageDialog(parent, "Product saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "Failed to save product in database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return success;
    }

    public boolean deleteProduct(int productId, Component parent) {
        int confirm = JOptionPane.showConfirmDialog(parent,
                "Are you sure you want to delete Product #" + productId + "?",
                "Confirm Product Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = productDAO.delete(productId);
            if (success) {
                JOptionPane.showMessageDialog(parent, "Product #" + productId + " deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(parent, "Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    public void populateTableModel(DefaultTableModel model, List<Product> products) {
        model.setRowCount(0);
        for (Product p : products) {
            model.addRow(new Object[]{
                    p.getProductId(),
                    p.getName(),
                    p.getBrand(),
                    "₹" + String.format("%.2f", p.getPrice()),
                    p.getDiscount() + "%",
                    p.getStock(),
                    p.isStatus() ? "Active" : "Inactive"
            });
        }
    }
}
