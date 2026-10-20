package com.shopsphere.dao;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Shopping Cart.
 */
public class CartDAO {

    public List<CartItem> getCartByUser(int userId) {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT c.cart_id, c.user_id, c.product_id, c.quantity, " +
                     "p.name, p.brand, p.price, p.discount, p.stock, p.image_url, p.status " +
                     "FROM cart c JOIN products p ON c.product_id = p.product_id WHERE c.user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartId(rs.getInt("cart_id"));
                item.setUserId(rs.getInt("user_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));

                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setBrand(rs.getString("brand"));
                p.setPrice(rs.getDouble("price"));
                p.setDiscount(rs.getDouble("discount"));
                p.setStock(rs.getInt("stock"));
                p.setImageUrl(rs.getString("image_url"));
                p.setStatus(rs.getBoolean("status"));

                item.setProduct(p);
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    public boolean addToCart(int userId, int productId, int quantity) {
        String checkSql = "SELECT cart_id, quantity FROM cart WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE cart SET quantity = quantity + ? WHERE cart_id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(checkSql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int cartId = rs.getInt("cart_id");
                rs.close();
                ps.close();

                ps = conn.prepareStatement(updateSql);
                ps.setInt(1, quantity);
                ps.setInt(2, cartId);
                return ps.executeUpdate() > 0;
            } else {
                rs.close();
                ps.close();

                ps = conn.prepareStatement(insertSql);
                ps.setInt(1, userId);
                ps.setInt(2, productId);
                ps.setInt(3, quantity);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    public boolean updateQuantity(int cartId, int userId, int quantity) {
        if (quantity <= 0) {
            return removeFromCart(cartId, userId);
        }
        String sql = "UPDATE cart SET quantity = ? WHERE cart_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
        return false;
    }

    public boolean removeFromCart(int cartId, int userId) {
        String sql = "DELETE FROM cart WHERE cart_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
        return false;
    }

    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
        return false;
    }

    public int getCartCount(int userId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }
}
