package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;

import java.util.List;

/**
 * Service Layer for Cart operations and cart calculations.
 */
public class CartService {

    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public List<CartItem> getCartItems(int userId) {
        return cartDAO.getCartByUser(userId);
    }

    public boolean addToCart(int userId, int productId, int quantity) {
        Product p = productDAO.findById(productId);
        if (p == null || !p.isAvailable() || quantity <= 0) {
            return false;
        }
        return cartDAO.addToCart(userId, productId, quantity);
    }

    public boolean updateQuantity(int cartId, int userId, int quantity) {
        return cartDAO.updateQuantity(cartId, userId, quantity);
    }

    public boolean removeFromCart(int cartId, int userId) {
        return cartDAO.removeFromCart(cartId, userId);
    }

    public boolean clearCart(int userId) {
        return cartDAO.clearCart(userId);
    }

    public int getCartCount(int userId) {
        return cartDAO.getCartCount(userId);
    }

    public double calculateSubtotal(List<CartItem> items) {
        double subtotal = 0.0;
        if (items != null) {
            for (CartItem item : items) {
                subtotal += item.getItemTotal();
            }
        }
        return Math.round(subtotal * 100.0) / 100.0;
    }
}
