package com.shopsphere.dao;

import com.shopsphere.model.OrderItem;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RTU Advanced Java DAO: OrderItemDAO
 * Handles data access operations for individual items belonging to an Order.
 * Demonstrates: PreparedStatement, ResultSet traversal, transaction propagation with shared Connection.
 */
public class OrderItemDAO {

    private static final Logger LOGGER = Logger.getLogger(OrderItemDAO.class.getName());

    /**
     * Inserts an order item within an existing active transaction.
     * @param conn active JDBC Connection with transaction control
     * @param item OrderItem to insert
     * @return generated order_item_id
     * @throws SQLException on database error
     */
    public int insertOrderItem(Connection conn, OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getPrice());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    item.setOrderItemId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    /**
     * Retrieves all items belonging to a specific order ID with product details.
     * @param orderId the order ID
     * @return List of OrderItem instances
     */
    public List<OrderItem> getItemsByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.price, " +
                     "       p.name AS product_name, p.brand AS product_brand, p.image_url AS product_image " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_id = ? " +
                     "ORDER BY oi.order_item_id ASC";

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
                item.setProductName(rs.getString("product_name"));
                item.setProductBrand(rs.getString("product_brand"));
                item.setProductImageUrl(rs.getString("product_image"));
                items.add(item);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving order items for orderId: " + orderId, ex);
        } finally {
            DBConnection.close(conn, ps, rs);
        }

        return items;
    }

    /**
     * Retrieves a single order item by its primary key ID.
     * @param orderItemId the order item ID
     * @return OrderItem or null if not found
     */
    public OrderItem getById(int orderItemId) {
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.price, " +
                     "       p.name AS product_name, p.brand AS product_brand, p.image_url AS product_image " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_item_id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderItemId);
            rs = ps.executeQuery();

            if (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                item.setProductName(rs.getString("product_name"));
                item.setProductBrand(rs.getString("product_brand"));
                item.setProductImageUrl(rs.getString("product_image"));
                return item;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving order item by ID: " + orderItemId, ex);
        } finally {
            DBConnection.close(conn, ps, rs);
        }

        return null;
    }
}
