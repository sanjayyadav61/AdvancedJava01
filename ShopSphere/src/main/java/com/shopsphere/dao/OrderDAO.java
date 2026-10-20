package com.shopsphere.dao;

import com.shopsphere.model.Address;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Order and OrderItem entities.
 * Demonstrates JDBC Transaction Management (setAutoCommit, commit, rollback).
 */
public class OrderDAO {

    /**
     * Creates an order with items and updates inventory atomically using JDBC Transactions.
     */
    public boolean createOrder(Order order, List<OrderItem> items) {
        String insertOrderSql = "INSERT INTO orders (user_id, address_id, total_amount, discount, payment_method, payment_status, order_status) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertItemSql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        String updateStockSql = "UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?";

        Connection conn = null;
        PreparedStatement psOrder = null;
        PreparedStatement psItem = null;
        PreparedStatement psStock = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            // Start JDBC Transaction
            conn.setAutoCommit(false);

            psOrder = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, order.getUserId());
            psOrder.setInt(2, order.getAddressId());
            psOrder.setDouble(3, order.getTotalAmount());
            psOrder.setDouble(4, order.getDiscount());
            psOrder.setString(5, order.getPaymentMethod());
            psOrder.setString(6, order.getPaymentStatus());
            psOrder.setString(7, order.getOrderStatus() != null ? order.getOrderStatus() : "PLACED");

            int affected = psOrder.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                return false;
            }

            rs = psOrder.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
                order.setOrderId(orderId);
            } else {
                conn.rollback();
                return false;
            }

            // Insert line items and deduct stock
            psItem = conn.prepareStatement(insertItemSql);
            psStock = conn.prepareStatement(updateStockSql);

            for (OrderItem item : items) {
                // Insert order item
                psItem.setInt(1, orderId);
                psItem.setInt(2, item.getProductId());
                psItem.setInt(3, item.getQuantity());
                psItem.setDouble(4, item.getPrice());
                psItem.executeUpdate();

                // Deduct inventory
                psStock.setInt(1, item.getQuantity());
                psStock.setInt(2, item.getProductId());
                psStock.setInt(3, item.getQuantity());
                int stockUpdated = psStock.executeUpdate();
                if (stockUpdated == 0) {
                    // Not enough stock
                    conn.rollback();
                    return false;
                }
            }

            // Commit atomic transaction
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {}
            }
            DBConnection.close(null, psOrder, rs);
            DBConnection.close(null, psItem, null);
            DBConnection.close(conn, psStock, null);
        }
    }

    public Order findById(int orderId) {
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "WHERE o.order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setItems(findItemsByOrderId(orderId));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public List<Order> findByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "WHERE o.user_id = ? ORDER BY o.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order o = extractOrderFromResultSet(rs);
                o.setItems(findItemsByOrderId(o.getOrderId()));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "ORDER BY o.created_at DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Order o = extractOrderFromResultSet(rs);
                o.setItems(findItemsByOrderId(o.getOrderId()));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return list;
    }

    public boolean updateStatus(int orderId, String status) {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
        return false;
    }

    public List<OrderItem> findItemsByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name, p.brand, p.image_url " +
                     "FROM order_items oi JOIN products p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                item.setProductName(rs.getString("name"));
                item.setProductBrand(rs.getString("brand"));
                item.setProductImageUrl(rs.getString("image_url"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return items;
    }

    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0.0) FROM orders WHERE payment_status = 'PAID'";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return 0.0;
    }

    public int countOrders() {
        String sql = "SELECT COUNT(*) FROM orders";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return 0;
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setUserId(rs.getInt("user_id"));
        o.setUserName(rs.getString("user_name"));
        o.setUserEmail(rs.getString("user_email"));
        o.setAddressId(rs.getInt("address_id"));
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setDiscount(rs.getDouble("discount"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setPaymentStatus(rs.getString("payment_status"));
        o.setOrderStatus(rs.getString("order_status"));
        o.setCreatedAt(rs.getTimestamp("created_at"));

        if (rs.getString("address_line") != null) {
            Address a = new Address();
            a.setAddressId(rs.getInt("address_id"));
            a.setAddressLine(rs.getString("address_line"));
            a.setCity(rs.getString("city"));
            a.setState(rs.getString("state"));
            a.setPincode(rs.getString("pincode"));
            a.setAddressType(rs.getString("address_type"));
            o.setAddress(a);
        }
        return o;
    }
}
