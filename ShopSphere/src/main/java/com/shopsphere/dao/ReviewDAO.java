package com.shopsphere.dao;

import com.shopsphere.model.Review;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Product Reviews and Ratings.
 */
public class ReviewDAO {

    public List<Review> findByProductId(int productId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.*, u.name AS user_name FROM reviews r " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "WHERE r.product_id = ? ORDER BY r.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Review r = new Review();
                r.setReviewId(rs.getInt("review_id"));
                r.setUserId(rs.getInt("user_id"));
                r.setUserName(rs.getString("user_name"));
                r.setProductId(rs.getInt("product_id"));
                r.setRating(rs.getInt("rating"));
                r.setReviewText(rs.getString("review_text"));
                r.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    public boolean create(Review review) {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, review_text) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getReviewText());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    review.setReviewId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    public double getAverageRating(int productId) {
        String sql = "SELECT COALESCE(AVG(rating), 0.0) FROM reviews WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            if (rs.next()) {
                double avg = rs.getDouble(1);
                return Math.round(avg * 10.0) / 10.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0.0;
    }
}
