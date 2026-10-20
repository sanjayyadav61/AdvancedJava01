package com.shopsphere.dao;

import com.shopsphere.model.Payment;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object for Payment entity.
 */
public class PaymentDAO {

    public boolean createPayment(Payment payment) {
        String sql = "INSERT INTO payments (order_id, payment_method, transaction_reference, amount, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getTransactionReference());
            ps.setDouble(4, payment.getAmount());
            ps.setString(5, payment.getPaymentStatus() != null ? payment.getPaymentStatus() : "SUCCESS");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    payment.setPaymentId(rs.getInt(1));
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

    public Payment findByOrderId(int orderId) {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("payment_id"));
                p.setOrderId(rs.getInt("order_id"));
                p.setPaymentMethod(rs.getString("payment_method"));
                p.setTransactionReference(rs.getString("transaction_reference"));
                p.setAmount(rs.getDouble("amount"));
                p.setPaymentStatus(rs.getString("payment_status"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }
}
