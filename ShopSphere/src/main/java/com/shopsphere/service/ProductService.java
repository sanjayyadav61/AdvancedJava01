package com.shopsphere.service;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.model.Review;

import java.util.List;

/**
 * Service Layer handling product catalog, inventory checks and reviews.
 */
public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    public List<Product> getAllActiveProducts() {
        return productDAO.findActive();
    }

    public List<Product> getFeaturedProducts(int limit) {
        return productDAO.findFeatured(limit);
    }

    public Product getProductById(int id) {
        return productDAO.findById(id);
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productDAO.findByCategory(categoryId);
    }

    public List<Product> searchProducts(String keyword, Integer categoryId, Double minPrice, Double maxPrice, String sortBy) {
        return productDAO.search(keyword, categoryId, minPrice, maxPrice, sortBy);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findActive();
    }

    public List<Category> getAllCategoriesAdmin() {
        return categoryDAO.findAll();
    }

    public List<Review> getProductReviews(int productId) {
        return reviewDAO.findByProductId(productId);
    }

    public double getProductAverageRating(int productId) {
        return reviewDAO.getAverageRating(productId);
    }

    public boolean addReview(int userId, int productId, int rating, String text) {
        if (rating < 1 || rating > 5) return false;
        Review r = new Review();
        r.setUserId(userId);
        r.setProductId(productId);
        r.setRating(rating);
        r.setReviewText(text);
        return reviewDAO.create(r);
    }

    public List<Product> getLowStockAlerts(int threshold) {
        return productDAO.getLowStockProducts(threshold);
    }

    public boolean saveProduct(Product p) {
        if (p.getProductId() > 0) {
            return productDAO.update(p);
        } else {
            return productDAO.create(p);
        }
    }

    public boolean deleteProduct(int id) {
        return productDAO.delete(id);
    }

    public boolean saveCategory(Category c) {
        if (c.getCategoryId() > 0) {
            return categoryDAO.update(c);
        } else {
            return categoryDAO.create(c);
        }
    }

    public boolean deleteCategory(int id) {
        return categoryDAO.delete(id);
    }
}
