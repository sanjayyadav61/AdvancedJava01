package com.shopsphere.swing.view;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * RTU Swing MVC View: OrderFrame
 * Standalone Order Management Frame fulfilling Section 21 of the project guide.
 * Demonstrates: JFrame, JTable, DefaultTableModel, JComboBox, JButton, JOptionPane.
 */
public class OrderFrame extends JFrame {

    private final OrderDAO orderDAO = new OrderDAO();
    private DefaultTableModel tableModel;
    private JTable orderTable;
    private JComboBox<String> statusFilterBox;

    public OrderFrame() {
        setTitle("ShopSphere - Orders Administration (Swing MVC View)");
        setSize(950, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadOrders();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Top Filter Bar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        top.add(new JLabel("Filter Status:"));
        statusFilterBox = new JComboBox<>(new String[]{"ALL", "PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"});
        top.add(statusFilterBox);
        JButton filterBtn = new JButton("Apply Filter");
        top.add(filterBtn);
        main.add(top, BorderLayout.NORTH);

        // Table
        String[] cols = {"Order ID", "Customer Name", "Total Amount", "Discount", "Final Paid", "Status", "Order Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        orderTable = new JTable(tableModel);
        orderTable.setRowHeight(25);
        main.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        // Bottom Actions
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        JButton updateStatusBtn = new JButton("Update Status");
        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn = new JButton("Close");

        bottom.add(updateStatusBtn);
        bottom.add(refreshBtn);
        bottom.add(closeBtn);
        main.add(bottom, BorderLayout.SOUTH);

        filterBtn.addActionListener(e -> applyFilter());
        statusFilterBox.addActionListener(e -> applyFilter());
        refreshBtn.addActionListener(e -> loadOrders());
        closeBtn.addActionListener(e -> dispose());

        updateStatusBtn.addActionListener(e -> {
            int row = orderTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select an order to update.", "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int orderId = (int) tableModel.getValueAt(row, 0);
            String[] options = {"PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"};
            String newStatus = (String) JOptionPane.showInputDialog(this, "Select New Status for Order #" + orderId + ":",
                    "Update Status", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (newStatus != null) {
                if (orderDAO.updateStatus(orderId, newStatus)) {
                    JOptionPane.showMessageDialog(this, "Order #" + orderId + " updated to: " + newStatus);
                    loadOrders();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update order status.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        getContentPane().add(main);
    }

    private void loadOrders() {
        tableModel.setRowCount(0);
        List<Order> orders = orderDAO.findAll();
        for (Order o : orders) {
            tableModel.addRow(new Object[]{
                    o.getOrderId(),
                    o.getUserName() != null ? o.getUserName() : "Customer #" + o.getUserId(),
                    "₹" + String.format("%.2f", o.getTotalAmount()),
                    "₹" + String.format("%.2f", o.getDiscountAmount()),
                    "₹" + String.format("%.2f", o.getFinalAmount()),
                    o.getOrderStatus(),
                    o.getCreatedAt() != null ? o.getCreatedAt().toString() : "N/A"
            });
        }
    }

    private void applyFilter() {
        String filter = (String) statusFilterBox.getSelectedItem();
        tableModel.setRowCount(0);
        List<Order> orders = orderDAO.findAll();
        for (Order o : orders) {
            if ("ALL".equalsIgnoreCase(filter) || o.getOrderStatus().equalsIgnoreCase(filter)) {
                tableModel.addRow(new Object[]{
                        o.getOrderId(),
                        o.getUserName() != null ? o.getUserName() : "Customer #" + o.getUserId(),
                        "₹" + String.format("%.2f", o.getTotalAmount()),
                        "₹" + String.format("%.2f", o.getDiscountAmount()),
                        "₹" + String.format("%.2f", o.getFinalAmount()),
                        o.getOrderStatus(),
                        o.getCreatedAt() != null ? o.getCreatedAt().toString() : "N/A"
                });
            }
        }
    }
}
