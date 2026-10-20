package com.shopsphere.dao;

import com.shopsphere.model.Coupon;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Coupon entity and usage tracking.
 */
public class CouponDAO {

    public Coupon findByCode(String code) {
        if (code == null || code.trim().isEmpty()) return null;
        String sql = "SELECT * FROM coupons WHERE UPPER(code) = UPPER(?) AND status = TRUE " +
                     "AND (expiry_date IS NULL OR expiry_date >= CURRENT_DATE)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, code.trim());
            rs = ps.executeQuery();
            if (rs.next()) {
                return extractCouponFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public List<Coupon> findAll() {
        List<Coupon> list = new ArrayList<>();
        String sql = "SELECT * FROM coupons ORDER BY coupon_id DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(extractCouponFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return list;
    }

    public boolean create(Coupon coupon) {
        String sql = "INSERT INTO coupons (code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, coupon.getCode().toUpperCase().trim());
            ps.setString(2, coupon.getDiscountType());
            ps.setDouble(3, coupon.getDiscountValue());
            ps.setDouble(4, coupon.getMinimumOrder());
            ps.setDouble(5, coupon.getMaximumDiscount());
            ps.setDate(6, coupon.getExpiryDate());
            ps.setBoolean(7, coupon.isStatus());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    coupon.setCouponId(rs.getInt(1));
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

    public boolean update(Coupon coupon) {
        String sql = "UPDATE coupons SET code = ?, discount_type = ?, discount_value = ?, " +
                     "minimum_order = ?, maximum_discount = ?, expiry_date = ?, status = ? WHERE coupon_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, coupon.getCode().toUpperCase().trim());
            ps.setString(2, coupon.getDiscountType());
            ps.setDouble(3, coupon.getDiscountValue());
            ps.setDouble(4, coupon.getMinimumOrder());
            ps.setDouble(5, coupon.getMaximumDiscount());
            ps.setDate(6, coupon.getExpiryDate());
            ps.setBoolean(7, coupon.isStatus());
            ps.setInt(8, coupon.getCouponId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM coupons WHERE coupon_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
        return false;
    }

    public void recordUsage(int couponId, int userId, int orderId) {
        String sql = "INSERT INTO coupon_usage (coupon_id, user_id, order_id) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setInt(3, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    private Coupon extractCouponFromResultSet(ResultSet rs) throws SQLException {
        Coupon c = new Coupon();
        c.setCouponId(rs.getInt("coupon_id"));
        c.setCode(rs.getString("code"));
        c.setDiscountType(rs.getString("discount_type"));
        c.setDiscountValue(rs.getDouble("discount_value"));
        c.setMinimumOrder(rs.getDouble("minimum_order"));
        c.setMaximumDiscount(rs.getDouble("maximum_discount"));
        c.setExpiryDate(rs.getDate("expiry_date"));
        c.setStatus(rs.getBoolean("status"));
        return c;
    }
}
