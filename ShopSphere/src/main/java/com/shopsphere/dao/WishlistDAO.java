package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.model.WishlistItem;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Customer Wishlist.
 */
public class WishlistDAO {

    public List<WishlistItem> getWishlistByUser(int userId) {
        List<WishlistItem> list = new ArrayList<>();
        String sql = "SELECT w.wishlist_id, w.user_id, w.product_id, w.created_at, " +
                     "p.name, p.brand, p.price, p.discount, p.stock, p.image_url, p.status " +
                     "FROM wishlist w JOIN products p ON w.product_id = p.product_id WHERE w.user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                WishlistItem item = new WishlistItem();
                item.setWishlistId(rs.getInt("wishlist_id"));
                item.setUserId(rs.getInt("user_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setCreatedAt(rs.getTimestamp("created_at"));

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

    public boolean addToWishlist(int userId, int productId) {
        String sql = "INSERT INTO wishlist (user_id, product_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Already in wishlist or duplicate key
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    public boolean removeFromWishlist(int userId, int productId) {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
        return false;
    }

    public boolean isInWishlist(int userId, int productId) {
        String sql = "SELECT wishlist_id FROM wishlist WHERE user_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }
}
